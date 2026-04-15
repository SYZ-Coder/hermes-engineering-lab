package com.example.hermesmcp.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP 工具服务。
 *
 * <p>这个类承担三类职责：</p>
 * <ul>
 *     <li>提供 MCP 基础方法，例如 initialize、tools/list、ping</li>
 *     <li>暴露示例业务工具，例如 query_order、query_user、approve_order</li>
 *     <li>维护一套内存数据表，让多个工具之间共享状态</li>
 * </ul>
 */
@Service
public class McpToolService {

    /**
     * 模拟订单表。
     * key 为 orderId，value 为订单字段集合。
     */
    private final Map<String, Map<String, Object>> orders = new ConcurrentHashMap<>();

    /**
     * 模拟用户表。
     * key 为 userId，value 为用户字段集合。
     */
    private final Map<String, Map<String, Object>> users = new ConcurrentHashMap<>();

    /**
     * 初始化示例数据。
     *
     * <p>这样工程一启动，团队就可以直接演示“先查订单，再审批，再查订单”的状态变化。</p>
     */
    @PostConstruct
    void initMockData() {
        users.put("U20001", user("U20001", "Alice", "VIP", "LOW", "finance_manager"));
        users.put("U20002", user("U20002", "Bob", "NORMAL", "MEDIUM", "ops_lead"));

        orders.put("A10086", order("A10086", "U20001", "PENDING_APPROVAL", 199.00, "APP", null));
        orders.put("A10087", order("A10087", "U20002", "PAID", 88.50, "WEB", "system_auto"));
    }

    /**
     * 返回 MCP 初始化信息。
     *
     * @param id 请求 id
     * @return initialize 响应
     */
    public Map<String, Object> initialize(Object id) {
        return response(id, Map.of(
            "protocolVersion", "2024-11-05",
            "serverInfo", Map.of("name", "example-spring-mcp-server", "version", "0.0.1")
        ));
    }

    /**
     * 返回当前服务暴露的工具列表。
     *
     * @param id 请求 id
     * @return tools/list 响应
     */
    public Map<String, Object> toolsList(Object id) {
        return response(id, Map.of("tools", List.of(
            queryOrderTool(),
            queryUserTool(),
            approveOrderTool()
        )));
    }

    /**
     * 按工具名分发 tools/call 调用。
     *
     * @param id 请求 id
     * @param params tools/call 参数
     * @return 某个工具的执行结果
     */
    public Map<String, Object> toolsCall(Object id, Map<String, Object> params) {
        String name = String.valueOf(params.get("name"));
        Map<String, Object> arguments = params.get("arguments") instanceof Map<?, ?> args
            ? (Map<String, Object>) args
            : Map.of();

        return switch (name) {
            case "query_order" -> queryOrder(id, arguments);
            case "query_user" -> queryUser(id, arguments);
            case "approve_order" -> approveOrder(id, arguments);
            default -> error(id, -32601, "Unknown tool: " + name);
        };
    }

    /**
     * ping 用于健康检查或链路探活。
     *
     * @param id 请求 id
     * @return ping 响应
     */
    public Map<String, Object> ping(Object id) {
        return response(id, Map.of("ok", true));
    }

    /**
     * 处理不支持的方法。
     *
     * @param id 请求 id
     * @param method 未支持的方法名
     * @return 错误响应
     */
    public Map<String, Object> unsupported(Object id, String method) {
        return error(id, -32601, "Unsupported method: " + method);
    }

    /**
     * 定义 query_order 工具的 schema。
     *
     * @return 工具定义
     */
    private Map<String, Object> queryOrderTool() {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", "query_order");
        tool.put("description", "Query an order record from the in-memory Spring Boot service.");
        tool.put("inputSchema", Map.of(
            "type", "object",
            "properties", Map.of(
                "orderId", Map.of("type", "string", "description", "Order ID to query")
            ),
            "required", List.of("orderId")
        ));
        return tool;
    }

    /**
     * 定义 query_user 工具的 schema。
     *
     * @return 工具定义
     */
    private Map<String, Object> queryUserTool() {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", "query_user");
        tool.put("description", "Query a user profile from the in-memory Spring Boot service.");
        tool.put("inputSchema", Map.of(
            "type", "object",
            "properties", Map.of(
                "userId", Map.of("type", "string", "description", "User ID to query")
            ),
            "required", List.of("userId")
        ));
        return tool;
    }

    /**
     * 定义 approve_order 工具的 schema。
     *
     * @return 工具定义
     */
    private Map<String, Object> approveOrderTool() {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", "approve_order");
        tool.put("description", "Approve an order in the in-memory Spring Boot service.");
        tool.put("inputSchema", Map.of(
            "type", "object",
            "properties", Map.of(
                "orderId", Map.of("type", "string", "description", "Order ID to approve"),
                "approver", Map.of("type", "string", "description", "Approver name")
            ),
            "required", List.of("orderId", "approver")
        ));
        return tool;
    }

