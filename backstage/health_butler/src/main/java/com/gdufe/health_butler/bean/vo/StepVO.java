package com.gdufe.health_butler.bean.vo;

import lombok.Data;

/**
 * @Author: laichengfeng
 * @Description: 步数VO
 * @Date: 2019/3/2 10:43
 */
@Data
public class StepVO {
    /**
     * 日期
     */
    private String date;

    /**
     * 步数
     */
    private int steps;

    /**
     * 是否为一个月的开始
     */
    private boolean first;
}
