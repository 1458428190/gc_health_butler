package com.gdufe.health_butler.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.bean.vo.ClockInVO;
import com.gdufe.health_butler.common.enums.RecordType;
import com.gdufe.health_butler.common.exception.ParamErrorException;
import com.gdufe.health_butler.common.util.TimeUtils;
import com.gdufe.health_butler.entity.Record;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.ClockInService;
import com.gdufe.health_butler.service.RecordService;
import com.gdufe.health_butler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 打卡服务
 * @Date: 2019/3/3 20:49
 */
@Service
public class ClockInServiceImpl implements ClockInService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    /**
     * 获取打卡记录
     * @param token
     * @return
     */
    @Override
    public List<ClockInVO> list(String token) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:queryClockInRecordList, uid:{}, token:{}]", uid, token);

        List<ClockInVO> clockInVOList = new ArrayList<>();

        // 早起打卡记录
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.MORNING_CLOCK_IN.getValue())
                .eq(Record::getUid, uid);
        List<Record> morningRecord = recordService.list(recordQueryWrapper);
        ClockInVO morningClockInVO = getClockInVO(morningRecord);
        morningClockInVO.setRemindTime(user.getMorningTime());

        // 早睡
        recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.GOOD_NIGHT_CLOCK_IN.getValue())
                .eq(Record::getUid, uid);
        List<Record> nightRecord = recordService.list(recordQueryWrapper);
        ClockInVO nightClockInVO = getClockInVO(nightRecord);
        nightClockInVO.setRemindTime(user.getNightTime());

        // 运动
        recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.SPORT_CLOCK_IN.getValue())
                .eq(Record::getUid, uid);
        List<Record> sportRecord = recordService.list(recordQueryWrapper);
        ClockInVO sportClockInVO = getClockInVO(sportRecord);
        sportClockInVO.setRemindTime(user.getSportTime());

        clockInVOList.add(morningClockInVO);
        clockInVOList.add(nightClockInVO);
        clockInVOList.add(sportClockInVO);
        logger.info("[op_rslt:success, clockInVOList:{}]", JSON.toJSON(clockInVOList));
        return clockInVOList;
    }

    @Override
    public String clockIn(String token, int type) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:clockIn, uid:{}, token:{}, type:{}]", uid, token, type);

        if(!RecordType.isClock(type)) {
            throw new ParamErrorException("打卡类型有误");
        }
        // 检查是否已经打过卡
        long startTime = TimeUtils.getTodayTime().get(0);
        long endTime = TimeUtils.getTodayTime().get(1);
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.of(type).getValue())
                .eq(Record::getUid, uid).between(Record::getCreateTime, startTime, endTime);
        Record record = recordService.getOne(recordQueryWrapper);
        if(null != record) {
            throw new ParamErrorException("您已经打卡，请勿重复打卡");
        }
        Record clockRecord = new Record();
        clockRecord.setUid(uid);
        clockRecord.setType(type);
        clockRecord.setCreateTime(System.currentTimeMillis());
        clockRecord.setModifiedTime(System.currentTimeMillis());
        recordService.save(clockRecord);
        return "打卡成功";
    }

    private ClockInVO getClockInVO(List<Record> recordList) {
        ClockInVO clockInVO = new ClockInVO();
        clockInVO.setAccumulative(recordList.size());
        List<Long> todayTime = TimeUtils.getTodayTime();
        clockInVO.setHasClock(false);
        if(recordList.size() > 0) {
            QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
            recordQueryWrapper.lambda().eq(Record::getUid, recordList.get(0).getUid()).eq(Record::getType,
                    recordList.get(0).getType()).between(Record::getCreateTime, todayTime.get(0), todayTime.get(1));
            clockInVO.setHasClock(recordService.list(recordQueryWrapper).size() > 0);
        }
        List<Map<String, String>> clockRecordList = new ArrayList<>();
        recordList.forEach(record -> {
            Map<String, String> map = new HashMap<>();
            map.put("date", TimeUtils.formatTimeDate(record.getCreateTime()));
            map.put("background", "#1ECBBE");
            map.put("text", TimeUtils.formatTimeDay(record.getCreateTime()).substring(0, 5));
            map.put("color", "white");
            clockRecordList.add(map);
        });
        clockInVO.setRecord(clockRecordList);
        return clockInVO;
    }
}
