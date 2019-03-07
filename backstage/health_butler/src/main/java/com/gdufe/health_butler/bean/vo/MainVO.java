package com.gdufe.health_butler.bean.vo;

import com.gdufe.health_butler.entity.User;
import lombok.Data;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/2/28 15:46
 */
@Data
public class MainVO {

    /**
     * 用户信息
     */
    private User user;

    /**
     * 步数排名
     */
    private int rankStep;

    /**
     * 健康币排名
     */
    private int rankCoin;

    /**
     * 早起排名
     */
    private int rankMorning;

    /**
     * 昨日早睡排名
     */
    private int rankNight;

    /**
     * 礼品兑换个数
     */
    private int recordCount;

}
