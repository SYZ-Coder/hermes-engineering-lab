# example-spring-integration-modes

这个模块把下面三种 Hermes + Spring Boot 接入方式放在了一个 Spring Boot 工程里：

1. **Spring Boot -> Hermes API Server**
2. **Spring Boot -> Hermes Webhook**
3. **Hermes -> Spring Boot MCP 服务**

它的目的不是替代 `example-spring-client` 和 `example-spring-mcp-server`，而是把“三种方式的最小闭环”统一到一个模块里，方便团队做培训、对照和演示。

## 对应文档

如果你想先看原理和落地方法，再回来看代码，建议先看这 3 篇：

- [09-Hermes-外部直接交互控制入口与-Spring-Boot-实践](../docs/团队工程化学习手册-拆分版/09-Hermes-外部直接交互控制入口与-Spring-Boot-实践.md)
- [14-Hermes-与-Spring-Boot-三种接入方式对照表](../docs/团队工程化学习手册-拆分版/14-Hermes-与-Spring-Boot-三种接入方式对照表.md)
- [15-Spring-Boot-接-Hermes-的标准落地手册](../docs/团队工程化学习手册-拆分版/15-Spring-Boot-接-Hermes-的标准落地手册.md)

如果你想从文档首页进入，也可以看：

- [docs/README.md](../docs/README.md)
- [《Hermes 团队工程化学习手册（拆分版）》](../docs/《Hermes 团队工程化学习手册（拆分版）》.md)

## 目录说明

- `HermesIntegrationController`
  演示前两种方式：
  - `/demo/integrations/api-server/chat`
  - `/demo/integrations/webhook/forward`

- `HermesMcpController`
  演示第三种方式：
  - `/mcp`

- `HermesApiServerService`
  封装 Spring Boot 调 Hermes API Server 的最小流程

- `HermesWebhookService`
  封装 Spring Boot 推送事件到 Hermes Webhook 的最小流程

- `HermesMcpToolService`
  封装 Spring Boot 作为 MCP 风格能力服务时的工具列表与工具调用

## 三种方式分别对应什么？

### 1. Spring Boot -> Hermes API Server

适合：
- 同步问答
- 同步智能分析
- 带工具的即时任务请求

请求示例：
```bash
curl -X POST http://127.0.0.1:8092/demo/integrations/api-server/chat \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "请帮我总结这条业务异常的影响范围",
    "sessionId": "biz-session-001"
  }'
```

### 2. Spring Boot -> Hermes Webhook

适合：
- 订单事件
- 告警事件
- 审批事件
- 工单事件

请求示例：
```bash
curl -X POST http://127.0.0.1:8092/demo/integrations/webhook/forward \
  -H "Content-Type: application/json" \
  -d '{
    "routeName": "business-alert",
    "eventType": "order_failed",
    "payload": {
      "orderId": "A10086",
      "message": "支付回调失败"
    }
  }'
```

### 3. Hermes -> Spring Boot MCP 服务

适合：
- 查询订单
- 查询用户
- 审批操作
- 读取业务状态

最小支持方法：

- `initialize`
- `tools/list`
- `tools/call`
- `ping`

请求示例：
```bash
curl -X POST http://127.0.0.1:8092/mcp \
  -H "Content-Type: application/json" \
  -d '{"method":"tools/list"}'
```

工具调用示例：
```bash
curl -X POST http://127.0.0.1:8092/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "method":"tools/call",
    "params":{
      "name":"query_order",
      "arguments":{
        "orderId":"A10086"
      }
    }
  }'
```

## 运行方式

### 1. 启动本模块
```bash
mvn spring-boot:run
```

默认端口：`8092`

### 2. 如果你要验证 API Server 或 Webhook

还需要先启动 Hermes 对应入口：
- API Server：确保 Hermes 的 `8642` 可用
- Webhook：确保 Hermes 的 `8644` 可用，并配置好 route

### 3. 如果你要验证 MCP

本模块本身就提供了最小 MCP 风格入口 `/mcp`，你可以直接让 Hermes 指向这个地址。

## 推荐使用方式

如果团队是第一次看这三种接法，建议按这个顺序：

1. 先用 `/demo/integrations/api-server/chat` 理解“Spring Boot 调 Hermes”
2. 再用 `/demo/integrations/webhook/forward` 理解“Spring Boot 推事件给 Hermes”
3. 最后用 `/mcp` 理解“Hermes 反向调 Spring Boot”
