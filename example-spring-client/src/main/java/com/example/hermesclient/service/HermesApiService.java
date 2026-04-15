package com.example.hermesclient.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 RestTemplate 的 Hermes 调用封装。
 *
 * <p>这个类代表最传统、最容易迁移到现有 Spring Boot 项目里的接法。
 * 如果团队当前系统已经大量使用 RestTemplate，那么直接参考这个类就可以快速落地。</p>
 */
@Service
public class HermesApiService {

    /**
     * Spring 提供的同步 HTTP 客户端。
     */
    private final RestTemplate restTemplate;

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
     * @param restTemplate RestTemplate Bean
     * @param baseUrl Hermes API 基础地址
     * @param model 请求模型名
     */
    public HermesApiService(RestTemplate restTemplate,
                            @Value("${hermes.base-url}") String baseUrl,
                            @Value("${hermes.model}") String model) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    /**
     * 调用 Hermes 的 Chat Completions 接口。
     *
     * @param prompt 用户输入的提示词
     * @return Hermes 返回的原始响应 JSON 结构
     */
    public Map<String, Object> chat(String prompt) {
        String url = baseUrl + "/chat/completions";

        // 这里按 OpenAI 兼容格式构造最小请求体。
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("model", model);
        payload.put("messages", List.of(
            Map.of("role", "user", "content", prompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return response.getBody();
    }

    /**
     * 调用 Hermes 的 webhook 路由。
     *
     * @param route webhook 路由名
     * @param event 事件类型
     * @param message 事件说明
     * @return Hermes webhook 的响应文本
     */
    public String sendWebhook(String route, String event, String message) {
        // webhook 地址不走 /v1，所以这里把 API 前缀去掉。
        String url = baseUrl.replace("/v1", "") + "/webhooks/" + route;

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event", event);
        payload.put("message", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Hermes webhook 常见做法是把事件类型写进头里，便于路由和审计。
        headers.set("X-Hermes-Event", event);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }
}