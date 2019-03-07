package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/2/22 23:31
 */
public enum FoodSourceType {

    /**
     * 160趣健康
     */
    HEAHTH_160(1, "160趣健康");

    int key;

    String value;

    FoodSourceType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

    private static final Map<Integer, FoodSourceType> map = new HashMap<>();

    static {
        for(FoodSourceType type: values()) {
            map.put(type.key, type);
        }
    }

    public static FoodSourceType of(int key) {
        return map.get(key);
    }

}
