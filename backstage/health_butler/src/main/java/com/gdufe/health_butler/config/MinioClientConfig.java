package com.gdufe.health_butler.config;

import com.gdufe.health_butler.common.exception.SystemErrorException;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: laichengfeng
 * @Description: Minio配置
 * @Date: 2019/3/5 10:08
 */
@Configuration
public class MinioClientConfig {

    /**
     * 对象存储服务的URL
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * Access key就像用户ID，可以唯一标识你的账户。
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * Secret key是你账户的密码。
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient () {
        try {
            return new MinioClient(endpoint, accessKey, secretKey);
        } catch (Exception e) {
            throw new SystemErrorException("minio配置有误，或minio无法提供服务");
        }
    }
}
