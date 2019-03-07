package com.gdufe.health_butler.common.exception;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/3/2 11:37
 */
public class TokenErrorException extends RuntimeException {
    private static final long serialVersionUID = 2964354866320973899L;

    public TokenErrorException() {
    }

    public TokenErrorException(String message) {
        super(message);
    }

    public TokenErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenErrorException(Throwable cause) {
        super(cause);
    }

    public TokenErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
