package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 任务分类2
 * @Date: 2019/2/28 19:35
 */
public enum TaskCategory2Enum {
    /**
     * 授权个人信息
     */
    AUTHORIZE_INFO(1),

    /**
     * 授权步数
     */
    AUTHORIZE_RUN_DATA(2),

    /**
     * 授权地理位置
     */
    AUTHORIZE_ADDRESS(3),

    /**;
     * 邀请新用户
     */
    INVITE(4),

    /**
     * 分享到群
     */
    SHARE_CROWD(5),

    /**
     * 打卡
     */
    CLOCK(6),

    /**
     * 挑战步数
     */
    CHALLENGE_STEP(7),

    /**
     * 分享动态
     */
    SHARE_DYNAMIC(8),

    /**
     * 排行榜
     */
    RANK(9)
    ;

    private int value;

    public int getValue() {
        return value;
    }

    TaskCategory2Enum(int value) {
        this.value = value;
    }

    private static Map<Integer, TaskCategory2Enum> map = new HashMap<>();

    static {
        for(TaskCategory2Enum taskCategory2Enum: values()) {
            map.put(taskCategory2Enum.getValue(), taskCategory2Enum);
        }
    }

    public static TaskCategory2Enum of(int value) {
        return map.get(value);
    }
}
