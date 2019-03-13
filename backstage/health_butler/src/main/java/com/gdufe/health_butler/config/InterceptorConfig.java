package com.gdufe.health_butler.config;

import com.gdufe.health_butler.aop.AuthTokenAOPInterceptor;
import com.gdufe.health_butler.aop.IpInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/3/2 10:17
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authTokenAOPInterceptor())
                .addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @AuthToken 注解 决定是否需要验证token
        registry.addInterceptor(ipInterceptor())  //后台管理接口IP拦截
                .addPathPatterns("/manager/*");
    }

    @Bean
    public AuthTokenAOPInterceptor authTokenAOPInterceptor() {
        return new AuthTokenAOPInterceptor();
    }

    @Bean
    public IpInterceptor ipInterceptor() {
        return new IpInterceptor();
    }
}
