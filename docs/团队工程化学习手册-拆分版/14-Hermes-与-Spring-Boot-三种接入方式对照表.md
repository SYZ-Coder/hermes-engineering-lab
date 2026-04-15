# Hermes 与 Spring Boot 三种接入方式对照表

如果团队要快速形成统一理解，最好的方式不是先讲代码，而是先把三种接入方式并排摆出来。

## 配套代码示例

建议和下面这个模块一起看：

- [example-spring-integration-modes/README.md](../../example-spring-integration-modes/README.md)

这个模块把三种方式统一收在一个 Spring Boot 工程里：

- API Server：`/demo/integrations/api-server/chat`
- Webhook：`/demo/integrations/webhook/forward`
- MCP：`/mcp`

## 1. 总对照表

| 方式 | 通信方向 | 角色关系 | 适合场景 | 重点机制 |
|---|---|---|---|---|
| API Server | Spring Boot -> Hermes | Spring Boot 调 Hermes | 同步问答、智能分析、即时结果 | HTTP 服务调用 |
| Webhook | Spring Boot -> Hermes | Spring Boot 推事件给 Hermes | 告警、工单、审批、回调事件 | 事件驱动处理 |
| MCP | Hermes -> Spring Boot | Hermes 调 Spring Boot | 查询订单、查询用户、审批、知识服务 | 外部工具接入 |

## 2. API Server 对照说明

### 它是什么？
Hermes 对外暴露的标准 HTTP 服务入口。

### 它的关系是什么？
- Spring Boot 是调用方
- Hermes 是智能服务方

### 它最适合什么？
- 一个请求对应一个智能处理结果
- 需要同步返回
- 前端 / 业务系统已经习惯调 HTTP 服务

### 它最强的点是什么？
- 最容易落地
- 最适合先跑通
- 最像团队熟悉的服务调用模式

### 它的限制是什么？
- 更适合同步请求
- 如果任务很长，需要额外转成 run/event 模式

## 3. Webhook 对照说明

### 它是什么？
Hermes 的事件接收入口。

### 它的关系是什么？
- Spring Boot 是事件源
- Hermes 是事件处理方

### 它最适合什么？
- 订单失败
- 告警通知
- 审批回调
- PR / 工单 / 风控事件

### 它最强的点是什么？
- 非常适合业务事件驱动场景
- 不要求业务系统“等一个返回结果”
- 更接近企业自动化处理流程

### 它的限制是什么？
- 不适合做最简单的前端同步问答入口
- 更适合异步、事件型交互

## 4. MCP 对照说明

### 它是什么？
Hermes 主动调用外部能力的标准方式。

### 它的关系是什么？
- Hermes 是调用方
- Spring Boot 是能力提供方

### 它最适合什么？
- 查询订单
- 查询用户
- 审批操作
- 查询知识库
- 企业内部 API 聚合

### 它最强的点是什么？
- 让 Spring Boot 的业务能力直接进入 Hermes 工具体系
- 让 Hermes 不只是“会说”，而是“会调业务能力”

### 它的限制是什么？
- 需要对 Spring Boot 服务做 MCP 能力包装
- 初次接入比 API Server 稍重一些

## 5. 怎么选最合适？

- 你要“Spring Boot 调 Hermes”：优先选 API Server
- 你要“Spring Boot 把事件推给 Hermes”：优先选 Webhook
- 你要“Hermes 调 Spring Boot”：优先选 MCP

## 6. 最推荐的落地顺序

| 阶段 | 推荐方式 | 目的 |
|---|---|---|
| 第一步 | API Server | 先打通服务调用 |
| 第二步 | Webhook | 把事件场景接进来 |
| 第三步 | MCP | 让 Hermes 真正接入业务能力 |

理由很简单：

- 先做最熟悉的 HTTP 调用
- 再做事件驱动
- 最后再做能力级集成

## 延伸阅读

- [09-Hermes-外部直接交互控制入口与-Spring-Boot-实践](./09-Hermes-外部直接交互控制入口与-Spring-Boot-实践.md)
- [15-Spring-Boot-接-Hermes-的标准落地手册](./15-Spring-Boot-接-Hermes-的标准落地手册.md)
- [example-spring-integration-modes/README.md](../../example-spring-integration-modes/README.md)
