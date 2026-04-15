package com.example.hermesintegrationmodes.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.HashMap;
import java.util.Map;

/**
 * Webhook 模式的入参。
 *
 * 用来演示“Spring Boot 把业务事件推给 Hermes”的最小请求结构。
 */
public class WebhookForwardRequest {

    /**
     * Hermes 侧预先配置好的 webhook route 名称。
     */
    @NotBlank(message = "routeName 不能为空")
    private String routeName;

    /**
     * 事件类型。
     *
     * 这个字段会被放进请求头里，便于 Hermes 做事件过滤。
     */
    @NotBlank(message = "eventType 不能为空")
    private String eventType;

    /**
     * 业务事件的主体内容。
     */
    private Map<String, Object> payload = new HashMap<>();

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}