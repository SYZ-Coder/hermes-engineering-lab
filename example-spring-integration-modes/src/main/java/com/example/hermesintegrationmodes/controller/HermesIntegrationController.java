package com.example.hermesintegrationmodes.controller;

import com.example.hermesintegrationmodes.dto.ApiChatRequest;
import com.example.hermesintegrationmodes.dto.WebhookForwardRequest;
import com.example.hermesintegrationmodes.service.HermesApiServerService;
import com.example.hermesintegrationmodes.service.HermesWebhookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 三种接入方式里的“Spring Boot -> Hermes”入口控制器。
 *
 * 这个控制器主要负责演示两件事：
 * 1. 如何从 Spring Boot 调 Hermes API Server
 * 2. 如何从 Spring Boot 把事件推给 Hermes Webhook
 */
@RestController
@RequestMapping("/demo/integrations")
public class HermesIntegrationController {

    private final HermesApiServerService hermesApiServerService;
    private final HermesWebhookService hermesWebhookService;

    public HermesIntegrationController(
            HermesApiServerService hermesApiServerService,
            HermesWebhookService hermesWebhookService
    ) {
        this.hermesApiServerService = hermesApiServerService;
        this.hermesWebhookService = hermesWebhookService;
    }

    /**
     * 返回当前模块覆盖的三种接入方式说明。
     *
     * @return 简要说明，方便打开项目后快速自检
     */
    @GetMapping("/modes")
    public ResponseEntity<Map<String, Object>> modes() {
        return ResponseEntity.ok(Map.of(
                "module", "example-spring-integration-modes",
                "covers", new String[]{"api-server", "webhook", "mcp"}
        ));
    }

    /**
     * 演示“Spring Boot 调 Hermes API Server”。
     *
     * @param request 业务侧输入的 prompt 和可选 sessionId
     * @return Hermes 原始响应
     */
    @PostMapping("/api-server/chat")
    public ResponseEntity<String> chatViaApiServer(@Valid @RequestBody ApiChatRequest request) {
        return ResponseEntity.ok(
                hermesApiServerService.chat(request.getPrompt(), request.getSessionId())
        );
    }

    /**
     * 演示“Spring Boot 推事件到 Hermes Webhook”。
     *
     * @param request webhook route、事件类型和事件 payload
     * @return Hermes webhook 原始响应
     */
    @PostMapping("/webhook/forward")
    public ResponseEntity<String> forwardViaWebhook(@Valid @RequestBody WebhookForwardRequest request) {
        return ResponseEntity.ok(
                hermesWebhookService.forwardEvent(
                        request.getRouteName(),
                        request.getEventType(),
                        request.getPayload()
                )
        );
    }
}