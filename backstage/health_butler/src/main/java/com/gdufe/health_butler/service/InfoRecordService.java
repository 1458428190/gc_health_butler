package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.InfoRecordVO;
import com.gdufe.health_butler.entity.InfoRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户消息记录表 服务类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-26
 */
public interface InfoRecordService extends IService<InfoRecord> {
    /**
     * 获取未读消息数量
     * @param token
     * @return
     */
    int getUnReadInfoCount(String token);

    /**
     * 获取未读消息
     * @param token
     * @return
     */
    List<InfoRecordVO> getUnReadInfo(String token);

    /**
     * 获取已读消息
     * @param token
     * @return
     */
    List<InfoRecordVO> getReadInfo(String token);

}
