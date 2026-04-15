package com.example.hermesclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 示例客户端的启动入口。
 *
 * <p>这个工程的目标是演示两条能力：</p>
 * <ul>
 *     <li>Spring Boot 如何主动调用 Hermes API Server</li>
 *     <li>Spring Boot 如何把事件转发给 Hermes Webhook</li>
 * </ul>
 */
@SpringBootApplication
public class HermesClientApplication {

    /**
     * 应用主入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(HermesClientApplication.class, args);
    }
}