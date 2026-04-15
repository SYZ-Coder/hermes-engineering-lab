# Spring Boot 调 Hermes 的完整调用链路图

下面这张图最适合团队理解整体路径。

## 最常见同步调用链路图

```mermaid
flowchart TD
    A["前端 / 业务请求"] --> B["Spring Boot Controller"]
    B --> C["Spring Boot Service"]
    C --> D["HTTP Client"]
    D --> E["Hermes API Server"]

    E --> F["Hermes 接口适配层"]
    F --> G["统一 Agent Runtime"]
    G --> H["会话系统"]
    G --> I["上下文治理"]
    G --> J["工具系统"]
    G --> K["记忆 / 技能"]

    J --> L["如果需要则执行工具"]
    L --> G
    G --> M["生成最终回答"]

    M --> E
    E --> D
    D --> C
    C --> B
    B --> A
```

这张图的意思是：

- Spring Boot 并不是直接调模型
- 而是调 Hermes 的服务入口
- Hermes 内部再统一进入 Agent Runtime
- Agent Runtime 再决定要不要用工具、会话、记忆等能力

## 更细一点的内部调用链路图

```mermaid
sequenceDiagram
    participant Client as 前端/业务方
    participant SB as Spring Boot
    participant API as Hermes API Server
    participant Agent as Hermes Agent Runtime
    participant Tools as 工具系统
    participant Model as 大模型

    Client->>SB: 发起业务请求
    SB->>API: POST /v1/chat/completions
    API->>Agent: 转成一次 Agent 执行

    Agent->>Agent: 恢复/创建会话
    Agent->>Agent: 组装 system prompt
    Agent->>Agent: 注入上下文/记忆/技能
    Agent->>Agent: 解析当前可用工具
    Agent->>Model: 发起模型请求

    alt 模型需要工具
        Model-->>Agent: 返回工具调用请求
        Agent->>Tools: 执行工具
        Tools-->>Agent: 返回工具结果
        Agent->>Model: 回填工具结果继续推理
    else 直接回答
        Model-->>Agent: 返回最终文本
    end

    Agent-->>API: 返回最终响应
    API-->>SB: HTTP JSON 响应
    SB-->>Client: 返回业务结果
```

这张图最关键的一点是：

**Hermes 内部是“模型推理 + 工具执行 + 再推理”的 Agent 循环，而不是一次裸模型调用。**

## 如果你要做会话连续，链路上要多理解一层

如果 Spring Boot 每次都只发一个独立请求，Hermes 就容易把它当成一次新任务。
如果你想让多轮请求连起来，Spring Boot 需要自己持有并传递：

- 业务会话 id
- 或 Hermes 会话关联信息

这时链路会变成：

```mermaid
flowchart LR
    A["Spring Boot 业务会话ID"] --> B["Hermes Session 映射"]
    B --> C["同一条 Hermes 会话"]
    C --> D["多轮上下文连续"]
```

## 一句话理解整条调用链

**Spring Boot 负责把业务请求送进 Hermes，Hermes 负责把这个请求变成一次完整的 Agent 执行过程，再把最终结果回给 Spring Boot。**
