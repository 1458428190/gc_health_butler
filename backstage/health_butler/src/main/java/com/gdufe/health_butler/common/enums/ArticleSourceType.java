package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 文章来源
 * @Date: 2019/2/21 17:20
 */
public enum ArticleSourceType {

    /**
     * 香山健康小程序
     */
    XIANG_SHAN(1, "香山健康");

    int key;

    String value;

    ArticleSourceType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

    private static final Map<Integer, ArticleSourceType> map = new HashMap<>();

    static {
        for(ArticleSourceType type: values()) {
            map.put(type.key, type);
        }
    }

    public static ArticleSourceType of(int key) {
        return map.get(key);
    }
}
