package com.gdufe.health_butler.common.exception;

/**
 * @Author: laichengfeng
 * @Description: 系统错误异常
 * @Date: 2019/2/26 20:19
 */
public class SystemErrorException extends RuntimeException {
    private static final long serialVersionUID = 2235225909963044866L;

    public SystemErrorException() {
    }

    public SystemErrorException(String message) {
        super(message);
    }

    public SystemErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemErrorException(Throwable cause) {
        super(cause);
    }

    public SystemErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
