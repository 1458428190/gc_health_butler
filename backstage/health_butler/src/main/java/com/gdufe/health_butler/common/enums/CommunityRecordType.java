package com.gdufe.health_butler.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 社区操作记录类型
 * @Date: 2019/3/4 17:53
 */
public enum CommunityRecordType {

    /**
     * 点赞
     */
    PRAISE(1),

    /**
     * 评论
     */
    COMMENT(2),

    /**
     * 打赏
     */
    REWARD(3)
    ;

    int value;

    CommunityRecordType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, CommunityRecordType> map = new HashMap<>();

    static {
        for(CommunityRecordType communityRecordType: values()) {
            map.put(communityRecordType.getValue(), communityRecordType);
        }
    }

    public static CommunityRecordType of(int value){
        return map.get(value);
    }
}
