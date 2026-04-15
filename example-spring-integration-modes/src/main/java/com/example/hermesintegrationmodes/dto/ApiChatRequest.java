package com.example.hermesintegrationmodes.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * API Server 模式的入参。
 *
 * 这个对象描述“Spring Boot 调 Hermes API Server”时最小需要的业务输入。
 */
public class ApiChatRequest {

    /**
     * 用户想让 Hermes 处理的提示词。
     */
    @NotBlank(message = "prompt 不能为空")
    private String prompt;

    /**
     * 可选会话标识。
     *
     * 如果传入，Spring Boot 可以把多次请求绑定到同一个 Hermes 会话。
     */
    private String sessionId;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}