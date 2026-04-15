# Spring Boot 接 Hermes 的标准落地手册

下面给你一个更实战的版本，适合团队拿去做内部落地说明。

## 配套代码示例

建议优先配合这个模块一起看：

- [example-spring-integration-modes/README.md](../../example-spring-integration-modes/README.md)

如果你们团队想看更细的拆分式示例，也可以分别看：

- `D:\spring_AI\hermes_spring\example-spring-client`
- `D:\spring_AI\hermes_spring\example-spring-mcp-server`

## 第一阶段：先明确系统分工

### Hermes 负责什么？
- 理解用户任务
- 规划处理动作
- 决定调用什么工具或外部能力
- 组织多轮上下文
- 汇总最终结果

### Spring Boot 负责什么？
- 提供稳定业务接口
- 管理用户、订单、审批、权限、审计
- 对接数据库和内部中台
- 继续作为企业业务主系统

必须先统一这个共识：

**Hermes 做智能编排层，Spring Boot 做稳定业务层。**

## 第二阶段：最小可落地方案先走 API Server

### 目标
先打通：

- Spring Boot 能请求 Hermes
- Hermes 能返回结果
- 会话可连续
- 团队能看到第一条完整链路

### 推荐步骤
1. 部署 Hermes API Server
2. Spring Boot 建一个最小调用客户端
3. 提供一个 `/demo/hermes/chat` 或 `/demo/integrations/api-server/chat` 之类的测试接口
4. 先只传最简单 prompt
5. 验证请求和响应结构
6. 再补 session 控制

### 这一阶段不要做什么？
- 不要一开始就接太多业务逻辑
- 不要一开始就搞多入口
- 不要一开始就把所有工具全接进来

### 这一阶段的目的
不是“做强”，而是“先跑顺”。

## 第三阶段：把事件场景接到 Webhook

### 目标
让业务事件也能进入 Hermes。

### 推荐步骤
1. 确定要接入的事件类型
2. 给 Hermes 配 route
3. 给每个 route 配：
   - secret
   - event filter
   - prompt 模板
   - 结果投递方式
4. Spring Boot 在事件触发点发送 webhook
5. 验证 Hermes 的处理和回传链路

### 适合优先接哪些事件？
- 订单异常
- 审批驳回
- 告警触发
- 风控命中
- 工单升级

## 第四阶段：用 MCP 把 Spring Boot 业务能力接进 Hermes

### 目标
让 Hermes 不只是回答，而是能真正调业务能力。

### 推荐步骤
1. 先选 2-3 个最稳定的查询类能力：
   - 查询订单
   - 查询用户
   - 查询审批状态
2. 用 Spring Boot 暴露成 MCP 风格能力
3. 在 Hermes 配置 MCP Server
4. 让 Hermes 发现这些工具
5. 先从只读能力开始验证
6. 等稳定后，再逐步接审批动作、状态更新和更复杂业务操作

## 第五阶段：建立团队规范

需要明确这些规范：

1. 哪些业务能力可以暴露给 Hermes
2. 哪些只能查询，哪些允许修改
3. Spring Boot 调 Hermes 统一走哪个入口
4. 事件接入统一如何定义 route
5. MCP 工具命名和参数规范怎么定
6. 会话 id 如何映射业务 id
7. 日志和排障怎么统一

## 第六阶段：推荐的最终落地形态

### Spring Boot
负责：
- 用户、订单、审批、权限、审计、数据、规则

### Hermes
负责：
- 智能交互
- 任务理解
- 外部能力编排
- 多轮上下文
- 工具选择和结果整合

### 交互路径
- 同步请求：API Server
- 事件请求：Webhook
- 反向能力调用：MCP

## 第七阶段：团队落地时最容易犯的错

1. 一开始就想全都接
2. 把业务规则写进 Hermes
3. 把 Agent 编排写进 Spring Boot
4. 一开始就暴露高风险写操作给 Hermes
5. 不建立统一会话和日志规范

## 第八阶段：一句话落地建议

**先用 API Server 跑通 Spring Boot 调 Hermes，再用 Webhook 接入业务事件，最后用 MCP 把 Spring Boot 的核心业务能力暴露给 Hermes，逐步形成“Spring Boot 稳定业务层 + Hermes 智能编排层”的结构。**

## 延伸阅读

- [09-Hermes-外部直接交互控制入口与-Spring-Boot-实践](./09-Hermes-外部直接交互控制入口与-Spring-Boot-实践.md)
- [14-Hermes-与-Spring-Boot-三种接入方式对照表](./14-Hermes-与-Spring-Boot-三种接入方式对照表.md)
- [example-spring-integration-modes/README.md](../../example-spring-integration-modes/README.md)
