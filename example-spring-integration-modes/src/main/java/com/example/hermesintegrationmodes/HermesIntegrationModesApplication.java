package com.example.hermesintegrationmodes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用入口。
 *
 * 这个模块把三种最常见的 Hermes + Spring Boot 接入方式放到一个 Spring Boot 工程里：
 * 1. Spring Boot 调 Hermes API Server
 * 2. Spring Boot 推事件到 Hermes Webhook
 * 3. Spring Boot 作为 MCP 风格能力服务供 Hermes 调用
 */
@SpringBootApplication
public class HermesIntegrationModesApplication {

    /**
     * 启动 Spring Boot 应用。
     *
     * @param args 标准启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(HermesIntegrationModesApplication.class, args);
    }
}