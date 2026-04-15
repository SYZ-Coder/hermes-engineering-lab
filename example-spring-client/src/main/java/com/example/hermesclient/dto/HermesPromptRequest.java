package com.example.hermesclient.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 调用 Hermes 聊天接口时的最小请求对象。
 *
 * <p>示例里只保留一个 prompt 字段，目的是让团队先把最小调用链路看明白，
 * 后续如果要扩展为多消息、系统提示词或会话参数，可以在这里继续加字段。</p>
 */
public class HermesPromptRequest {

    /**
     * 发送给 Hermes 的自然语言提示词。
     */
    @NotBlank
    private String prompt;

    /**
     * @return 当前请求里的提示词内容
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * @param prompt 要发送给 Hermes 的提示词
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}