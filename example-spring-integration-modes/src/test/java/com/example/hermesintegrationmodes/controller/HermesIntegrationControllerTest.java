package com.example.hermesintegrationmodes.controller;

import com.example.hermesintegrationmodes.service.HermesApiServerService;
import com.example.hermesintegrationmodes.service.HermesWebhookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 针对“Spring Boot -> Hermes”两条链路的最小控制器测试。
 *
 * 这里不真实请求 Hermes，而是通过 Mock 服务对象验证：
 * 1. API Server 路径能正确接收请求
 * 2. Webhook 路径能正确接收业务事件
 */
@SpringBootTest
@AutoConfigureMockMvc
class HermesIntegrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HermesApiServerService hermesApiServerService;

    @MockBean
    private HermesWebhookService hermesWebhookService;

    /**
     * 校验模式说明接口能返回三种接入方式。
     */
    @Test
    void shouldExposeThreeIntegrationModes() throws Exception {
        mockMvc.perform(get("/demo/integrations/modes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.module").value("example-spring-integration-modes"))
                .andExpect(jsonPath("$.covers[0]").value("api-server"))
                .andExpect(jsonPath("$.covers[1]").value("webhook"))
                .andExpect(jsonPath("$.covers[2]").value("mcp"));
    }

    /**
     * 校验 API Server 演示入口会把 prompt 和 sessionId 传给服务层。
     */
    @Test
    void shouldForwardChatToHermesApiServerService() throws Exception {
        when(hermesApiServerService.chat(eq("请帮我分析订单异常"), eq("session-001")))
                .thenReturn("mock-api-response");

        mockMvc.perform(post("/demo/integrations/api-server/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "prompt": "请帮我分析订单异常",
                                  "sessionId": "session-001"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("mock-api-response"));
    }

    /**
     * 校验 Webhook 演示入口会把 route、eventType 和 payload 传给服务层。
     */
    @Test
    void shouldForwardEventToHermesWebhookService() throws Exception {
        when(hermesWebhookService.forwardEvent(eq("business-alert"), eq("order_failed"), anyMap()))
                .thenReturn("mock-webhook-response");

        mockMvc.perform(post("/demo/integrations/webhook/forward")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "routeName": "business-alert",
                                  "eventType": "order_failed",
                                  "payload": {
                                    "orderId": "A10086",
                                    "message": "支付回调失败"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("mock-webhook-response"));
    }
}