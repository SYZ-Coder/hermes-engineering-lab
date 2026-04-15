# 团队学习图谱：Hermes 架构流程与通讯时序

说明：

- 本文档把本轮学习过程中涉及到的 **流程图、时序图、架构图** 统一收拢，方便团队集中查看。
- 图主要围绕当前项目保留下来的主题：**Hermes 核心架构、工具调用、子代理机制、外部服务通讯、Spring Boot 对接**。
- 所有图均使用 Mermaid 表达，后续团队可以继续在此基础上追加。

---

## 1. Hermes 总体架构图

```mermaid
flowchart LR
    U[用户] --> CLI[命令行入口 CLI]
    U --> GW[消息网关入口 Gateway]

    CLI --> AG[AIAgent 核心运行时\nrun_agent.py]
    GW --> AG

    AG --> PB[提示词构建器\nagent/prompt_builder.py]
    AG --> RT[运行时提供者解析\nhermes_cli/runtime_provider.py]
    AG --> MT[工具编排层\nmodel_tools.py]
    AG --> CC[上下文压缩器\nagent/context_compressor.py]
    AG --> PC[提示缓存层\nagent/prompt_caching.py]
    AG --> DB[会话存储层\nhermes_state.py]

    MT --> TS[工具集合配置\ntoolsets.py]
    MT --> REG[工具注册中心\ntools/registry.py]
    REG --> TOOLS[具体工具实现\ntools/*.py]
    MT --> MCP[MCP 工具发现]
    MT --> PLUG[插件发现]
```

---

## 2. Hermes 一次完整执行流程图

```mermaid
flowchart TD
    A[用户输入] --> B[CLI 或 Gateway 接收请求]
    B --> C[创建或调用 AIAgent]
    C --> D[构建系统提示词]
    D --> E[解析模型提供方与 API 模式]
    E --> F[加载当前可用工具定义]
    F --> G[发起模型调用]

    G --> H{模型是否返回工具调用}
    H -- 否 --> I[输出最终回答]
    H -- 是 --> J[执行工具]
    J --> K[将工具结果回填到消息历史]
    K --> G

    I --> L[写入 SQLite 会话存储]
```

---

## 3. Hermes 工具体系架构图

```mermaid
flowchart TD
    A[model_tools.py] --> B[发现工具 _discover_tools]
    B --> C[导入 tools/*.py]
    C --> D[调用 registry.register]
    D --> E[工具注册中心]

    A --> F[解析工具集合 toolsets]
    F --> G[toolsets.py]

    A --> H[生成工具定义 get_tool_definitions]
    H --> E
    H --> I[按 toolset 和 check_fn 过滤]
    I --> J[返回给模型的工具 schema]

    A --> K[分发工具调用 handle_function_call]
    K --> E
    E --> L[执行具体工具 handler]
```

---

## 4. Hermes 上下文治理架构图

```mermaid
flowchart LR
    M[历史消息 messages] --> A[AIAgent]
    A --> B[提示词构建器]
    A --> C[上下文压缩器]
    A --> D[提示缓存器]

    B --> E[系统提示词]
    C --> F[压缩后的上下文]
    D --> G[缓存控制标记]

    E --> H[最终发送给模型的消息]
    F --> H
    G --> H
    H --> I[模型调用]
```

---

## 5. Hermes 会话与记忆架构图

```mermaid
flowchart TD
    A[AIAgent] --> B[记忆工具 memory]
    A --> C[会话检索 session_search]
    A --> D[会话数据库 SessionDB]

    D --> E[SQLite state.db]
    E --> F[sessions 表]
    E --> G[messages 表]
    E --> H[messages_fts 全文索引]

    C --> H
    B --> I[MEMORY.md / USER.md]
```

---

## 6. Hermes 多入口统一运行时图

