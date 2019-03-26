package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdufe.health_butler.bean.vo.CommunityVO;
import com.gdufe.health_butler.common.exception.ParamErrorException;
import com.gdufe.health_butler.common.exception.SystemErrorException;
import com.gdufe.health_butler.common.util.MinioUtils;
import com.gdufe.health_butler.common.util.ResourceLockUtils;
import com.gdufe.health_butler.entity.Community;
import com.gdufe.health_butler.dao.CommunityMapper;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.CommunityRecordService;
import com.gdufe.health_butler.service.CommunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdufe.health_butler.service.UserService;
import io.minio.MinioClient;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 社区分享表 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${minio.imgBucket}")
    private String imgBucket;

    @Value("${img.separator}")
    private String imgSeparator;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityRecordService communityRecordService;

    @Override
    public List<CommunityVO> listByToken(String token) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        logger.info("[op:listByToken, uid:{}, token:{}]", user.getId(), token);
        QueryWrapper<Community> communityQueryWrapper = new QueryWrapper<>();
        communityQueryWrapper.lambda().eq(Community::getIsDelete, false).and(wrapper ->
                wrapper.eq(Community::getUid, user.getId()).or().eq(Community::getOnlyMe, false))
                .orderByDesc(Community::getCreateTime);
        return getCommunityVoList(list(communityQueryWrapper), user.getId());
    }

    @Override
    public long share(String token, String content, boolean onlyMe) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:share, uid:{}, token:{}, content:{}, onlyMe:{}]", uid, token, content, onlyMe);

        Community community = new Community();
        community.setContent(content);
        community.setCreateTime(System.currentTimeMillis());
        community.setIsDelete(false);
        community.setModifiedTime(System.currentTimeMillis());
        community.setOnlyMe(onlyMe);
        community.setPraise(0);
        community.setReward(0);
        community.setUid(uid);
        save(community);

        logger.info("[op_rslt: success, cid:{}]", community.getId());
        return community.getId();
    }

    @Override
    public String delete(String token, long cid) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        logger.info("[op:share, uid:{}, token:{}, cid:{}]", user.getId(), token, cid);
        Community community = getById(cid);
        if(community.getUid() == user.getId()) {
            community.setIsDelete(true);
            boolean update = updateById(community);
            logger.info("[op_rslt: success, update:{}]", update);
            return update?"删除成功":"删除失败";
        } else {
            logger.warn("[op_rslt: no auth");
            return "您没有权限删除此动态";
        }
    }

    @Override
    public List<CommunityVO> getMe(String token) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        logger.info("[op:getMe, uid:{}, token:{}]", user.getId(), token);
        QueryWrapper<Community> communityQueryWrapper = new QueryWrapper<>();
        communityQueryWrapper.lambda().eq(Community::getIsDelete, false).and(wrapper ->
                wrapper.eq(Community::getUid, user.getId())).orderByDesc(Community::getCreateTime);
        return getCommunityVoList(list(communityQueryWrapper), user.getId());
    }

    /**
     * 当出现恶意并发调用此接口时，会导致并发问题（1：加入了多张图片，超过9张、2：后者覆盖前者）
     *      TODO 解决方法:  折中方法（牺牲一点网络请求，保证数据正确性）
     *          1. 先上传图片并获取到url
     *          2. 调用 事务方法 updateImgUrlList （改变imgUrlList）
     *
     * @param token
     * @param cid
     * @param file
     * @return
     */
    @Override
    public String upload(String token, long cid, MultipartFile file, int imgNo) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        String originalFileName = file.getOriginalFilename();
        logger.info("[op:upload, uid:{}, token:{}, cid:{}, file:{}, imgNo:{}]", uid, token, cid,
                originalFileName, imgNo);

        if(file.isEmpty()) {
            throw new ParamErrorException("文件有误");
        }

        // 验证cid是否为此登录用户
        Community community = getById(cid);
        if(community == null || community.getUid() != uid) {
            throw new ParamErrorException("cid有误");
        }

        try {
            String newImgUrl = MinioUtils.FileUploaderByStream(minioClient, imgBucket, file.getInputStream(),
                    System.currentTimeMillis() + originalFileName);
            updateImgUrlList(cid, newImgUrl, imgNo);
        } catch (Exception e) {
            logger.error("[op_rslt: error]", e);
            throw new SystemErrorException("内部系统出错", e);
        }
        return "上传成功";
    }

    /**
     * 获取健康小圈
     * @param communityList
     * @param uid
     * @return
     */
    private List<CommunityVO> getCommunityVoList(List<Community> communityList, long uid) {
        List<CommunityVO> list = new ArrayList<>();
        communityList.forEach(community -> {
            CommunityVO communityVO = new CommunityVO();
            communityVO.setCommunity(community);
            communityVO.setUser(userService.filter(userService.getById(community.getUid())));
            communityVO.setPraise(communityRecordService.isPraise(uid, community.getId()));
            communityVO.setPraiseUser(communityRecordService.getPraise(community.getId()));
            communityVO.setCommentList(communityRecordService.getComment(community.getId()));
            list.add(communityVO);
        });
        return list;
    }

    /**
     * 以事务的形式 更新imgUrlList
     *     old1-已舍弃:    设置隔离级别为系列化，即顺序执行（为了准确的判断是否超过9张，同时为了防止覆盖）（效果未如愿）
     *                      循环争取锁
     *     old2: 增加imgNo, 和sum 判断当前上传的是第几张，如果跟当前张数一致， 便上传，否则自旋200ms等待。(造成死锁等待)
     *     old3: 使用java锁
     *     new: 编写更新语句
     * @param cid
     * @param newImgUrl
     */
    @Transactional(isolation=Isolation.SERIALIZABLE)
    public void updateImgUrlList(long cid, String newImgUrl, int imgNo) {
        logger.info("[op:updateImgUrlList, cid: {}, newImgUrl:{}, imgNo:{}]", cid, newImgUrl, imgNo);

        // 判断是否超过9张
        Community community = getById(cid);
        String imgUrlList = community.getImgUrlList();
        List<String> newImgUrlList = new ArrayList<>();
        if (StringUtils.isNotBlank(imgUrlList)) {
            newImgUrlList = new ArrayList<>(Arrays.asList(imgUrlList.split(imgSeparator)));
        }
        if (newImgUrlList.size() >= 9) {
            throw new ParamErrorException("您的图片数量已经超了");
        }

        String updateSql = "img_url_list=(case when img_url_list='' then '"
                + newImgUrl +"' else concat(img_url_list, '"+(imgSeparator+newImgUrl)+"') end) where id="+cid;
        logger.info("[updateSql: {}]", updateSql);
        UpdateWrapper<Community> communityUpdateWrapper = new UpdateWrapper<>();
        communityUpdateWrapper.setSql(updateSql);
        update(communityUpdateWrapper);
    }

    @Override
    public int listSize(String token) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        logger.info("[op:listSize, uid:{}, token:{}]", user.getId(), token);
        QueryWrapper<Community> communityQueryWrapper = new QueryWrapper<>();
        communityQueryWrapper.lambda().eq(Community::getIsDelete, false).and(wrapper ->
                wrapper.eq(Community::getUid, user.getId()).or().eq(Community::getOnlyMe, false))
                .orderByDesc(Community::getCreateTime);
        int count = count(communityQueryWrapper);
        logger.info("[op_rslt: success, count:{}]", count);
        return count;
    }

    @Override
    public List<CommunityVO> pageList(String token, int pageNo, int size) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        logger.info("[op:pageList, uid:{}, token:{}]", user.getId(), token);
        QueryWrapper<Community> communityQueryWrapper = new QueryWrapper<>();
        communityQueryWrapper.lambda().eq(Community::getIsDelete, false).and(wrapper ->
                wrapper.eq(Community::getUid, user.getId()).or().eq(Community::getOnlyMe, false))
                .orderByDesc(Community::getCreateTime);
        Page<Community> page = new Page<>(pageNo, size);
        List<Community> communityList = page(page, communityQueryWrapper).getRecords();
        return getCommunityVoList(communityList, user.getId());
    }

    @Override
    public CommunityVO getByCid(String token, long cid) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        logger.info("[op:getByCid, uid:{}, token:{}, cid:{}]", user.getId(), token, cid);
        Community community = getById(cid);
        return getCommunityVoList(Collections.singletonList(community), user.getId()).get(0);
    }

}
