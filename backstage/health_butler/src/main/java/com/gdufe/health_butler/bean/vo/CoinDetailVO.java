package com.gdufe.health_butler.bean.vo;

import com.gdufe.health_butler.entity.CoinDetail;
import lombok.Data;

import java.util.List;

/**
 * @Author: laichengfeng
 * @Description: 健康币明细 视图VO
 * @Date: 2019/3/1 14:54
 */
@Data
public class CoinDetailVO {
    /**
     * 现有健康币
     */
    private long healthCoin;

    /**
     * 累计收入
     */
    private long sumIncome;

    /**
     * 累计支出
     */
    private long sumExpend;

    /**
     * 收入明细
     */
    private List<CoinDetail> incomeDetail;

    /**
     * 支出明细
     */
    private List<CoinDetail> expendDetail;

}