```mermaid
flowchart LR
    A[CLI] --> X[统一 Agent 运行时]
    B[Telegram] --> X
    C[Discord] --> X
    D[Slack] --> X
    E[WhatsApp] --> X
    F[Signal] --> X
    G[Email] --> X

    X --> H[统一工具系统]
    X --> I[统一记忆系统]
    X --> J[统一提示词 压缩 缓存]
    X --> K[统一会话持久化]
```

---

## 7. Hermes 架构分层图

```mermaid
flowchart TD
    subgraph L1[第一层 接入层]
        A1[CLI]
        A2[消息网关]
        A3[定时任务 其他入口]
    end

    subgraph L2[第二层 Agent 运行时层]
        B1[AIAgent]
        B2[运行时提供者解析]
        B3[提示词构建]
    end

    subgraph L3[第三层 能力编排层]
        C1[工具编排 model_tools]
        C2[工具集合 toolsets]
        C3[上下文压缩]
        C4[提示缓存]
    end

    subgraph L4[第四层 能力实现层]
        D1[tools/*.py]
        D2[MCP 工具]
        D3[插件能力]
        D4[记忆工具]
        D5[浏览器 终端 文件 代码执行]
    end

    subgraph L5[第五层 状态与存储层]
        E1[SessionDB]
        E2[SQLite state.db]
        E3[FTS5 全文检索]
        E4[MEMORY.md / USER.md]
    end

    L1 --> L2
    L2 --> L3
    L3 --> L4
    L2 --> L5
    L4 --> L5
```

---

## 8. Hermes 核心模块依赖图

```mermaid
flowchart TD
    A[hermes_cli/main.py] --> B[cli.py]
    A --> C[gateway/run.py]

    B --> D[run_agent.py]
    C --> D

    D --> E[agent/prompt_builder.py]
    D --> F[hermes_cli/runtime_provider.py]
    D --> G[model_tools.py]
    D --> H[agent/context_compressor.py]
    D --> I[agent/prompt_caching.py]
    D --> J[hermes_state.py]

    G --> K[toolsets.py]
    G --> L[tools/registry.py]

    L --> M[tools/web_tools.py]
    L --> N[tools/file_tools.py]
    L --> O[tools/terminal_tool.py]
    L --> P[tools/browser_tool.py]
    L --> Q[tools/code_execution_tool.py]
    L --> R[tools/delegate_tool.py]
    L --> S[其他 tools/*.py]
```

---

## 9. Hermes 单次工具调用时序图

```mermaid
sequenceDiagram
    participant 用户
    participant 入口 as CLI / Gateway
    participant Agent as AIAgent
    participant 工具编排 as model_tools.py
    participant 注册中心 as tools/registry.py
    participant 模型
    participant 工具 as 具体工具 handler

    用户->>入口: 发送消息
    入口->>Agent: run_conversation()

    Agent->>工具编排: get_tool_definitions()
    工具编排->>注册中心: 获取已注册工具 schema
    注册中心-->>工具编排: 返回可用工具定义
    工具编排-->>Agent: 返回 tools schema

    Agent->>模型: 发送 messages + tools
    模型-->>Agent: 返回 tool_calls

    Agent->>工具编排: handle_function_call(name, args)
    工具编排->>注册中心: dispatch(name, args)
    注册中心->>工具: 执行具体 handler
    工具-->>注册中心: 返回 JSON 结果
    注册中心-->>工具编排: 返回工具结果
    工具编排-->>Agent: 返回工具结果

    Agent->>模型: 追加 tool result 后再次请求
    模型-->>Agent: 返回最终回答
    Agent-->>入口: 输出结果
```

---

## 10. Hermes 工具发现与暴露时序图

```mermaid
sequenceDiagram
    participant 启动 as Hermes 启动阶段
    participant 工具编排 as model_tools.py
    participant 工具模块 as tools/*.py
    participant 注册中心 as tools/registry.py
    participant 工具集合 as toolsets.py
    participant Agent as AIAgent
    participant 模型

    启动->>工具编排: 调用 _discover_tools()
    工具编排->>工具模块: 逐个 import 工具模块
    工具模块->>注册中心: registry.register(...)
    注册中心-->>工具模块: 注册完成

    Agent->>工具编排: get_tool_definitions()
    工具编排->>工具集合: 解析 enabled / disabled toolsets
    工具集合-->>工具编排: 返回允许的工具名集合
    工具编排->>注册中心: 获取对应 schema
    注册中心-->>工具编排: 返回 schema 列表
    工具编排-->>Agent: 返回当前轮可见工具
    Agent->>模型: 带 tools schema 发起请求
```

