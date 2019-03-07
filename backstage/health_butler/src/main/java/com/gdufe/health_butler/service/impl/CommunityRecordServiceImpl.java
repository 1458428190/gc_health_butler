package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.common.enums.CommunityRecordType;
import com.gdufe.health_butler.common.exception.ParamErrorException;
import com.gdufe.health_butler.entity.CommunityRecord;
import com.gdufe.health_butler.dao.CommunityRecordMapper;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.CommunityRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdufe.health_butler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class CommunityRecordServiceImpl extends ServiceImpl<CommunityRecordMapper, CommunityRecord> implements CommunityRecordService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Override
    public List<User> getPraise(long cid) {
        QueryWrapper<CommunityRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CommunityRecord::getCid, cid)
                .eq(CommunityRecord::getCategory, CommunityRecordType.PRAISE.getValue());
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
                .eq(CommunityRecord::getCategory, CommunityRecordType.PRAISE.getValue());
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
                .eq(CommunityRecord::getFUid, uid).eq(CommunityRecord::getCategory, CommunityRecordType.PRAISE.getValue());
        CommunityRecord communityRecord = getOne(communityRecordQueryWrapper);
        switch (type) {
            case 0:
                // 取消点赞 直接删除
                if(communityRecord != null) {
                    removeById(communityRecord.getId());
                }
                break;
            case 1:
                // 点赞
                // 判断是否已经点过赞 （保持幂等性）
                if(communityRecord == null) {
                    communityRecord = new CommunityRecord();
                    communityRecord.setCid(cid);
                    communityRecord.setCategory(CommunityRecordType.PRAISE.getValue());
                    communityRecord.setCreateTime(System.currentTimeMillis());
                    communityRecord.setFUid(uid);
                    communityRecord.setModifiedTime(System.currentTimeMillis());
                    save(communityRecord);
                }
                break;
            default:
                throw new ParamErrorException("type类型不正确");
        }
        return "操作成功";
    }
}
