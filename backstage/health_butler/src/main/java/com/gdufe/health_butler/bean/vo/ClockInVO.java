package com.gdufe.health_butler.bean.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 打卡
 * @Date: 2019/3/3 21:04
 */
@Data
public class ClockInVO {

    /**
     * 提醒时间
     */
    private String remindTime;

    /**
     * 是否已经打卡
     */
    private boolean hasClock;

    /**
     * 累计打卡
     */
    private int accumulative;

    /**
     * 打卡记录
     */
    private List<Map<String, String>> record;
}
