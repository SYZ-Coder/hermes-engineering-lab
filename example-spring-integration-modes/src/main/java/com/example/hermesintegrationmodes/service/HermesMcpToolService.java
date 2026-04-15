package com.example.hermesintegrationmodes.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP 工具服务。
 *
 * 这个服务演示“Spring Boot 作为 MCP 风格能力服务，被 Hermes 反向调用”时，
 * 业务能力应该如何以工具列表和工具调用的形式暴露出去。
 */
@Service
public class HermesMcpToolService {

    /**
     * 用于模拟用户表的内存数据。
     */
    private final Map<String, Map<String, Object>> users = new HashMap<>();

    /**
     * 用于模拟订单表的内存数据。
     */
    private final Map<String, Map<String, Object>> orders = new HashMap<>();

    /**
     * 初始化最小演示数据。
     */
    @PostConstruct
    public void initData() {
        Map<String, Object> user1 = new HashMap<>();
        user1.put("userId", "U20001");
        user1.put("name", "Alice");
        user1.put("level", "VIP");
        user1.put("riskLevel", "LOW");
        user1.put("owner", "finance_manager");
        users.put("U20001", user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("userId", "U20002");
        user2.put("name", "Bob");
        user2.put("level", "NORMAL");
        user2.put("riskLevel", "MEDIUM");
        user2.put("owner", "ops_lead");
        users.put("U20002", user2);

        Map<String, Object> order1 = new HashMap<>();
        order1.put("orderId", "A10086");
        order1.put("userId", "U20001");
        order1.put("amount", new BigDecimal("1999.00"));
        order1.put("status", "PENDING_APPROVAL");
        order1.put("approvedBy", null);
        order1.put("updatedAt", LocalDateTime.now().toString());
        orders.put("A10086", order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("orderId", "A10087");
        order2.put("userId", "U20002");
        order2.put("amount", new BigDecimal("399.00"));
        order2.put("status", "PAID");
        order2.put("approvedBy", null);
        order2.put("updatedAt", LocalDateTime.now().toString());
        orders.put("A10087", order2);
    }

    /**
     * 返回 MCP 服务支持的工具列表。
     *
     * @return 用于 tools/list 的工具描述
     */
    public Map<String, Object> listTools() {
        return Map.of(
                "tools", List.of(
                        Map.of(
                                "name", "query_order",
                                "description", "查询订单详情和订单状态",
                                "inputSchema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "orderId", Map.of("type", "string", "description", "订单编号")
                                        ),
                                        "required", List.of("orderId")
                                )
                        ),
                        Map.of(
                                "name", "query_user",
                                "description", "查询用户档案和风险等级",
                                "inputSchema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "userId", Map.of("type", "string", "description", "用户编号")
                                        ),
                                        "required", List.of("userId")
                                )
                        ),
                        Map.of(
                                "name", "approve_order",
                                "description", "审批一个待审批订单",
                                "inputSchema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "orderId", Map.of("type", "string", "description", "订单编号"),
                                                "approvedBy", Map.of("type", "string", "description", "审批人")
                                        ),
                                        "required", List.of("orderId", "approvedBy")
                                )
                        )
                )
        );
    }

    /**
     * 根据工具名分发调用。
     *
     * @param toolName 工具名称
     * @param arguments 工具参数
     * @return 统一结果结构
     */
    public Map<String, Object> callTool(String toolName, Map<String, Object> arguments) {
        return switch (toolName) {
            case "query_order" -> queryOrder(String.valueOf(arguments.get("orderId")));
            case "query_user" -> queryUser(String.valueOf(arguments.get("userId")));
            case "approve_order" -> approveOrder(
                    String.valueOf(arguments.get("orderId")),
                    String.valueOf(arguments.get("approvedBy"))
            );
            default -> Map.of(
                    "content", List.of(Map.of("type", "text", "text", "Unknown tool: " + toolName)),
                    "isError", true
            );
        };
    }

    /**
     * 查询订单，并把关联用户信息一起返回。
     *
     * @param orderId 订单编号
     * @return MCP 风格的工具返回内容
     */
    public Map<String, Object> queryOrder(String orderId) {
        Map<String, Object> order = orders.get(orderId);
        if (order == null) {
            return Map.of(
                    "content", List.of(Map.of("type", "text", "text", "订单不存在: " + orderId)),
                    "isError", true
            );
        }
        Map<String, Object> user = users.get(String.valueOf(order.get("userId")));
        return Map.of(
                "content", List.of(Map.of(
                        "type", "text",
                        "text", "订单详情: " + Map.of("order", order, "user", user)
                )),
                "isError", false
        );
    }

    /**
     * 查询用户信息。
     *
     * @param userId 用户编号
     * @return MCP 风格的工具返回内容
     */
    public Map<String, Object> queryUser(String userId) {
        Map<String, Object> user = users.get(userId);
        if (user == null) {
            return Map.of(
                    "content", List.of(Map.of("type", "text", "text", "用户不存在: " + userId)),
                    "isError", true
            );
        }
        return Map.of(
                "content", List.of(Map.of("type", "text", "text", "用户详情: " + user)),
                "isError", false
        );
    }

    /**
     * 审批订单，并更新内存里的订单状态。
     *
     * @param orderId 订单编号
     * @param approvedBy 审批人
     * @return MCP 风格的工具返回内容
     */
    public Map<String, Object> approveOrder(String orderId, String approvedBy) {
        Map<String, Object> order = orders.get(orderId);
        if (order == null) {
            return Map.of(
                    "content", List.of(Map.of("type", "text", "text", "订单不存在: " + orderId)),
                    "isError", true
            );
        }
        order.put("status", "APPROVED");
        order.put("approvedBy", approvedBy);
        order.put("updatedAt", LocalDateTime.now().toString());
        return Map.of(
                "content", List.of(Map.of("type", "text", "text", "审批完成: " + order)),
                "isError", false
        );
    }

    /**
     * 返回 initialize 响应用的基础能力声明。
     *
     * @return MCP 服务器初始化结果
     */
    public Map<String, Object> initialize() {
        return Map.of(
                "protocolVersion", "2024-11-05",
                "serverInfo", Map.of("name", "spring-hermes-integration-modes", "version", "0.0.1"),
                "capabilities", Map.of("tools", Map.of())
        );
    }
}