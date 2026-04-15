# Hermes 与 Spring Boot 多租户隔离架构图

最推荐的做法是：

**由 Spring Boot 负责租户识别和最终数据隔离，Hermes 负责会话、工具、记忆和上下文的租户感知与范围收敛。**

## 推荐的多租户总体架构图

```mermaid
flowchart TD
    subgraph Client["用户与入口"]
        A1["租户A 用户"]
        A2["租户B 用户"]
        A3["运营后台"]
        A4["客服工作台"]
    end

    subgraph Access["接入与租户识别层"]
        B1["Spring Boot API 网关"]
        B2["认证中心"]
        B3["租户识别"]
        B4["租户上下文注入"]
    end

    subgraph HermesLayer["Hermes 智能层"]
        C1["Hermes API Server / Webhook"]
        C2["Hermes Agent Runtime"]
        C3["会话隔离"]
        C4["工具范围控制"]
        C5["记忆 / 技能 / 上下文隔离"]
    end

    subgraph BizLayer["Spring Boot 业务层"]
        D1["用户服务"]
        D2["订单服务"]
        D3["审批服务"]
        D4["知识服务"]
        D5["租户级数据访问控制"]
    end

    subgraph Data["数据层"]
        E1["租户A数据"]
        E2["租户B数据"]
        E3["共享基础数据"]
        E4["审计日志"]
    end

    A1 --> B1
    A2 --> B1
    A3 --> B1
    A4 --> B1

    B1 --> B2
    B2 --> B3
    B3 --> B4

    B4 --> C1
    C1 --> C2
    C2 --> C3
    C2 --> C4
    C2 --> C5

    C4 --> D1
    C4 --> D2
    C4 --> D3
    C4 --> D4
    D1 --> D5
    D2 --> D5
    D3 --> D5
    D4 --> D5

    D5 --> E1
    D5 --> E2
    D5 --> E3
    C2 --> E4
    D5 --> E4
```

## 核心理解

1. 租户识别先在 Spring Boot 做。
2. Hermes 负责“租户感知”，不是租户裁决中心。
3. 最终数据隔离仍在 Spring Boot 业务层做。
4. 会话、记忆、工具范围和上下文都建议按租户做隔离。

## 一句话总结

**租户先识别，再智能处理，最后由业务层做最终隔离。**
