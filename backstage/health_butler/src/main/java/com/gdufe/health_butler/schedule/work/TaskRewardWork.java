package com.gdufe.health_butler.schedule.work;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.common.enums.DealType;
import com.gdufe.health_butler.common.enums.RecordType;
import com.gdufe.health_butler.common.util.TimeUtils;
import com.gdufe.health_butler.entity.CoinDetail;
import com.gdufe.health_butler.entity.Record;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.service.CoinDetailService;
import com.gdufe.health_butler.service.RecordService;
import com.gdufe.health_butler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.List;

/**
 * @Author: laichengfeng
 * @Description: 任务奖励工作
 * @Date: 2019/3/13 13:49
 */
@Component
public class TaskRewardWork {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private CoinDetailService coinDetailService;

    @Value("${rewardCoin.clock}")
    private long clockRewardCoin;

    @Value("${rewardCoin.step}")
    private long stepRewardCoin;

    /**
     * 步数超1万，或者前三名
     */
    public void stepReward() {
        logger.info("[op:stepReward]");
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().gt(User::getNowStep, 0).orderByDesc(User::getNowStep);
        List<User> userList = userService.list(userQueryWrapper);
        // 前三名
        if(userList.size() > 0) {
            deal(userList.get(0).getId(), 3 * stepRewardCoin, DealType.DO_TASK, "今日步数冠军得币");
        }
        if(userList.size() > 1) {
            deal(userList.get(1).getId(), 2 * stepRewardCoin, DealType.DO_TASK, "今日步数亚军得币");
        }
        if(userList.size() > 2) {
            deal(userList.get(2).getId(), stepRewardCoin, DealType.DO_TASK, "今日步数季军得币");
        }
        for(User user: userList) {
            if(user.getNowStep() >= 10000) {
                deal(user.getId(), stepRewardCoin, DealType.DO_TASK, "今日步数超1万得币");
            }
        }
    }

    /**
     * 打卡奖励
     */
    public void clockReward() {
        logger.info("[op:ClockReward]");
        List<Long> todayTime = TimeUtils.getTodayTime();
        long startTime = todayTime.get(0);
        long endTime = todayTime.get(1);
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        // 早起， 早睡， 运动
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.MORNING_CLOCK_IN.getValue())
                .between(Record::getCreateTime, startTime, endTime);
        List<Record> morningRecordList = recordService.list(recordQueryWrapper);
        recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.GOOD_NIGHT_CLOCK_IN.getValue())
                .between(Record::getCreateTime, startTime, endTime);
        List<Record> goodNightRecordList = recordService.list(recordQueryWrapper);
        recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.SPORT_CLOCK_IN.getValue())
                .between(Record::getCreateTime, startTime, endTime);
        List<Record> sportRecordList = recordService.list(recordQueryWrapper);
        handlerClockRecord(morningRecordList);
        handlerClockRecord(goodNightRecordList);
        handlerClockRecord(sportRecordList);
    }

    private void handlerClockRecord(List<Record> recordList) {
        if(recordList.size() == 0) {
            return;
        }
        String recordType = "运动";
        if(recordList.get(0).getType() == RecordType.MORNING_CLOCK_IN.getValue()) {
            recordType = "早起";
        }
        if(recordList.get(0).getType() == RecordType.GOOD_NIGHT_CLOCK_IN.getValue()) {
            recordType = "早睡";
        }
        // 奖励所有人
        for(Record record: recordList) {
            long uid = record.getUid();
            deal(uid, clockRewardCoin, DealType.DO_TASK, recordType + "打卡得币");
        }

        // 奖励冠军，亚军，季军
        long uid_1 = recordList.get(0).getUid();
        deal(uid_1, 3 * clockRewardCoin, DealType.DO_TASK, recordType + "冠军打卡得币");

        if(recordList.size() >= 2) {
            long uid_2 = recordList.get(1).getUid();
            deal(uid_2, 2 * clockRewardCoin, DealType.DO_TASK, recordType + "亚军打卡得币");
        }

        if(recordList.size() >= 3) {
            long uid_3 = recordList.get(2).getUid();
            deal(uid_3, clockRewardCoin, DealType.DO_TASK, recordType + "季军打卡得币");
        }
    }

    /**
     * 单方产生交易
     */
    private void deal(long uid, long coin, DealType dealType, String description) {
        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setToUid(0L);
        coinDetail.setCoin(coin);
        coinDetail.setDescription(description);
        coinDetail.setCreateTime(System.currentTimeMillis());
        coinDetail.setModifiedTime(System.currentTimeMillis());
        coinDetail.setUid(uid);
        coinDetail.setType(dealType.getValue());
        coinDetailService.save(coinDetail);
        User user = userService.getById(uid);
        user.setHealthCoin(user.getHealthCoin() + coin);
        userService.updateById(user);
    }
}
