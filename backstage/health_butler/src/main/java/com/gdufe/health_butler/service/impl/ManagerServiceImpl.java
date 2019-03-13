package com.gdufe.health_butler.service.impl;

import com.gdufe.health_butler.aop.TaskRewardAspect;
import com.gdufe.health_butler.common.enums.DealType;
import com.gdufe.health_butler.entity.CoinDetail;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.service.CoinDetailService;
import com.gdufe.health_butler.service.ManagerService;
import com.gdufe.health_butler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: laichengfeng
 * @Description: 后台管理服务
 * @Date: 2019/3/13 14:36
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    private final static Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CoinDetailService coinDetailService;

    @Override
    public String give(long uid, long coin) {
        logger.info("[op:give, uid:{}, coin:{}]", uid, coin);
        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setToUid(0L);
        coinDetail.setCoin(coin);
        coinDetail.setDescription("系统赠送健康币");
        coinDetail.setCreateTime(System.currentTimeMillis());
        coinDetail.setModifiedTime(System.currentTimeMillis());
        coinDetail.setUid(uid);
        coinDetail.setType(DealType.SYSTEM_PRESENT.getValue());
        coinDetailService.save(coinDetail);
        User user = userService.getById(uid);
        user.setHealthCoin(user.getHealthCoin() + coin);
        userService.updateById(user);
        return "赠送成功";
    }
}
