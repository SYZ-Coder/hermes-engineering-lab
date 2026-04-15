package com.example.hermesclient.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 WebClient 的 Hermes 调用封装。
 *
 * <p>这个类和 HermesApiService 做的是同一件事，只是换成了 WebClient 实现。
 * 它更适合团队后续扩展流式响应、统一过滤器、链式重试等现代 HTTP 调用能力。</p>
 */
@Service
public class HermesWebClientService {

    /**
     * Spring 提供的响应式 HTTP 客户端。
     */
    private final WebClient webClient;

    /**
     * Hermes API Server 的基础地址，例如 http://127.0.0.1:8642/v1。
     */
    private final String baseUrl;

    /**
     * 请求 Hermes 时使用的模型名。
     */
    private final String model;

    /**
     * 构造器注入配置和客户端。
     *
     * @param webClient WebClient Bean
     * @param baseUrl Hermes API 基础地址
     * @param model 请求模型名
     */
    public HermesWebClientService(WebClient webClient,
                                  @Value("${hermes.base-url}") String baseUrl,
                                  @Value("${hermes.model}") String model) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    /**
     * 用 WebClient 调用 Hermes Chat Completions。
     *
     * @param prompt 用户提示词
     * @return Hermes 返回的原始响应结构
     */
    public Map<String, Object> chat(String prompt) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("model", model);
        payload.put("messages", List.of(
            Map.of("role", "user", "content", prompt)
        ));

        return webClient.post()
            .uri(baseUrl + "/chat/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(Map.class)
            // 示例工程里直接 block，目的是保持调用链路清晰直观。
            .block();
    }

    /**
     * 用 WebClient 调用 Hermes webhook。
     *
     * @param route webhook 路由名
     * @param event 事件类型
     * @param message 事件说明
     * @return webhook 响应文本
     */
    public String sendWebhook(String route, String event, String message) {
        String webhookBase = baseUrl.replace("/v1", "");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event", event);
        payload.put("message", message);

        return webClient.post()
            .uri(webhookBase + "/webhooks/" + route)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-Hermes-Event", event)
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}