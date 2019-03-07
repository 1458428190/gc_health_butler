package com.gdufe.health_butler.common.enums;

/**
 * @Author: laichengfeng
 * @Description: 返回结果状态枚举
 * @Date: 2019/2/26 15:04
 */
public enum ResponseStatusEnum {

    SUCCESS(200, "成功"),

    LIMITED_IP(-1, "IP受限"),

    NETWORK_ERFROR(-2, "网络出错了..."),

    SYSTEM_ERROR(-3, "系统出错了..."),

    PARAM_ERROR(-4, "参数不正确"),

    TOKEN_ERROR(-5, "TOKEN失效或有误"),

    NO_AUTHORIZATION(-101, "用户未授权");

    private int code;

    private String msg;

    ResponseStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
