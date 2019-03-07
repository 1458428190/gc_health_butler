package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 任务分类1
 * @Date: 2019/2/28 19:29
 */
public enum TaskCategory1Enum {

    /**
     * 每日任务
     */
    EVERY_DAY(1),

    /**
     * 长期任务
     */
    LONG_TERM(2),

    /**
     * 新手任务
     */
    NOVICE(3);

    private int value;

    private static Map<Integer, TaskCategory1Enum> map = new HashMap<>();

    static {
        for(TaskCategory1Enum taskCategory1Enum: values()) {
            map.put(taskCategory1Enum.value, taskCategory1Enum);
        }
    }

    TaskCategory1Enum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskCategory1Enum of(int value) {
        return map.get(value);
    }
}
