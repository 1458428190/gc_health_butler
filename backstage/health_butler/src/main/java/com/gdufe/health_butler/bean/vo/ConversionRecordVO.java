package com.gdufe.health_butler.bean.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: laichengfeng
 * @Description: 兑换记录
 * @Date: 2019/3/1 13:29
 */
@Data
public class ConversionRecordVO {

    /**
     * 兑换记录
     */
    private List<ConversionGoodsVO> recordList;

    /**
     * 总花费
     */
    private long sumCoin;

}
