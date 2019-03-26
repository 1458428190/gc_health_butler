package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 消息类型
 * @Date: 2019/3/25 20:14
 */
public enum InfoRecordType {
    /**
     * 打赏
     */
    REWARD(1),

    /**
     * 健康小圈评论以及回复
     */
    COMMUNITY(2),

    /**
     * 点赞
     */
    PRAISE(3),

    /**
     * 健康币奖励
     */
    COIN_REWARD(4);

    int value;

    InfoRecordType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, InfoRecordType> map = new HashMap<>();

    static {
        for(InfoRecordType infoRecordType: values()) {
            map.put(infoRecordType.getValue(), infoRecordType);
        }
    }

    public static InfoRecordType of(int value){
        return map.get(value);
    }
}
