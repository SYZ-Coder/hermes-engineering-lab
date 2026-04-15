package com.example.hermesintegrationmodes.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MCP 风格入口测试。
 *
 * 这里走真实的控制器 + 真实的内存工具服务，验证：
 * 1. tools/list 能返回 3 个工具
 * 2. query_order 能读到默认订单
 * 3. approve_order 后状态会更新为 APPROVED
 */
@SpringBootTest
@AutoConfigureMockMvc
class HermesMcpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 校验 tools/list 能返回三个演示工具。
     */
    @Test
    void shouldListThreeMcpTools() throws Exception {
        mockMvc.perform(post("/mcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "method": "tools/list"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tools.length()").value(3))
                .andExpect(jsonPath("$.tools[0].name").value("query_order"))
                .andExpect(jsonPath("$.tools[1].name").value("query_user"))
                .andExpect(jsonPath("$.tools[2].name").value("approve_order"));
    }

    /**
     * 校验 query_order 能读取内存中的默认订单。
     */
    @Test
    void shouldQueryOrderThroughMcpCall() throws Exception {
        mockMvc.perform(post("/mcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "method": "tools/call",
                                  "params": {
                                    "name": "query_order",
                                    "arguments": {
                                      "orderId": "A10086"
                                    }
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isError").value(false))
                .andExpect(jsonPath("$.content[0].text").value(org.hamcrest.Matchers.containsString("A10086")))
                .andExpect(jsonPath("$.content[0].text").value(org.hamcrest.Matchers.containsString("PENDING_APPROVAL")));
    }

    /**
     * 校验 approve_order 执行后，订单状态会变成 APPROVED。
     */
    @Test
    void shouldApproveOrderThroughMcpCall() throws Exception {
        mockMvc.perform(post("/mcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "method": "tools/call",
                                  "params": {
                                    "name": "approve_order",
                                    "arguments": {
                                      "orderId": "A10086",
                                      "approvedBy": "finance_manager"
                                    }
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isError").value(false))
                .andExpect(jsonPath("$.content[0].text").value(org.hamcrest.Matchers.containsString("APPROVED")))
                .andExpect(jsonPath("$.content[0].text").value(org.hamcrest.Matchers.containsString("finance_manager")));
    }
}