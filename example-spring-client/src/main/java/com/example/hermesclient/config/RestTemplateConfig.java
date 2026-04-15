package com.example.hermesclient.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * RestTemplate 配置类。
 *
 * <p>这里专门给示例里的同步 HTTP 调用准备一个 RestTemplate Bean，
 * 主要用于演示最传统、最容易理解的 Spring Boot -> Hermes 接法。</p>
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建 RestTemplate Bean。
     *
     * <p>连接超时和读取超时都在这里统一设置，
     * 方便后续把示例工程扩展成更正式的业务客户端。</p>
     *
     * @param builder Spring 提供的构建器
     * @return 带超时配置的 RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            // 建立连接最多等待 10 秒。
            .setConnectTimeout(Duration.ofSeconds(10))
            // Hermes 处理任务可能比普通接口慢，所以读取超时放宽到 120 秒。
            .setReadTimeout(Duration.ofSeconds(120))
            .build();
    }
}