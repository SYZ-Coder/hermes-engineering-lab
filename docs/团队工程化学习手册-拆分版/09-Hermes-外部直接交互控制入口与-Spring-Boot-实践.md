# Hermes 提供有哪些与外部直接交互控制的入口以及如何具体控制？

如果把这个问题压成一句话：

**Hermes 对外有“让别人来控制我”的入口，也有“我去控制外部能力”的入口。**

如果外部是 Spring Boot，最常用、最实用的组合通常是三种：

1. Spring Boot -> Hermes API Server
2. Spring Boot -> Hermes Webhook
3. Hermes -> Spring Boot MCP 服务

## 配套代码示例

对应代码工程：

- `D:\spring_AI\hermes_spring\example-spring-client`
- `D:\spring_AI\hermes_spring\example-spring-mcp-server`
- `D:\spring_AI\hermes_spring\example-spring-integration-modes`

如果只想看一个最集中、最适合培训演示的工程，优先看：

- [example-spring-integration-modes/README.md](../../example-spring-integration-modes/README.md)

这个模块把三种方式都收敛在一个 Spring Boot 工程里：

- `/demo/integrations/api-server/chat`
- `/demo/integrations/webhook/forward`
- `/mcp`

## 1. 正式入口有哪些？

### API Server

这是最标准的“外部服务主动请求 Hermes”的入口。
它本质上是：

- Hermes 对外暴露一个 HTTP 服务
- 外部系统按 OpenAI 风格协议请求它
- Hermes 收到请求后，进入统一 Agent Runtime 执行

它适合：
- Spring Boot 同步调用 Hermes
- 前端经 Spring Boot 再转 Hermes
- 统一 AI / Agent 服务接入

### Webhook

这是最标准的“外部系统把事件推给 Hermes”的入口。
它本质上不是“问一句回一句”，而是：

- 外部系统发生一个事件
- POST 给 Hermes
- Hermes 把这个事件转换成 Agent 任务
- 再按配置把结果发回去

### ACP

这是协议型入口，更适合：
- 编辑器
- IDE 插件
- ACP 客户端

对 Spring Boot 而言，它一般不是第一选择。

## 2. Hermes 如何“被外部控制”？

这里要抓住一个核心原理：

**不管外部从 API Server、Webhook 还是 ACP 进来，最后都不是直接操作零散模块，而是会汇入统一 Agent Runtime。**

也就是说：
- 外部入口负责接协议 / 事件
- Hermes 内部统一把它们转成 Agent 执行
- 真正执行任务的是同一套运行时

## 3. 如果外部是 Spring Boot，三种最常见结合方式

### 方案一：Spring Boot 调 Hermes API Server

#### 原理
Spring Boot 把 Hermes 当成一个 HTTP Agent 服务调用。
Hermes 收到请求后，进入 AIAgent 主循环，必要时调用工具，然后返回结果。

#### 机制
Spring Boot 可以控制 Hermes 的方式主要有三种：
1. 同步请求
2. 会话连续控制
3. 异步 run + 事件流

#### 实践流程
1. 在 Hermes 侧启动 API Server
2. Spring Boot 用 HTTP Client 调 Hermes
3. 先只打通一个最小问答接口
4. 再补 session 控制
5. 长任务再升级成 run/event 模式

### 方案二：Spring Boot 把事件推给 Hermes Webhook

#### 原理
Spring Boot 不把 Hermes 当“同步服务”来问，而是把 Hermes 当“事件处理器”。

#### 机制
一个 webhook route 通常会定义：

- 接受什么事件
- 用什么 secret 校验
- payload 如何变成 prompt
- 是否加载额外 skills
- 结果发到哪里去

同时会做：
- HMAC 校验
- 限流
- 幂等控制
- body size 限制

#### 实践流程
1. 明确 Spring Boot 要推什么事件
2. 在 Hermes 配 route
3. Spring Boot 在业务逻辑里把事件 POST 给 Hermes
4. Hermes 执行并回传结果

### 方案三：Hermes 反过来调用 Spring Boot，Spring Boot 作为 MCP 服务

#### 原理
Spring Boot 不再只是调用 Hermes，而是把自己的业务能力暴露给 Hermes。
Hermes 把这些能力当成“工具”来调用。

#### 机制
Hermes 在这边的工作机制是：

1. 连接 MCP Server
2. 发现外部工具
3. 注册到 Hermes 工具系统
4. Agent 按需调用

#### 实践流程
1. 先确定暴露哪些业务能力
2. 把这些能力包装成 MCP 工具
3. 在 Hermes 配置 MCP Server
4. 验证工具发现和调用
5. 再逐步扩展到更多业务能力

## 4. 三种方式怎么选？

- 你想让 Spring Boot 调 Hermes：选 API Server
- 你想让 Spring Boot 把业务事件交给 Hermes：选 Webhook
- 你想让 Hermes 调 Spring Boot 的业务能力：选 MCP

## 5. 一句话总结

**Hermes 对外直接交互 / 控制的正式入口主要是 API Server、Webhook、ACP；而和 Spring Boot 结合时，最实用的三条链路分别是 Spring Boot -> API Server、Spring Boot -> Webhook、Hermes -> Spring Boot MCP。**

## 延伸阅读

- [14-Hermes-与-Spring-Boot-三种接入方式对照表](./14-Hermes-与-Spring-Boot-三种接入方式对照表.md)
- [15-Spring-Boot-接-Hermes-的标准落地手册](./15-Spring-Boot-接-Hermes-的标准落地手册.md)
- [example-spring-integration-modes/README.md](../../example-spring-integration-modes/README.md)
