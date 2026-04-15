package com.example.hermesintegrationmodes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Webhook 模式服务。
 *
 * 这个服务演示“Spring Boot 把业务事件推给 Hermes”的方式。
 * 适合告警、审批、工单、订单异常等事件驱动场景。
 */
@Service
public class HermesWebhookService {

    private final RestTemplate restTemplate;
    private final String webhookBaseUrl;

    public HermesWebhookService(
            RestTemplate restTemplate,
            @Value("${hermes.webhook-base-url:http://127.0.0.1:8644}") String webhookBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.webhookBaseUrl = webhookBaseUrl;
    }

    /**
     * 把业务事件转发到 Hermes 的 webhook route。
     *
     * @param routeName route 名称，对应 Hermes 里配置好的 webhook 入口
     * @param eventType 事件类型，会进入请求头供 Hermes 过滤
     * @param payload   业务事件主体
     * @return Hermes webhook 处理后的原始响应
     */
    public String forwardEvent(String routeName, String eventType, Map<String, Object> payload) {
        String url = webhookBaseUrl + "/webhooks/" + routeName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Hermes-Event", eventType);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        return restTemplate.postForObject(url, request, String.class);
    }
}