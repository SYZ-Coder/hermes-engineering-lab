# Hermes + Spring Boot 快速开始

这是一份面向团队的快速入口页，帮助你在最短时间内理解：

1. Spring Boot 如何调用 Hermes
2. Spring Boot 如何把事件推给 Hermes
3. Hermes 如何反过来调用 Spring Boot

## 1. 先记住结论

Spring Boot 和 Hermes 对接，最清晰的方式有 3 种：

1. Spring Boot 主动请求 Hermes
2. Spring Boot 把业务事件推送给 Hermes
3. Hermes 主动调用 Spring Boot

推荐顺序：

- Spring Boot 调 Hermes：优先用 API Server
- Spring Boot 推事件给 Hermes：用 Webhook
- Hermes 调 Spring Boot：优先把 Spring Boot 做成 MCP 服务

## 2. 方式一：Spring Boot 调 Hermes

Hermes 对外可以作为标准 HTTP 能力服务使用。

常见接口包括：

- `POST /v1/chat/completions`
- `POST /v1/responses`
- `GET /v1/models`
- `GET /health`

适合场景：

- 问答式能力调用
- 带工具的任务请求
- 想复用 OpenAI 兼容协议

## 3. 方式二：Spring Boot 推事件给 Hermes

Hermes 支持 Webhook 方式接收外部事件。

适合场景：

- 告警通知
- 工单事件
- 审批流事件
- PR / Issue / 外部平台回调

## 4. 方式三：Hermes 调 Spring Boot

如果希望 Hermes 主动调用你的业务系统，最推荐的方式是：

- Spring Boot 提供 MCP 服务
- Hermes 通过 MCP 接入
- Hermes 把 Spring Boot 暴露的能力当成工具调用

适合场景：

- 查询订单
- 查询用户
- 执行审批
- 拉取报表
- 调内部业务 API

## 5. 现在应该先看哪里

### 如果你想先看整体理解

- `docs/README.md`
- `docs/《Hermes 团队工程化学习手册（拆分版）》.md`

### 如果你想直接看 Spring Boot 示例

- `example-spring-client/README.md`
- `example-spring-mcp-server/README.md`
- `example-spring-integration-modes/README.md`

### 如果你想看图和架构链路

- `docs/团队学习图谱-Hermes架构流程与通讯时序.md`

## 6. 推荐阅读顺序

1. `docs/《Hermes 团队工程化学习手册（拆分版）》.md`
2. `docs/团队工程化学习手册-拆分版/09-Hermes-外部直接交互控制入口与-Spring-Boot-实践.md`
3. `docs/团队工程化学习手册-拆分版/14-Hermes-与-Spring-Boot-三种接入方式对照表.md`
4. `docs/团队工程化学习手册-拆分版/15-Spring-Boot-接-Hermes-的标准落地手册.md`
5. `example-spring-client/README.md`
6. `example-spring-mcp-server/README.md`
7. `example-spring-integration-modes/README.md`

## 7. 一句话记忆

**Spring Boot 调 Hermes，优先走 API Server；Spring Boot 推事件给 Hermes，走 Webhook；Hermes 调 Spring Boot，优先把 Spring Boot 做成 MCP 服务。**