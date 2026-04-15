package com.example.hermesclient.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 转发到 Hermes webhook 时使用的请求对象。
 *
 * <p>这个对象模拟业务系统把一个事件推送给 Hermes 的典型输入结构：</p>
 * <ul>
 *     <li>route：Hermes webhook 路由名</li>
 *     <li>event：事件类型</li>
 *     <li>message：事件说明文本</li>
 * </ul>
 */
public class WebhookEventRequest {

    /**
     * Hermes webhook 的路由名，例如 business-alert。
     */
    @NotBlank
    private String route;

    /**
     * 当前事件类型，会同时进入请求头和请求体。
     */
    @NotBlank
    private String event;

    /**
     * 发送给 Hermes 的事件描述。
     */
    @NotBlank
    private String message;

    /**
     * @return webhook 路由名
     */
    public String getRoute() {
        return route;
    }

    /**
     * @param route webhook 路由名
     */
    public void setRoute(String route) {
        this.route = route;
    }

    /**
     * @return 事件名
     */
    public String getEvent() {
        return event;
    }

    /**
     * @param event 事件名
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * @return 事件文本说明
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message 事件文本说明
     */
    public void setMessage(String message) {
        this.message = message;
    }
}