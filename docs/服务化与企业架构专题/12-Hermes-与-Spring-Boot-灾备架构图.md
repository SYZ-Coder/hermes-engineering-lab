# Hermes 与 Spring Boot 灾备架构图

最推荐的灾备思路是：

**应用双活或主备、状态多副本、会话与审计外置、对象存储跨区域复制、消息队列可恢复、数据库主从或多活，并通过流量切换能力把 Spring Boot 和 Hermes 一起做成可切换的整体。**

## 推荐的灾备总体架构图

```mermaid
flowchart TD
    subgraph RegionA["主区域 / 主机房"]
        A1["SLB / API Gateway"]
        A2["Spring Boot 集群"]
        A3["Hermes API 集群"]
        A4["Hermes Worker 集群"]
        A5["主数据库"]
        A6["Redis / Cache"]
        A7["MQ / Stream"]
        A8["Session Store"]
        A9["日志 / 审计存储"]
        A10["对象存储"]
    end

    subgraph RegionB["灾备区域 / 备机房"]
        B1["SLB / API Gateway"]
        B2["Spring Boot 备集群"]
        B3["Hermes API 备集群"]
        B4["Hermes Worker 备集群"]
        B5["备数据库"]
        B6["Redis / Cache 副本"]
        B7["MQ / Stream 副本"]
        B8["Session Store 副本"]
        B9["日志 / 审计副本"]
        B10["对象存储副本"]
    end

    subgraph Global["全局流量与治理"]
        C1["DNS / GSLB / 全局流量调度"]
        C2["健康检查"]
        C3["故障切换控制"]
    end

    Client["用户 / 前端 / 第三方事件"] --> C1
    C1 --> A1
    C1 --> B1

    A1 --> A2
    A2 --> A3
    A3 --> A4

    B1 --> B2
    B2 --> B3
    B3 --> B4

    A2 --> A5
    A2 --> A6
    A3 --> A8
    A4 --> A7
    A4 --> A8
    A3 --> A9
    A4 --> A9
    A4 --> A10

    B2 --> B5
    B2 --> B6
    B3 --> B8
    B4 --> B7
    B4 --> B8
    B3 --> B9
    B4 --> B9
    B4 --> B10

    A5 --> B5
    A6 --> B6
    A7 --> B7
    A8 --> B8
    A9 --> B9
    A10 --> B10

    C2 --> A1
    C2 --> A2
    C2 --> A3
    C2 --> A4
    C2 --> B1
    C2 --> B2
    C2 --> B3
    C2 --> B4

    C3 --> C1
```

## 核心理解

1. 灾备不是只备 Spring Boot，也要备 Hermes。
2. Hermes 的状态必须外置，否则灾备切换很难做。
3. 主备切换要把“应用 + 状态 + 流量”一起切。
4. Session、Run、MQ、日志和对象存储都应该进入灾备范围。

## 一句话总结

**主区域负责正常服务，灾备区域保持可接管，状态持续复制，故障时通过全局流量调度整体切换。**
