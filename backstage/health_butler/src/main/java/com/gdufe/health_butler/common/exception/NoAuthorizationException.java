package com.gdufe.health_butler.common.exception;

import com.gdufe.health_butler.common.enums.ResponseStatusEnum;

/**
 * @Author: laichengfeng
 * @Description: 未授权异常
 * @Date: 2019/2/26 20:16
 */
public class NoAuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 5403747378134027937L;

    public NoAuthorizationException() {
    }

    public NoAuthorizationException(String message) {
        super(message);
    }

    public NoAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAuthorizationException(Throwable cause) {
        super(cause);
    }

    public NoAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
