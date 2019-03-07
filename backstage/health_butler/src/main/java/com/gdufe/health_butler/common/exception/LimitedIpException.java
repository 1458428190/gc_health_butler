package com.gdufe.health_butler.common.exception;

/**
 * @Author: laichengfeng
 * @Description: IP受限异常
 * @Date: 2019/2/26 20:18
 */
public class LimitedIpException extends RuntimeException {
    private static final long serialVersionUID = 2306002289894519812L;

    public LimitedIpException() {
    }

    public LimitedIpException(String message) {
        super(message);
    }

    public LimitedIpException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitedIpException(Throwable cause) {
        super(cause);
    }

    public LimitedIpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
