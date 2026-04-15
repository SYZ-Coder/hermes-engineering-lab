package com.example.hermesclient.controller;

import com.example.hermesclient.dto.HermesPromptRequest;
import com.example.hermesclient.dto.WebhookEventRequest;
import com.example.hermesclient.service.HermesApiService;
import com.example.hermesclient.service.HermesWebClientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 对外暴露示例接口的控制器。
 *
 * <p>这个控制器本身不承载复杂业务，主要职责是把外部请求转发到不同的 Hermes 调用实现：</p>
 * <ul>
 *     <li>HermesApiService：基于 RestTemplate 的同步调用</li>
 *     <li>HermesWebClientService：基于 WebClient 的现代调用</li>
 * </ul>
 */
@RestController
@RequestMapping("/demo/hermes")
public class HermesDemoController {

    /**
     * RestTemplate 版服务。
     */
    private final HermesApiService hermesApiService;

    /**
     * WebClient 版服务。
     */
    private final HermesWebClientService hermesWebClientService;

    /**
     * 构造器注入两个版本的 Hermes 调用服务。
     *
     * @param hermesApiService RestTemplate 版调用服务
     * @param hermesWebClientService WebClient 版调用服务
     */
    public HermesDemoController(HermesApiService hermesApiService,
                                HermesWebClientService hermesWebClientService) {
        this.hermesApiService = hermesApiService;
        this.hermesWebClientService = hermesWebClientService;
    }

    /**
     * 用 RestTemplate 转发一条普通聊天请求到 Hermes Chat Completions。
     *
     * @param request 聊天请求体
     * @return Hermes 返回的原始响应结构
     */
    @PostMapping("/chat")
    public Map<String, Object> chat(@Valid @RequestBody HermesPromptRequest request) {
        return hermesApiService.chat(request.getPrompt());
    }

    /**
     * 用 RestTemplate 转发一条 webhook 事件到 Hermes。
     *
     * @param request webhook 事件请求体
     * @return Hermes webhook 的响应文本
     */
    @PostMapping("/webhook")
    public String webhook(@Valid @RequestBody WebhookEventRequest request) {
        return hermesApiService.sendWebhook(request.getRoute(), request.getEvent(), request.getMessage());
    }

    /**
     * 用 WebClient 转发一条普通聊天请求到 Hermes Chat Completions。
     *
     * @param request 聊天请求体
     * @return Hermes 返回的原始响应结构
     */
    @PostMapping("/chat-webclient")
    public Map<String, Object> chatWithWebClient(@Valid @RequestBody HermesPromptRequest request) {
        return hermesWebClientService.chat(request.getPrompt());
    }

    /**
     * 用 WebClient 转发一条 webhook 事件到 Hermes。
     *
     * @param request webhook 事件请求体
     * @return Hermes webhook 的响应文本
     */
    @PostMapping("/webhook-webclient")
    public String webhookWithWebClient(@Valid @RequestBody WebhookEventRequest request) {
        return hermesWebClientService.sendWebhook(request.getRoute(), request.getEvent(), request.getMessage());
    }
}