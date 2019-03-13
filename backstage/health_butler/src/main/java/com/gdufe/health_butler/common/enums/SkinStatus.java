package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 皮肤状态
 * @Date: 2019/3/10 12:59
 */
public enum SkinStatus {

    HEALTH(1, "健康"),

    STAIN(2, "色斑"),

    ACNE(3, "青春痘"),

    DARK_CIRCLE(4, "黑眼圈");

    int key;

    String value;

    SkinStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

    private static final Map<Integer, SkinStatus> map = new HashMap<>();

    static {
        for(SkinStatus type: values()) {
            map.put(type.key, type);
        }
    }

    public static SkinStatus of(int key) {
        return map.get(key);
    }
}
