package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.bean.vo.CommentVO;
import com.gdufe.health_butler.common.enums.CommunityRecordType;
import com.gdufe.health_butler.common.enums.InfoRecordType;
import com.gdufe.health_butler.common.exception.ParamErrorException;
import com.gdufe.health_butler.entity.CommunityRecord;
import com.gdufe.health_butler.dao.CommunityRecordMapper;
import com.gdufe.health_butler.entity.InfoRecord;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.CommunityRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdufe.health_butler.service.CommunityService;
import com.gdufe.health_butler.service.InfoRecordService;
import com.gdufe.health_butler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 社区点赞打赏记录表 服务实现类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-04
 */
@Service
public class CommunityRecordServiceImpl extends ServiceImpl<CommunityRecordMapper, CommunityRecord> implements CommunityRecordService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private InfoRecordService infoRecordService;

    @Autowired
    private CommunityService communityService;

    @Override
    public List<User> getPraise(long cid) {
        QueryWrapper<CommunityRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CommunityRecord::getCid, cid)
                .eq(CommunityRecord::getCategory, CommunityRecordType.PRAISE.getValue())
                .eq(CommunityRecord::getIsDelete, false);
        List<CommunityRecord> communityRecordList = list(queryWrapper);
        List<User> userList = new ArrayList<>();
        communityRecordList.forEach(communityRecord ->
                userList.add(userService.filter(userService.getById(communityRecord.getFUid()))));
        return userList;
    }

    @Override
    public boolean isPraise(long uid, long cid) {
        QueryWrapper<CommunityRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CommunityRecord::getCid, cid).eq(CommunityRecord::getFUid, uid)
                .eq(CommunityRecord::getCategory, CommunityRecordType.PRAISE.getValue())
                .eq(CommunityRecord::getIsDelete, false);
        CommunityRecord communityRecord = getOne(queryWrapper);
        return communityRecord != null;
    }

    @Override
    public String praise(String token, long cid, int type) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        long uid = user.getId();
        logger.info("[op:praise, uid:{}, token:{}, cid:{}, type:{}]", uid, token, cid, type);
        QueryWrapper<CommunityRecord> communityRecordQueryWrapper = new QueryWrapper<>();
        communityRecordQueryWrapper.lambda().eq(CommunityRecord::getCid, cid)
                .eq(CommunityRecord::getFUid, uid).eq(CommunityRecord::getCategory, CommunityRecordType.PRAISE.getValue())
                .eq(CommunityRecord::getIsDelete, false);
        CommunityRecord communityRecord = getOne(communityRecordQueryWrapper);
        switch (type) {
            case 0:
                // 取消点赞 直接删除
                if (communityRecord != null) {
                    communityRecord.setIsDelete(true);
                    updateById(communityRecord);

                    // 存入消息记录

                }
                break;
            case 1:
                // 点赞
                // 判断是否已经点过赞 （保持幂等性）
                if (communityRecord == null) {
                    communityRecord = new CommunityRecord();
                    communityRecord.setCid(cid);
                    communityRecord.setCategory(CommunityRecordType.PRAISE.getValue());
                    communityRecord.setCreateTime(System.currentTimeMillis());
                    communityRecord.setFUid(uid);
                    communityRecord.setModifiedTime(System.currentTimeMillis());
                    save(communityRecord);

                    // 存入消息记录
                    InfoRecord infoRecord = new InfoRecord();
                    infoRecord.setMid(communityRecord.getId());
                    infoRecord.setCreateTime(System.currentTimeMillis());
                    infoRecord.setModifiedTime(System.currentTimeMillis());
                    infoRecord.setReadStatus(false);
                    infoRecord.setType(InfoRecordType.PRAISE.getValue());
                    infoRecord.setUid(communityService.getById(communityRecord.getCid()).getUid());
                    infoRecordService.save(infoRecord);
                }
                break;
            default:
                throw new ParamErrorException("type类型不正确");
        }
        return "操作成功";
    }

    @Override
    public void comment(String token, long cid, String content) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        long uid = user.getId();
        logger.info("[op:comment, uid:{}, token:{}, cid:{}, content:{}]", uid, token, cid, content);
        CommunityRecord communityRecord = new CommunityRecord();
        communityRecord.setCid(cid);
        communityRecord.setCategory(CommunityRecordType.COMMENT.getValue());
        communityRecord.setModifiedTime(System.currentTimeMillis());
        communityRecord.setCreateTime(System.currentTimeMillis());
        communityRecord.setFUid(uid);
        communityRecord.setContent(content);
        save(communityRecord);

        // 存入消息记录
        infoRecordService.save(buildCommentInfoRecord(communityRecord));
    }

    @Override
    public List<CommentVO> getComment(long cid) {
        QueryWrapper<CommunityRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CommunityRecord::getCid, cid)
                .eq(CommunityRecord::getCategory, CommunityRecordType.COMMENT.getValue())
                .eq(CommunityRecord::getIsDelete, false);
        List<CommunityRecord> communityRecordList = list(queryWrapper);
        List<CommentVO> commentList = new ArrayList<>();
        communityRecordList.forEach(communityRecord -> {
            CommentVO commentVO = new CommentVO();
            commentVO.setRid(communityRecord.getId());
            commentVO.setFromId(communityRecord.getFUid());
            commentVO.setFromName(userService.getById(communityRecord.getFUid()).getNickName());
            Long toUid = communityRecord.getToUid();
            commentVO.setToId(null == toUid ? 0 : toUid);
            commentVO.setToName(null != toUid ? userService.getById(toUid)
                    .getNickName() : "");
            commentVO.setContent(communityRecord.getContent());
            commentList.add(commentVO);
        });
        return commentList;
    }

    @Override
    public void replay(String token, long cid, String content, long toUid) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        long uid = user.getId();
        logger.info("[op:replay, uid:{}, token:{}, cid:{}, content:{}, toUid:{}]", uid, token, cid, content, toUid);
        CommunityRecord communityRecord = new CommunityRecord();
        communityRecord.setCid(cid);
        communityRecord.setCategory(CommunityRecordType.COMMENT.getValue());
        communityRecord.setModifiedTime(System.currentTimeMillis());
        communityRecord.setCreateTime(System.currentTimeMillis());
        communityRecord.setFUid(uid);
        communityRecord.setToUid(toUid);
        communityRecord.setContent(content);
        save(communityRecord);

        // 存入消息记录
        infoRecordService.save(buildCommentInfoRecord(communityRecord));
    }

    @Override
    public void deleteComment(String token, long rid) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        long uid = user.getId();
        logger.info("[op:deleteComment, uid:{}, token:{}, rid:{}]", uid, token, rid);
        CommunityRecord communityRecord = getById(rid);
        if (communityRecord.getFUid() == uid) {
            communityRecord.setIsDelete(true);
            updateById(communityRecord);
        } else {
            throw new ParamErrorException("参数错误");
        }
    }

    /**
     * 给评论或回复生成InfoRecord
     * @param communityRecord
     * @return
     */
    private InfoRecord buildCommentInfoRecord(CommunityRecord communityRecord) {
        InfoRecord infoRecord = new InfoRecord();
        infoRecord.setMid(communityRecord.getId());
        infoRecord.setCreateTime(System.currentTimeMillis());
        infoRecord.setModifiedTime(System.currentTimeMillis());
        infoRecord.setReadStatus(false);
        infoRecord.setType(InfoRecordType.COMMUNITY.getValue());
        if(communityRecord.getToUid() != null && communityRecord.getToUid()!=0) {
            infoRecord.setUid(communityRecord.getToUid());
        } else {
            infoRecord.setUid(communityService.getById(communityRecord.getCid()).getUid());
        }
        return infoRecord;
    }
}
