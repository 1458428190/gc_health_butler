package com.gdufe.health_butler.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.common.util.StringFormatUtils;
import com.gdufe.health_butler.entity.BmiRecord;
import com.gdufe.health_butler.dao.BmiRecordMapper;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.BmiRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdufe.health_butler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  BMI记录 服务实现类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-04
 */
@Service
@Transactional
public class BmiRecordServiceImpl extends ServiceImpl<BmiRecordMapper, BmiRecord> implements BmiRecordService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Override
    public void saveRecord(String token, double height, double weight) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:saveRecord, uid:{}, token:{}, height:{}, weight:{}]", uid, token, height, weight);
        BmiRecord bmiRecord = new BmiRecord();
        bmiRecord.setUid(uid);
        bmiRecord.setCreateTime(System.currentTimeMillis());
        bmiRecord.setHeight(height);
        bmiRecord.setWeight(weight);
        double bmi = Double.parseDouble(StringFormatUtils.formatRound(weight / (height*height/10000), 1));
        bmiRecord.setBmi(bmi);
        bmiRecord.setModifiedTime(System.currentTimeMillis());
        save(bmiRecord);
    }

    @Override
    public List<BmiRecord> listByToken(String token) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:saveRecord, uid:{}, token:{}]", uid, token);
        QueryWrapper<BmiRecord> bmiRecordQueryWrapper = new QueryWrapper<>();
        bmiRecordQueryWrapper.lambda().eq(BmiRecord::getUid, uid);
        List<BmiRecord> list = list(bmiRecordQueryWrapper);
        logger.info("[op_rslt: success, bmiRecordlist:{}]", JSON.toJSON(list));
        return list;
    }
}
