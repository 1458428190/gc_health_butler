package com.gdufe.health_butler.aop;

import com.gdufe.health_butler.bean.dto.wx.Code2Session;
import com.gdufe.health_butler.common.exception.TokenErrorException;
import com.gdufe.health_butler.common.util.ThreadPoolUtils;
import com.gdufe.health_butler.manager.TokenContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/3/2 9:37
 */
public class AuthTokenAOPInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String authFieldName = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getParameter(authFieldName);// 取出 token
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        //检查是否有authToken注释，有则认证
        if (method.isAnnotationPresent(AuthToken.class)) {
            Code2Session code2Session = TokenContainer.get(token);
            if (null == code2Session) {
                logger.info("[token failure, token: {}]", token);
                throw new TokenErrorException("token无效");
            }
            if(code2Session.getGetTime() + code2Session.getExpiresIn() <= System.currentTimeMillis()) {
                logger.info("[token overdue, token: {}]", token);
                throw new TokenErrorException("token已过期");
            }
        }
        return true;
    }
}