    /**
     * 查询订单，并额外带出关联用户信息。
     *
     * @param id 请求 id
     * @param arguments 工具参数
     * @return 查询结果或错误
     */
    private Map<String, Object> queryOrder(Object id, Map<String, Object> arguments) {
        String orderId = String.valueOf(arguments.getOrDefault("orderId", "UNKNOWN"));
        Map<String, Object> order = orders.get(orderId);
        if (order == null) {
            return error(id, -32004, "Order not found: " + orderId);
        }

        String userId = String.valueOf(order.get("userId"));
        Map<String, Object> user = users.get(userId);
        String text = "Order result: orderId=" + order.get("orderId")
            + ", status=" + order.get("status")
            + ", amount=" + order.get("amount")
            + ", channel=" + order.get("channel")
            + ", userId=" + userId
            + ", userName=" + (user != null ? user.get("name") : "UNKNOWN")
            + ", approvedBy=" + order.get("approvedBy");
        return successText(id, text);
    }

    /**
     * 查询用户。
     *
     * @param id 请求 id
     * @param arguments 工具参数
     * @return 查询结果或错误
     */
    private Map<String, Object> queryUser(Object id, Map<String, Object> arguments) {
        String userId = String.valueOf(arguments.getOrDefault("userId", "UNKNOWN"));
        Map<String, Object> user = users.get(userId);
        if (user == null) {
            return error(id, -32004, "User not found: " + userId);
        }

        String text = "User result: userId=" + user.get("userId")
            + ", name=" + user.get("name")
            + ", level=" + user.get("level")
            + ", risk=" + user.get("risk")
            + ", owner=" + user.get("owner");
        return successText(id, text);
    }

    /**
     * 审批订单，并修改共享内存表中的状态。
     *
     * @param id 请求 id
     * @param arguments 工具参数
     * @return 审批结果或错误
     */
    private Map<String, Object> approveOrder(Object id, Map<String, Object> arguments) {
        String orderId = String.valueOf(arguments.getOrDefault("orderId", "UNKNOWN"));
        String approver = String.valueOf(arguments.getOrDefault("approver", "SYSTEM"));
        Map<String, Object> order = orders.get(orderId);
        if (order == null) {
            return error(id, -32004, "Order not found: " + orderId);
        }

        // 这里直接更新共享内存数据，便于演示工具之间的状态联动。
        order.put("status", "APPROVED");
        order.put("approvedBy", approver);

        String text = "Approval result: orderId=" + order.get("orderId")
            + ", approved=true"
            + ", status=" + order.get("status")
            + ", approver=" + order.get("approvedBy");
        return successText(id, text);
    }

    /**
     * 生成一条订单记录。
     *
     * @param orderId 订单 id
     * @param userId 用户 id
     * @param status 订单状态
     * @param amount 订单金额
     * @param channel 下单渠道
     * @param approvedBy 审批人
     * @return 订单记录
     */
    private Map<String, Object> order(String orderId, String userId, String status, double amount, String channel, String approvedBy) {
        Map<String, Object> order = new ConcurrentHashMap<>();
        order.put("orderId", orderId);
        order.put("userId", userId);
        order.put("status", status);
        order.put("amount", amount);
        order.put("channel", channel);
        order.put("approvedBy", approvedBy == null ? "NONE" : approvedBy);
        return order;
    }

    /**
     * 生成一条用户记录。
     *
     * @param userId 用户 id
     * @param name 用户名
     * @param level 用户等级
     * @param risk 风险等级
     * @param owner 归属负责人
     * @return 用户记录
     */
    private Map<String, Object> user(String userId, String name, String level, String risk, String owner) {
        Map<String, Object> user = new ConcurrentHashMap<>();
        user.put("userId", userId);
        user.put("name", name);
        user.put("level", level);
        user.put("risk", risk);
        user.put("owner", owner);
        return user;
    }

    /**
     * 生成成功响应的文本内容。
     *
     * @param id 请求 id
     * @param text 返回文本
     * @return 标准成功响应
     */
    private Map<String, Object> successText(Object id, String text) {
        return response(id, Map.of(
            "content", List.of(Map.of("type", "text", "text", text)),
            "isError", false
        ));
    }

    /**
     * 生成标准 JSON-RPC 成功响应。
     *
     * @param id 请求 id
     * @param result 结果体
     * @return JSON-RPC 响应
     */
    private Map<String, Object> response(Object id, Object result) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("jsonrpc", "2.0");
        body.put("id", id);
        body.put("result", result);
        return body;
    }

    /**
     * 生成标准 JSON-RPC 错误响应。
     *
     * @param id 请求 id
     * @param code 错误码
     * @param message 错误消息
     * @return 错误响应
     */
    private Map<String, Object> error(Object id, int code, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("jsonrpc", "2.0");
        body.put("id", id);
        body.put("error", Map.of("code", code, "message", message));
        return body;
    }
}