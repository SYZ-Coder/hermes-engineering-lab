package com.example.hermesintegrationmodes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Server 模式服务。
 *
 * 这个服务专门封装“Spring Boot -> Hermes API Server”这条链路，
 * 让团队更容易看清楚同步问答/同步任务请求的最小实现方式。
 */
@Service
public class HermesApiServerService {

    private final RestTemplate restTemplate;
    private final String apiBaseUrl;

    public HermesApiServerService(
            RestTemplate restTemplate,
            @Value("${hermes.api-base-url:http://127.0.0.1:8642/v1}") String apiBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * 调用 Hermes 的 chat/completions 接口。
     *
     * @param prompt    业务侧要交给 Hermes 的提示词
     * @param sessionId 可选会话标识，用于保持多轮连续性
     * @return Hermes 原始响应内容
     */
    public String chat(String prompt, String sessionId) {
        String url = apiBaseUrl + "/chat/completions";

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "hermes");
        payload.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionId != null && !sessionId.isBlank()) {
            headers.set("X-Hermes-Session-Id", sessionId);
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        return restTemplate.postForObject(url, request, String.class);
    }
}