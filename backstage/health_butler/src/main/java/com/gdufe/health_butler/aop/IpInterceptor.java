package com.gdufe.health_butler.aop;

import com.gdufe.health_butler.common.exception.LimitedIpException;
import com.gdufe.health_butler.common.exception.TokenErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: laichengfeng
 * @Description: IP拦截器
 * @Date: 2019/3/13 14:43
 */
public class IpInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${white.ip.list}")
    private String ipListString;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr();
        logger.info("[op:IpInterceptor, ip:{}]", ip);
        List<String> ipList = Arrays.asList(ipListString.split(","));
        if(ipList.contains(ip)) {
            return true;
        } else {
            logger.warn("[op_rslt: ip wrong]");
            throw new LimitedIpException("此ip无法访问");
        }
    }

}