---

## 11. Hermes 工具调用循环时序图

```mermaid
sequenceDiagram
    participant 用户
    participant Agent as AIAgent
    participant 模型
    participant 工具编排 as model_tools.py
    participant 注册中心 as registry
    participant 工具 as tools handler

    用户->>Agent: 用户消息
    Agent->>模型: 第一次请求

    loop 当模型持续返回工具调用时
        模型-->>Agent: tool_calls
        Agent->>工具编排: handle_function_call(...)
        工具编排->>注册中心: dispatch(...)
        注册中心->>工具: 执行工具
        工具-->>注册中心: 工具结果
        注册中心-->>工具编排: 返回结果
        工具编排-->>Agent: 返回结果
        Agent->>模型: 追加工具结果后再次请求
    end

    模型-->>Agent: 最终自然语言回答
    Agent-->>用户: 返回最终结果
```

---

## 12. Hermes 普通工具调用 vs delegate_task 对照图

```mermaid
sequenceDiagram
    participant 用户
    participant 主代理 as 主 AIAgent
    participant 模型
    participant 普通工具 as 普通工具系统
    participant 委托工具 as delegate_task
    participant 子代理 as 子 AIAgent

    用户->>主代理: 提出任务
    主代理->>模型: 发起请求

    alt 普通工具调用
        模型-->>主代理: 请求普通工具
        主代理->>普通工具: 直接执行工具
        普通工具-->>主代理: 返回工具结果
        主代理->>模型: 回填结果继续推理
    else delegate_task 调用
        模型-->>主代理: 请求 delegate_task
        主代理->>委托工具: 执行委托
        委托工具->>子代理: 创建新的子代理
        子代理-->>委托工具: 返回子任务结果
        委托工具-->>主代理: 返回子代理结果
        主代理->>模型: 回填结果继续推理
    end
```

---

## 13. Hermes delegate_task 基础调用时序图

```mermaid
sequenceDiagram
    participant 用户
    participant 主代理 as 主 AIAgent
    participant 模型
    participant 委托工具 as delegate_task
    participant 子代理 as 子 AIAgent
    participant 子模型 as 子代理模型

    用户->>主代理: 提出复杂任务
    主代理->>模型: 发起推理
    模型-->>主代理: 返回 delegate_task 调用请求

    主代理->>委托工具: 执行 delegate_task(goal, context, toolsets)
    委托工具->>子代理: 创建子 AIAgent
    子代理->>子模型: 以 delegated goal 启动独立对话循环

    loop 子代理独立执行
        子模型-->>子代理: 工具调用或文本输出
        子代理->>子代理: 执行自己的工具循环
    end

    子代理-->>委托工具: 返回子任务结果
    委托工具-->>主代理: 返回聚合后的 delegate_task 结果

    主代理->>模型: 将子代理结果回填后继续推理
    模型-->>主代理: 给出最终回答
    主代理-->>用户: 输出结果
```

---

## 14. Hermes delegate_task 与 OpenClaw 多 agent 机制对照图

```mermaid
flowchart LR
    subgraph Hermes[Hermes delegate_task]
        H1[主代理] --> H2[delegate_task 工具]
        H2 --> H3[创建子代理]
        H3 --> H4[子代理独立执行子任务]
        H4 --> H3
        H3 --> H2
        H2 --> H1
    end

    subgraph OpenClaw[OpenClaw 多 agent 机制]
        O1[Gateway] --> O2[路由绑定 bindings]
        O2 --> O3[目标 agentId]
        O3 --> O4[该 agent 的独立工作区 agentDir sessions]
        O4 --> O5[该 agent 执行请求]
    end
```

