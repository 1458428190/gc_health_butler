package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.entity.BmiRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-04
 */
public interface BmiRecordService extends IService<BmiRecord> {

    /**
     * 保存bmi记录
     * @param token
     * @param height
     * @param weight
     * @return
     */
    void saveRecord(String token, double height, double weight);

    /**
     * 获取bmi历史计算记录
     * @param token
     * @return
     */
    List<BmiRecord> listByToken(String token);
}
