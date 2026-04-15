package com.example.hermesmcp.controller;

import com.example.hermesmcp.service.McpToolService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * MCP 风格统一入口控制器。
 *
 * <p>这个控制器只有一个 /mcp POST 入口，
 * 再根据 JSON-RPC 风格请求里的 method 字段分发到不同处理逻辑。</p>
 */
@RestController
@RequestMapping("/mcp")
public class McpController {

    /**
     * 负责具体 MCP 方法实现的服务类。
     */
    private final McpToolService mcpToolService;

    /**
     * 构造器注入服务实现。
     *
     * @param mcpToolService MCP 业务服务
     */
    public McpController(McpToolService mcpToolService) {
        this.mcpToolService = mcpToolService;
    }

    /**
     * 统一处理 MCP 风格请求。
     *
     * <p>这里示例化地支持 initialize、tools/list、tools/call、ping 四类方法。</p>
     *
     * @param request JSON-RPC 风格请求体
     * @return 对应 method 的处理结果
     */
    @PostMapping
    public Map<String, Object> handle(@RequestBody Map<String, Object> request) {
        Object id = request.get("id");
        String method = String.valueOf(request.get("method"));
        Map<String, Object> params = request.get("params") instanceof Map<?, ?> raw
            ? (Map<String, Object>) raw
            : Map.of();

        return switch (method) {
            case "initialize" -> mcpToolService.initialize(id);
            case "tools/list" -> mcpToolService.toolsList(id);
            case "tools/call" -> mcpToolService.toolsCall(id, params);
            case "ping" -> mcpToolService.ping(id);
            default -> mcpToolService.unsupported(id, method);
        };
    }
}