---

## 15. Hermes 对外通讯总图

```mermaid
flowchart LR
    subgraph 外部系统
        A[Spring Boot]
        B[GitHub GitLab JIRA Stripe]
        C[Open WebUI LobeChat 其他 OpenAI 兼容前端]
        D[MCP 外部服务]
        E[Telegram Slack Twilio 其他平台 API]
    end

    subgraph Hermes
        H1[API Server\ngateway/platforms/api_server.py]
        H2[Webhook Adapter\ngateway/platforms/webhook.py]
        H3[Gateway 平台层]
        H4[AIAgent]
        H5[MCP Client\ntools/mcp_tool.py]
        H6[工具系统]
    end

    A --> H1
    A --> H2
    C --> H1
    B --> H2
    H4 --> H5
    H5 --> D
    H3 --> E
    H1 --> H3
    H2 --> H3
    H3 --> H4
    H4 --> H6
```

---

## 16. 外部请求 Hermes 的架构图

```mermaid
flowchart TD
    subgraph 外部请求进入 Hermes
        A1[Spring Boot 前端 业务系统] --> A2[API Server]
        A3[GitHub GitLab Stripe 事件源] --> A4[Webhook Adapter]
        A5[编辑器 ACP 客户端] --> A6[ACP Adapter]
    end

    A2 --> B[Gateway 层]
    A4 --> B
    A6 --> C[ACP Agent Server]

    B --> D[AIAgent]
    C --> D
    D --> E[工具系统]
    D --> F[会话系统]
```

---

## 17. Hermes 主动访问外部服务的架构图

```mermaid
flowchart TD
    A[AIAgent] --> B{调用哪类外部能力}

    B -- 通用业务扩展 --> C[MCP Client]
    B -- 网页信息 --> D[Web Tools]
    B -- 浏览器自动化 --> E[Browser Tool]
    B -- 消息平台 --> F[Gateway 平台适配器]
    B -- 其他插件能力 --> G[Plugin Discovery Plugin Hooks]

    C --> H[Spring Boot MCP 服务 其他 MCP 服务]
    D --> I[外部网站 搜索服务]
    E --> J[浏览器驱动 外部网页]
    F --> K[Telegram Slack Twilio 平台 API]
    G --> L[插件实现的外部集成逻辑]
```

---

## 18. Spring Boot 接入 Hermes 方案总览图

```mermaid
flowchart TD
    A[Spring Boot 服务] --> B{你希望哪种方向}

    B -- Spring Boot 请求 Hermes --> C[方案 1 调用 Hermes API Server]
    B -- Spring Boot 事件推送给 Hermes --> D[方案 2 调用 Hermes Webhook]
    B -- Hermes 主动调用 Spring Boot --> E[方案 3 Spring Boot 作为 MCP 服务]
```

---

## 19. Spring Boot 调用 Hermes API Server 时序图

```mermaid
sequenceDiagram
    participant SpringBoot as Spring Boot
    participant HermesAPI as Hermes API Server
    participant Gateway as Gateway 平台适配层
    participant Agent as AIAgent
    participant Tools as 工具系统

    SpringBoot->>HermesAPI: POST /v1/chat/completions 或 /v1/responses
    HermesAPI->>Gateway: 进入 api_server 平台适配器
    Gateway->>Agent: 创建恢复会话并执行请求
    Agent->>Tools: 需要时调用工具
    Tools-->>Agent: 返回工具结果
    Agent-->>Gateway: 返回最终结果
    Gateway-->>HermesAPI: 组织 OpenAI 兼容响应
    HermesAPI-->>SpringBoot: 返回 HTTP JSON 结果
```

---

## 20. Spring Boot 推送事件到 Hermes Webhook 时序图

