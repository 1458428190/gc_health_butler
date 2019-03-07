package com.gdufe.health_butler.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdufe.health_butler.bean.vo.ConversionGoodsVO;
import com.gdufe.health_butler.bean.vo.ConversionRecordVO;
import com.gdufe.health_butler.bean.vo.StepVO;
import com.gdufe.health_butler.bean.vo.TaskVO;
import com.gdufe.health_butler.common.enums.RecordType;
import com.gdufe.health_butler.common.util.TimeUtils;
import com.gdufe.health_butler.common.util.WxBizDataCryptUtils;
import com.gdufe.health_butler.entity.Goods;
import com.gdufe.health_butler.entity.Record;
import com.gdufe.health_butler.dao.RecordMapper;
import com.gdufe.health_butler.entity.Task;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.GoodsService;
import com.gdufe.health_butler.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdufe.health_butler.service.TaskService;
import com.gdufe.health_butler.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 用户记录表 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
@Transactional
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Override
    public void saveOrUpdateRunData(String token, String iv, String encryptedData) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:saveOrUpdateRunData, uid: {}, token:{}, iv:{}, encryptedData:{}]",
                uid, token, iv, encryptedData);
        try {
            String sessionKey = TokenContainer.get(token).getSessionKey();
            String runData = WxBizDataCryptUtils.decrypt(sessionKey, iv, encryptedData);
            logger.info("[runData: {}]", runData);
            Map<String, Object> runDataMap = (Map<String, Object>) JSON.parse(runData);
            List<Map<String, Object>> stepInfoList = (List<Map<String, Object>>) runDataMap.get("stepInfoList");
            // 当天的另外处理
            Map<String, Object> nowStep = stepInfoList.get(stepInfoList.size() - 1);
            stepInfoList.remove(stepInfoList.size() - 1);
            // 查出最近的一次步数记录, 根据此步数的记录时间添加
            QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
            recordQueryWrapper.lambda().eq(Record::getUid, uid)
                    .eq(Record::getType, RecordType.STEP.getValue()).orderByDesc(Record::getCreateTime);
            Page<Record> recordPage = new Page<>(1, 1);
            List<Record> records = page(recordPage, recordQueryWrapper).getRecords();
            long newCreateTime = 0L;
            if (records.size() > 0) {
                newCreateTime = records.get(0).getCreateTime();
            }
            long finalNewCreateTime = newCreateTime;
            List<Record> list = new ArrayList<>();

            // 累计用户的总步数
            stepInfoList.forEach(map -> {
                int timestamp = (int) map.get("timestamp");
                int step = (int) map.get("step");
                if ((long)timestamp * 1000 > finalNewCreateTime) {
                    Record record = new Record();
                    record.setUid(user.getId());
                    record.setType(RecordType.STEP.getValue());
                    record.setExtra(step + "");
                    // 以该天的0点为记录点
                    record.setCreateTime((long) timestamp * 1000);
                    record.setModifiedTime(System.currentTimeMillis());
                    list.add(record);
                    user.setSumStep(user.getSumStep() + step);
                }
            });
            // 批量保存
            saveBatch(list);
            // 更新总步数 和 当日步数
            user.setNowStep((int) nowStep.get("step"));
            userService.updateById(user);
            logger.info("[op_rslt:success]");
        } catch (Exception e) {
            logger.error("[op_rslt:error]", e);
        }
    }

    /**
     * 获取兑换记录
     * @param token
     * @return
     */
    @Override
    public ConversionRecordVO getConversionRecord(String token) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        logger.info("[op:getConversionRecord, uid: {}, token:{}]", user.getId(), token);
        ConversionRecordVO conversionRecordVO = new ConversionRecordVO();
        List<ConversionGoodsVO> conversionGoodsVOList = new ArrayList<>();
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.CONVERSION.getValue()).eq(Record::getUid, user.getId());
        List<Record> recordList = list(recordQueryWrapper);
        AtomicLong sumCoin = new AtomicLong();
        recordList.forEach(record -> {
            // 商品id
            long gid = Long.parseLong(record.getExtra());
            Goods goods = goodsService.getById(gid);
            ConversionGoodsVO conversionGoodsVO = new ConversionGoodsVO();
            conversionGoodsVO.setCoin(goods.getPrice());
            conversionGoodsVO.setGoodsName(goods.getName());
            conversionGoodsVO.setImgUrl(goods.getImgUrl());
            conversionGoodsVO.setTime(record.getCreateTime());
            sumCoin.addAndGet(goods.getPrice());
            conversionGoodsVOList.add(conversionGoodsVO);
        });
        conversionRecordVO.setRecordList(conversionGoodsVOList);
        conversionRecordVO.setSumCoin(sumCoin.get());
        return conversionRecordVO;
    }

    @Override
    public List<StepVO> getRunData(String token, String toUid) {
        String openId = TokenContainer.get(token).getOpenId();
        User user = userService.getByOpenId(openId);
        long uid = user.getId();
        logger.info("[op:getConversionRecord, uid: {}, token:{}]", uid, token);
        List<StepVO> runData = new ArrayList<>();
        // 可以访问他人的步数数据(兼容)
        if(null != toUid && !StringUtils.equals(toUid, "null")) {
            uid = Long.parseLong(toUid);
        }
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getUid, uid).eq(Record::getType, RecordType.STEP.getValue());
        List<Record> recordList = list(recordQueryWrapper);
        recordList.forEach(record -> {
            StepVO stepVO = new StepVO();
            stepVO.setDate(TimeUtils.formatTimeDate(record.getCreateTime()));
            stepVO.setSteps(Integer.parseInt(record.getExtra()));
            if(StringUtils.equals(stepVO.getDate().split("-")[2], "01")) {
                stepVO.setFirst(true);
            } else {
                stepVO.setFirst(false);
            }
            runData.add(stepVO);
        });
        return runData;
    }
}
