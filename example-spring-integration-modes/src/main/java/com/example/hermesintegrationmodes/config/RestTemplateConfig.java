package com.example.hermesintegrationmodes.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * RestTemplate 配置。
 *
 * 这个 Bean 主要给“Spring Boot -> Hermes”的同步 HTTP 调用使用，
 * 例如调用 Hermes API Server 或 Hermes Webhook。
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建带基础超时设置的 RestTemplate。
     *
     * @param builder Spring 提供的构建器
     * @return 可复用的 RestTemplate 实例
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }
}