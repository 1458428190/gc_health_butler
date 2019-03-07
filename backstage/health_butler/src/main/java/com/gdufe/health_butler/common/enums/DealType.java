package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 交易类型
 * @Date: 2019/3/1 14:33
 */
public enum DealType {
    /**
     * 打赏
     */
    REWARD(1),

    /**
     * 做任务
     */
    DO_TASK(2),

    /**
     * 兑换
     */
    CONVERSION(3),

    /**
     * 系统赠送
     */
    SYSTEM_PRESENT(4)
    ;

    int value;

    DealType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, DealType> map = new HashMap<>();

    static {
        for(DealType dealType: values()) {
            map.put(dealType.getValue(), dealType);
        }
    }

    public static DealType of(int value){
        return map.get(value);
    }
}
