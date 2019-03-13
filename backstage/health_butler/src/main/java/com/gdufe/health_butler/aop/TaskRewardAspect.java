package com.gdufe.health_butler.aop;

import com.gdufe.health_butler.common.enums.DealType;
import com.gdufe.health_butler.entity.CoinDetail;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.service.CoinDetailService;
import com.gdufe.health_butler.service.CommunityService;
import com.gdufe.health_butler.service.UserService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: laichengfeng
 * @Description: 任务奖励切面， 针对用户完成某个任务时，做一些奖励
 * @Date: 2019/3/13 13:58
 */
@Aspect
@Component
public class TaskRewardAspect {

    private final static Logger logger = LoggerFactory.getLogger(TaskRewardAspect.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CoinDetailService coinDetailService;

    @Autowired
    private CommunityService communityService;

    @Value("${rewardCoin.share}")
    private long shareRewardCoin;

    /**
     * 当用户成功分享健康动态时，奖励健康币
     */
    @AfterReturning(value = "execution(public long share(String, String, boolean))", returning = "result")
    public void afterReturning(Object result) {
        logger.info("[op:share get reward, result:{}]", result);
        long uid = communityService.getById((long)result).getUid();
        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setToUid(0L);
        coinDetail.setCoin(shareRewardCoin);
        coinDetail.setDescription("分享健康动态得币");
        coinDetail.setCreateTime(System.currentTimeMillis());
        coinDetail.setModifiedTime(System.currentTimeMillis());
        coinDetail.setUid(uid);
        coinDetail.setType(DealType.DO_TASK.getValue());
        coinDetailService.save(coinDetail);
        User user = userService.getById(uid);
        user.setHealthCoin(user.getHealthCoin() + shareRewardCoin);
        userService.updateById(user);
    }

}
