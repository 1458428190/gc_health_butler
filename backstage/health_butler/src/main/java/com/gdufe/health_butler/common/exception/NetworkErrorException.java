package com.gdufe.health_butler.common.exception;

/**
 * @Author: laichengfeng
 * @Description: 网络异常
 * @Date: 2019/2/26 20:18
 */
public class NetworkErrorException extends RuntimeException {
    private static final long serialVersionUID = 4645606813293824485L;

    public NetworkErrorException() {
    }

    public NetworkErrorException(String message) {
        super(message);
    }

    public NetworkErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkErrorException(Throwable cause) {
        super(cause);
    }

    public NetworkErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
