package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 记录类型
 * @Date: 2019/2/26 23:30
 */
public enum RecordType {

    /**
     * 步数
     */
    STEP(1),

    /**
     * 早起打卡
     */
    MORNING_CLOCK_IN(2),

    /**
     * 早睡打卡
     */
    GOOD_NIGHT_CLOCK_IN(3),

    /**
     * 运动打卡
     */
    SPORT_CLOCK_IN(4),

    /**
     * 任务
     */
    TASK(5),

    /**
     * 兑换
     */
    CONVERSION(6);

    int value;

    private static Map<Integer, RecordType> map = new HashMap<>(6);

    static {
        for(RecordType recordType: values()) {
            map.put(recordType.getValue(), recordType);
        }
    }

    RecordType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static RecordType of(int value) {
        return map.get(value);
    }

    public static boolean isClock(int value) {
        return value >= 2 && value <= 4;
    }
}
