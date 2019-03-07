package com.gdufe.health_butler.controller;

import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.common.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: laichengfeng
 * @Description: 全局异常拦截器
 * @Date: 2019/2/26 20:18
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = LimitedIpException.class)
    public ResponseVO limitedIpException(Exception e) {
        logger.error("[op_rslt: error, limitedIp .]", e);
        return new ResponseVO(ResponseStatusEnum.LIMITED_IP, "", e.getMessage());
    }

    @ExceptionHandler(value = NetworkErrorException.class)
    public ResponseVO networkErrorException(Exception e) {
        logger.error("[op_rslt: error, network error.]", e);
        return new ResponseVO(ResponseStatusEnum.NETWORK_ERFROR, "", e.getMessage());
    }

    @ExceptionHandler(value = SystemErrorException.class)
    public ResponseVO systemErrorException(Exception e) {
        logger.error("[op_rslt: error, system error.]", e);
       return new ResponseVO(ResponseStatusEnum.SYSTEM_ERROR, "", e.getMessage());
    }

    @ExceptionHandler(value = ParamErrorException.class)
    public ResponseVO paramErrorException(Exception e) {
        logger.error("[op_rslt: error, param error.]", e);
        return new ResponseVO(ResponseStatusEnum.PARAM_ERROR, "", e.getMessage());
    }

    @ExceptionHandler(value = NoAuthorizationException.class)
    public ResponseVO noAuthorizationException(Exception e) {
        logger.error("[op_rslt: error, noAuthorization error.]", e);
        return new ResponseVO(ResponseStatusEnum.NO_AUTHORIZATION, "", e.getMessage());
    }

    @ExceptionHandler(value = TokenErrorException.class)
    public ResponseVO tokenErrorException(Exception e) {
        logger.error("[op_rslt: error, token error.]", e);
        return new ResponseVO(ResponseStatusEnum.TOKEN_ERROR, "", e.getMessage());
    }
}
