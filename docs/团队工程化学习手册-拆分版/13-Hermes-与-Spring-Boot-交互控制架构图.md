# Hermes 与 Spring Boot 交互控制架构图

如果团队要真正把 Hermes 和 Spring Boot 接起来，最重要的不是先看代码，而是先把交互方向看清楚。

### 1. 总体交互控制图

```mermaid
flowchart LR
    subgraph SpringBoot["Spring Boot 业务系统"]
        SB1["业务接口"]
        SB2["事件生产者"]
        SB3["业务能力服务"]
    end

    subgraph Hermes["Hermes"]
        H1["API Server"]
        H2["Webhook"]
        H3["ACP"]
        H4["统一 Agent Runtime"]
        H5["工具系统"]
        H6["MCP Client"]
        H7["会话 / 记忆 / 上下文治理"]
    end

    SB1 --> H1
    SB2 --> H2
    H1 --> H4
    H2 --> H4
    H3 --> H4

    H4 --> H5
    H4 --> H7
    H4 --> H6
    H6 --> SB3
```

### 2. Spring Boot -> Hermes 的服务化调用架构图

```mermaid
flowchart TD
    A["Spring Boot Controller / Service"] --> B["HTTP Client"]
    B --> C["Hermes API Server"]
    C --> D["Hermes Gateway API Adapter"]
    D --> E["AIAgent"]
    E --> F["工具系统"]
    E --> G["会话系统"]
    E --> H["上下文治理"]
    E --> I["返回最终结果给 Spring Boot"]
```

### 3. Spring Boot -> Hermes 的事件驱动架构图

```mermaid
flowchart TD
    A["Spring Boot 业务事件"] --> B["Webhook POST"]
    B --> C["Hermes Webhook Adapter"]
    C --> D["校验 / 限流 / 幂等 / 路由"]
    D --> E["事件转 Prompt"]
    E --> F["AIAgent 执行"]
    F --> G["结果投递 / 回传"]
```

### 4. Hermes -> Spring Boot 的能力调用架构图

```mermaid
flowchart TD
    A["Hermes AIAgent"] --> B["MCP Client"]
    B --> C["Spring Boot MCP 服务"]
    C --> D["订单服务 / 用户服务 / 审批服务 / 知识服务"]
    D --> C
    C --> B
    B --> A
```

### 5. 最终推荐架构图

```mermaid
flowchart LR
    subgraph 上层入口
        U1["前端 / 管理后台"]
        U2["业务事件"]
        U3["运营 / 客服 / 自动化任务"]
    end

    subgraph SpringBoot
        S1["统一业务入口层"]
        S2["现有微服务 / 中台"]
        S3["MCP 能力暴露层"]
    end

    subgraph Hermes
        H1["API Server / Webhook"]
        H2["统一 Agent Runtime"]
        H3["工具 / 会话 / 记忆 / 上下文治理"]
    end

    U1 --> S1
    U2 --> S1
    U3 --> S1

    S1 --> H1
    H1 --> H2
    H2 --> H3
    H2 --> S3
    S3 --> S2
```

一句话解释：

**Spring Boot 保持业务中心不变，Hermes 作为智能编排层接进来，再通过 MCP 回连到 Spring Boot 的业务能力。**

---
