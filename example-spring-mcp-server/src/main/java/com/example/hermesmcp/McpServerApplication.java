package com.example.hermesmcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot MCP 风格示例服务的启动入口。
 *
 * <p>这个工程用来演示另一条方向：</p>
 * <ul>
 *     <li>不是 Spring Boot 调 Hermes</li>
 *     <li>而是 Spring Boot 把自己的业务能力暴露出来，供 Hermes 通过 MCP 思路调用</li>
 * </ul>
 */
@SpringBootApplication
public class McpServerApplication {

    /**
     * 应用主入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
}