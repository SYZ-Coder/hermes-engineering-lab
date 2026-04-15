# Hermes 与 Spring Boot 高可用架构图

最推荐的高可用思路是：

**Spring Boot 和 Hermes 都做无状态服务化部署，会话与审计落到外部存储，长任务与事件流走队列或事件通道，前面统一接入网关与负载均衡。**

## 推荐的高可用总体架构图

```mermaid
flowchart TD
    subgraph Client["客户端与入口"]
        A1["Web 前端"]
        A2["管理后台"]
        A3["客服工作台"]
        A4["自动化任务 / 第三方回调"]
    end

    subgraph Access["接入层"]
        B1["SLB / Nginx / API Gateway"]
        B2["认证鉴权"]
        B3["限流 / 熔断 / WAF"]
    end

    subgraph SpringLayer["Spring Boot 高可用集群"]
        C1["Spring Boot 实例 1"]
        C2["Spring Boot 实例 2"]
        C3["Spring Boot 实例 N"]
    end

    subgraph HermesLayer["Hermes 高可用集群"]
        D1["Hermes API Server 1"]
        D2["Hermes API Server 2"]
        D3["Hermes API Server N"]
        D4["Hermes Worker / Long Run Worker"]
    end

    subgraph Async["异步与事件层"]
        E1["MQ / Kafka / Redis Stream"]
        E2["Webhook Event Queue"]
        E3["Run/Event Stream"]
    end

    subgraph Data["共享状态与数据层"]
        F1["业务数据库"]
        F2["Redis / Cache"]
        F3["Hermes Session Store"]
        F4["审计日志 / 日志平台"]
        F5["对象存储 / 文件存储"]
    end

    subgraph Biz["业务微服务层"]
        G1["用户服务"]
        G2["订单服务"]
        G3["审批服务"]
        G4["工单服务"]
        G5["知识服务 / MCP 服务"]
    end

    A1 --> B1
    A2 --> B1
    A3 --> B1
    A4 --> B1

    B1 --> B2
    B2 --> B3
    B3 --> C1
    B3 --> C2
    B3 --> C3

    C1 --> D1
    C2 --> D2
    C3 --> D3

    C1 --> G1
    C2 --> G2
    C3 --> G3

    D1 --> D4
    D2 --> D4
    D3 --> D4

    D1 --> E1
    D2 --> E1
    D3 --> E2
    D4 --> E3

    C1 --> F1
    C2 --> F1
    C3 --> F1

    D1 --> F3
    D2 --> F3
    D3 --> F3
    D4 --> F3

    C1 --> F2
    C2 --> F2
    C3 --> F2

    D1 --> F4
    D2 --> F4
    D3 --> F4
    D4 --> F4
    C1 --> F4
    C2 --> F4
    C3 --> F4

    G1 --> F1
    G2 --> F1
    G3 --> F1
    G4 --> F1
    G5 --> F5

    D4 --> G1
    D4 --> G2
    D4 --> G3
    D4 --> G4
    D4 --> G5
```

## 核心理解

1. Spring Boot 和 Hermes 都做集群，不要单实例。
2. Hermes 尽量无状态化，关键状态必须外置。
3. 长任务和异步任务不要卡死在同步请求里。
4. API 层和 Worker 层建议拆开部署。

## 一句话总结

**前面统一接入，Spring Boot 做业务入口，Hermes 分成 API 集群和 Worker 集群，状态全部外置。**