```mermaid
sequenceDiagram
    participant SpringBoot as Spring Boot
    participant Webhook as Hermes Webhook Adapter
    participant Gateway as Gateway
    participant Agent as AIAgent
    participant Delivery as 回传通道

    SpringBoot->>Webhook: POST /webhooks/{route_name}
    Webhook->>Webhook: 校验 HMAC 路由 限流 幂等
    Webhook->>Gateway: 将 payload 转成 agent prompt
    Gateway->>Agent: 执行任务
    Agent-->>Gateway: 生成结果
    Gateway-->>Webhook: 返回 agent 输出
    Webhook->>Delivery: 按 deliver 配置回传
```

---

## 21. Hermes 通过 MCP 调用 Spring Boot 时序图

```mermaid
sequenceDiagram
    participant Hermes as Hermes Agent
    participant MCPClient as Hermes MCP Client
    participant SpringMCP as Spring Boot MCP 服务
    participant Registry as 工具注册中心
    participant Model as 模型

    Hermes->>MCPClient: 启动时读取 mcp_servers 配置
    MCPClient->>SpringMCP: 连接 HTTP StreamableHTTP MCP 服务
    SpringMCP-->>MCPClient: 返回可用工具列表
    MCPClient->>Registry: 注册外部 MCP 工具
    Registry-->>Hermes: 外部工具进入可调用工具面

    Model->>Hermes: 请求调用某个 MCP 工具
    Hermes->>MCPClient: 执行 MCP 工具调用
    MCPClient->>SpringMCP: 发起工具请求
    SpringMCP-->>MCPClient: 返回结果
    MCPClient-->>Hermes: 返回工具结果
    Hermes-->>Model: 回填结果继续推理
```

---

## 22. example-spring-client 调用链路图

```mermaid
flowchart LR
    A[客户端] --> B[HermesDemoController]
    B --> C[HermesApiService RestTemplate]
    B --> D[HermesWebClientService WebClient]
    C --> E[Hermes API Server 或 Webhook]
    D --> E
    E --> F[Hermes Agent]
```

---

## 23. example-spring-mcp-server 工具调用图

```mermaid
flowchart TD
    A[Hermes MCP Client] --> B[POST /mcp]
    B --> C[McpController]
    C --> D{method}
    D -- initialize --> E[initialize]
    D -- tools/list --> F[toolsList]
    D -- tools/call --> G[toolsCall]
    D -- ping --> H[ping]

    G --> I{tool name}
    I -- query_order --> J[查询订单表]
    I -- query_user --> K[查询用户表]
    I -- approve_order --> L[更新订单状态与审批人]
```

---

## 24. example-spring-mcp-server 数据状态变化图

```mermaid
sequenceDiagram
    participant Hermes as Hermes 或调用方
    participant MCP as MCP Server
    participant Orders as 订单内存表
    participant Users as 用户内存表

    Hermes->>MCP: tools/call query_order A10086
    MCP->>Orders: 查询 A10086
    Orders-->>MCP: userId U20001 status PENDING_APPROVAL
    MCP->>Users: 查询 U20001
    Users-->>MCP: Alice VIP LOW
    MCP-->>Hermes: 返回订单 用户关联结果

    Hermes->>MCP: tools/call approve_order A10086 finance_manager
    MCP->>Orders: 更新状态 APPROVED 更新审批人
    Orders-->>MCP: 写入成功
    MCP-->>Hermes: 返回审批成功结果

    Hermes->>MCP: tools/call query_order A10086
    MCP->>Orders: 再次查询 A10086
    Orders-->>MCP: status APPROVED approvedBy finance_manager
    MCP-->>Hermes: 返回更新后的订单结果
```

---

## 25. 使用建议

团队后续建议这样使用这份图谱文档：

1. 看 Hermes 总体设计时，先看第 1 到第 8 张图
2. 看工具和子代理机制时，重点看第 9 到第 14 张图
3. 看外部通讯和 Spring 对接时，重点看第 15 到第 24 张图
4. 后续新增图时，尽量按“核心架构 / 工具机制 / 外部通讯 / 示例工程”四组继续追加