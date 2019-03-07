package com.gdufe.health_butler.common.exception;

/**
 * @Author: laichengfeng
 * @Description: 参数错误异常
 * @Date: 2019/2/26 20:14
 */
public class ParamErrorException extends RuntimeException {

    private static final long serialVersionUID = 3920775829328092446L;

    public ParamErrorException() {
    }

    public ParamErrorException(String message) {
        super(message);
    }

    public ParamErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamErrorException(Throwable cause) {
        super(cause);
    }

    public ParamErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
