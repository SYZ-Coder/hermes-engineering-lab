package com.example.hermesintegrationmodes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient 配置。
 *
 * 这里保留一个基础 WebClient.Builder，方便后续如果团队想把
 * API Server / Webhook 调用改造成响应式写法时直接复用。
 */
@Configuration
public class WebClientConfig {

    /**
     * 提供一个基础 WebClient.Builder。
     *
     * @return 默认的 WebClient.Builder
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}