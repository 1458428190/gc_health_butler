package com.gdufe.health_butler.bean.dto.wx;

import lombok.Data;

/**
 * @Author: laichengfeng
 * @Description: 微信openid相关
 * @Date: 2019/2/26 15:04
 */
@Data
public class Code2Session {

    private String openId;

    private String sessionKey;

    /**
     * 有效期
     */
    private int expiresIn;

    /**
     * 获取时间
     */
    private long getTime;
}
