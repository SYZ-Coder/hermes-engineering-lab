# Hermes 与 Spring Boot 的推荐企业架构图

最推荐的企业架构，不是把 Hermes 硬塞进 Spring Boot 里，而是：

**Spring Boot 继续做稳定业务层，Hermes 作为独立智能编排层。**

## 推荐企业总体架构图

```mermaid
flowchart TD
    subgraph Client["客户端与业务入口"]
        A1["Web 前端"]
        A2["管理后台"]
        A3["运营平台"]
        A4["客服工作台"]
        A5["自动化任务 / 定时任务"]
    end

    subgraph Gateway["统一接入层"]
        B1["Spring Boot API 网关 / 业务聚合层"]
    end

    subgraph HermesLayer["智能编排层"]
        C1["Hermes API Server"]
        C2["Hermes Webhook"]
        C3["Hermes Agent Runtime"]
        C4["会话系统"]
        C5["上下文治理"]
        C6["工具系统"]
        C7["记忆 / 技能"]
    end

    subgraph BizLayer["业务服务层（Spring Boot 微服务）"]
        D1["用户服务"]
        D2["订单服务"]
        D3["审批服务"]
        D4["工单服务"]
        D5["报表服务"]
        D6["知识服务"]
    end

    subgraph Infra["基础设施层"]
        E1["MySQL / PostgreSQL"]
        E2["Redis"]
        E3["MQ / Kafka"]
        E4["对象存储 / 文件存储"]
        E5["监控 / 日志 / 审计"]
    end

    A1 --> B1
    A2 --> B1
    A3 --> B1
    A4 --> B1
    A5 --> B1

    B1 --> C1
    B1 --> C2

    C1 --> C3
    C2 --> C3
    C3 --> C4
    C3 --> C5
    C3 --> C6
    C3 --> C7

    C6 --> D1
    C6 --> D2
    C6 --> D3
    C6 --> D4
    C6 --> D5
    C6 --> D6

    D1 --> E1
    D2 --> E1
    D3 --> E1
    D4 --> E1
    D5 --> E1
    D6 --> E4

    B1 --> E2
    D1 --> E2
    D2 --> E2
    D3 --> E3
    D4 --> E3
    C3 --> E5
    B1 --> E5
```

## 核心理解

1. 前端不要直接大面积碰 Hermes，更推荐先走 Spring Boot。
2. Hermes 放在“智能编排层”，最适合负责任务理解、会话和工具编排。
3. Spring Boot 微服务继续做业务层，承接订单、用户、审批、工单等正式业务能力。
4. Hermes 通过工具系统回连业务服务，推荐优先使用 MCP 或受控服务接口。

## 一句话版建议

**前端和业务入口先走 Spring Boot，Hermes 作为独立智能编排层接在 Spring Boot 之后，再通过 MCP 或受控服务接口回连 Spring Boot 业务微服务。**
