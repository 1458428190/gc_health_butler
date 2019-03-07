package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.CoinDetailVO;
import com.gdufe.health_butler.entity.CoinDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 健康币交易明细表 服务类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
public interface CoinDetailService extends IService<CoinDetail> {

    /**
     * 获取健康币明细
     * @param token
     * @return
     */
    CoinDetailVO getDetail(String token);
}
