package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdufe.health_butler.bean.vo.InfoRecordVO;
import com.gdufe.health_butler.common.enums.InfoRecordType;
import com.gdufe.health_butler.entity.*;
import com.gdufe.health_butler.dao.InfoRecordMapper;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户消息记录表 服务实现类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-25
 */
@Service
public class InfoRecordServiceImpl extends ServiceImpl<InfoRecordMapper, InfoRecord> implements InfoRecordService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${img.separator}")
    private String imgSeparator;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinDetailService coinDetailService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CommunityRecordService communityRecordService;

    @Override
    public int getUnReadInfoCount(String token) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:getUnReadInfoCount, uid:{}, token:{}]", uid, token);
        QueryWrapper<InfoRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InfoRecord::getReadStatus, false).eq(InfoRecord::getUid, uid);
        int unReadInfoCount = count(queryWrapper);
        logger.info("[op_rslt: success, unReadInfoCount:{}]", unReadInfoCount);
        return unReadInfoCount;
    }

    @Override
    public List<InfoRecordVO> getUnReadInfo(String token) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:getUnReadInfo, uid:{}, token:{}]", uid, token);
        QueryWrapper<InfoRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InfoRecord::getUid, uid).eq(InfoRecord::getReadStatus, false)
                .orderByDesc(InfoRecord::getCreateTime);
        List<InfoRecord> infoRecordList = list(queryWrapper);
        // 改为已读
        UpdateWrapper<InfoRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(InfoRecord::getReadStatus, true).eq(InfoRecord::getUid, uid);
        update(updateWrapper);
        return convertInfoRecordVO(infoRecordList);
    }

    @Override
    public List<InfoRecordVO> getReadInfo(String token) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:getReadInfo, uid:{}, token:{}]", uid, token);
        QueryWrapper<InfoRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InfoRecord::getUid, uid).eq(InfoRecord::getReadStatus, true)
                .orderByDesc(InfoRecord::getCreateTime);
        List<InfoRecord> infoRecordList = list(queryWrapper);
        return convertInfoRecordVO(infoRecordList);
    }

    /**
     * 将InfoRecord转化为InfoRecordVO
     *
     * @param infoRecordList
     * @return
     */
    private List<InfoRecordVO> convertInfoRecordVO(List<InfoRecord> infoRecordList) {
        List<InfoRecordVO> list = new ArrayList<>();
        for (InfoRecord infoRecord : infoRecordList) {
            InfoRecordVO infoRecordVO = new InfoRecordVO();
            infoRecordVO.setRecordTime(infoRecord.getCreateTime());
            InfoRecordType type = InfoRecordType.of(infoRecord.getType());
            infoRecordVO.setType(type.getValue());
            switch (type) {
                case REWARD:
                    CoinDetail coinDetail = coinDetailService.getById(infoRecord.getMid());
                    infoRecordVO.setInfoContent("打赏"+coinDetail.getCoin()+"健康币给你");
                    long uid = coinDetail.getToUid();
                    infoRecordVO.setUid(uid);
                    User user = userService.getById(uid);
                    infoRecordVO.setNickName(user.getNickName());
                    infoRecordVO.setAvatarUrl(user.getAvatarUrl());
                    break;
                case COMMUNITY:
                    CommunityRecord communityRecord = communityRecordService.getById(infoRecord.getMid());
                    Community community = communityService.getById(communityRecord.getCid());
                    String infoContentPrefix = communityRecord.getToUid() != null ? "回复了你:" : "评论了你:";
                    infoRecordVO.setInfoContent(infoContentPrefix + communityRecord.getContent());
                    if (StringUtils.isNotBlank(community.getImgUrlList())) {
                        infoRecordVO.setOriginImgUrl(community.getImgUrlList().split(imgSeparator)[0]);
                    } else if (StringUtils.isNotBlank(community.getContent())) {
                        infoRecordVO.setOriginContent(community.getContent());
                    }
                    uid = communityRecord.getFUid();
                    infoRecordVO.setUid(uid);
                    user = userService.getById(uid);
                    infoRecordVO.setNickName(user.getNickName());
                    infoRecordVO.setAvatarUrl(user.getAvatarUrl());
                    infoRecordVO.setCid(community.getId());
                    break;
                case PRAISE:
                    CommunityRecord communityPraiseRecord = communityRecordService.getById(infoRecord.getMid());
                    Community communityPraise = communityService.getById(communityPraiseRecord.getCid());
                    infoRecordVO.setInfoContent("给你点赞了");
                    if (StringUtils.isNotBlank(communityPraise.getImgUrlList())) {
                        infoRecordVO.setOriginImgUrl(communityPraise.getImgUrlList().split(imgSeparator)[0]);
                    } else if (StringUtils.isNotBlank(communityPraise.getContent())) {
                        infoRecordVO.setOriginContent(communityPraise.getContent());
                    }
                    uid = communityPraiseRecord.getFUid();
                    infoRecordVO.setUid(uid);
                    user = userService.getById(uid);
                    infoRecordVO.setNickName(user.getNickName());
                    infoRecordVO.setAvatarUrl(user.getAvatarUrl());
                    infoRecordVO.setCid(communityPraise.getId());
                    break;
                case COIN_REWARD:
                    CoinDetail coinRewardDetail = coinDetailService.getById(infoRecord.getMid());
                    infoRecordVO.setInfoContent(coinRewardDetail.getDescription());
                    uid = coinRewardDetail.getUid();
                    infoRecordVO.setUid(uid);
                    user = userService.getById(uid);
                    infoRecordVO.setNickName(user.getNickName());
                    infoRecordVO.setAvatarUrl(user.getAvatarUrl());
                    break;
                default:
                    break;
            }
            list.add(infoRecordVO);
        }
        return list;
    }
}
