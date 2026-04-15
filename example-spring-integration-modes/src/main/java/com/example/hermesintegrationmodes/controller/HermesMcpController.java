package com.example.hermesintegrationmodes.controller;

import com.example.hermesintegrationmodes.service.HermesMcpToolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * MCP 风格控制器。
 *
 * 这里用一个最小 HTTP 入口来模拟“Spring Boot 作为 Hermes 外部能力提供方”
 * 的工作方式，方便团队把三种接入模式放在同一个模块里理解。
 */
@RestController
@RequestMapping("/mcp")
public class HermesMcpController {

    private final HermesMcpToolService hermesMcpToolService;

    public HermesMcpController(HermesMcpToolService hermesMcpToolService) {
        this.hermesMcpToolService = hermesMcpToolService;
    }

    /**
     * 统一 MCP 风格入口。
     *
     * 这里按最小可读实现支持四类方法：
     * - initialize
     * - tools/list
     * - tools/call
     * - ping
     *
     * @param request 请求体，至少包含 method，工具调用时还包含 params
     * @return 对应方法的结果
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> handle(@RequestBody Map<String, Object> request) {
        String method = String.valueOf(request.get("method"));
        Map<String, Object> params = extractParams(request);

        return switch (method) {
            case "initialize" -> ResponseEntity.ok(hermesMcpToolService.initialize());
            case "tools/list" -> ResponseEntity.ok(hermesMcpToolService.listTools());
            case "tools/call" -> ResponseEntity.ok(
                    hermesMcpToolService.callTool(
                            String.valueOf(params.get("name")),
                            extractArguments(params)
                    )
            );
            case "ping" -> ResponseEntity.ok(Map.of("status", "ok"));
            default -> ResponseEntity.badRequest().body(Map.of(
                    "error", "Unsupported method: " + method
            ));
        };
    }

    /**
     * 提取顶层 params。
     *
     * @param request 原始请求体
     * @return params 对象，不存在时返回空 Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> extractParams(Map<String, Object> request) {
        Object params = request.get("params");
        if (params instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return Collections.emptyMap();
    }

    /**
     * 提取 tools/call 的 arguments。
     *
     * @param params 顶层 params
     * @return arguments 对象，不存在时返回空 Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> extractArguments(Map<String, Object> params) {
        Object arguments = params.get("arguments");
        if (arguments instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return Collections.emptyMap();
    }
}