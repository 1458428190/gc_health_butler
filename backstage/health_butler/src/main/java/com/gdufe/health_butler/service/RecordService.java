package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.ConversionRecordVO;
import com.gdufe.health_butler.bean.vo.StepVO;
import com.gdufe.health_butler.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户记录表 服务类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
public interface RecordService extends IService<Record> {
    /**
     * 保存或更新步数数据 并返回当天的步数
     * @param token
     *          标记用户
     * @param iv
     * @param encryptedData
     */
    void saveOrUpdateRunData(String token, String iv, String encryptedData);

    /**
     *获取兑换记录
     * @param token
     * @return
     */
    ConversionRecordVO getConversionRecord(String token);

    /**
     * 获取步数数据
     * @param token
     * @param toUid
     * @return
     */
    List<StepVO> getRunData(String token, String toUid);
}
