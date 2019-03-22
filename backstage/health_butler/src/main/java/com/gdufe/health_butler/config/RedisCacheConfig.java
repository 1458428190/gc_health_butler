package com.gdufe.health_butler.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean("keyGenerator")
    public KeyGenerator keyGenerator(){
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName() + ".");
            sb.append(method.getName() + ".");
            for (Object obj : params) {
                sb.append(obj.toString() + "&");
            }
            return sb.toString();
        };
    }
}
