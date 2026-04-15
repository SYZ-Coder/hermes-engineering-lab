package com.example.hermesclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient 配置类。
 *
 * <p>这个 Bean 用来演示更现代的 HTTP 调用方式。
 * 在后续需要扩展流式调用、链式处理或更复杂的请求管道时，
 * WebClient 会比 RestTemplate 更方便。</p>
 */
@Configuration
public class WebClientConfig {

    /**
     * 创建 WebClient Bean。
     *
     * @param builder Spring 提供的 WebClient 构建器
     * @return 默认 WebClient 实例
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}