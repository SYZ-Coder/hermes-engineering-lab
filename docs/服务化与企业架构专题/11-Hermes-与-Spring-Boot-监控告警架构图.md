# Hermes 与 Spring Boot 监控告警架构图

最推荐的做法是：

**Spring Boot、Hermes API、Hermes Worker、业务微服务统一接入同一套监控与日志平台；指标、日志、链路、审计分层采集，最后统一汇聚到告警中心。**

## 推荐的监控告警总体架构图

```mermaid
flowchart TD
    subgraph Client["用户与入口"]
        A1["前端"]
        A2["管理后台"]
        A3["第三方事件源"]
    end

    subgraph App["应用层"]
        B1["Spring Boot 集群"]
        B2["Hermes API 集群"]
        B3["Hermes Worker 集群"]
        B4["业务微服务集群"]
    end

    subgraph State["状态与中间件"]
        C1["MySQL / PostgreSQL"]
        C2["Redis"]
        C3["MQ / Kafka / Stream"]
        C4["Session Store"]
    end

    subgraph Observe["可观测性平台"]
        D1["Metrics 指标采集"]
        D2["Log 日志采集"]
        D3["Trace 调用链追踪"]
        D4["Audit 审计日志"]
    end

    subgraph Alert["告警与响应"]
        E1["告警规则引擎"]
        E2["通知渠道：钉钉/企业微信/邮件/短信"]
        E3["值班 / 工单 / OnCall"]
    end

    A1 --> B1
    A2 --> B1
    A3 --> B2

    B1 --> B2
    B2 --> B3
    B1 --> B4
    B3 --> B4

    B1 --> C1
    B1 --> C2
    B2 --> C4
    B3 --> C4
    B3 --> C3
    B4 --> C1
    B4 --> C2

    B1 --> D1
    B2 --> D1
    B3 --> D1
    B4 --> D1
    C1 --> D1
    C2 --> D1
    C3 --> D1
    C4 --> D1

    B1 --> D2
    B2 --> D2
    B3 --> D2
    B4 --> D2

    B1 --> D3
    B2 --> D3
    B3 --> D3
    B4 --> D3

    B1 --> D4
    B2 --> D4
    B3 --> D4
    B4 --> D4

    D1 --> E1
    D2 --> E1
    D3 --> E1
    D4 --> E1

    E1 --> E2
    E1 --> E3
```

## 核心理解

1. 指标监控看系统健康。
2. 日志看发生了什么。
3. 调用链看问题卡在哪一跳。
4. 审计看谁做了什么。
5. 告警建议至少分可用性、性能、错误率、任务积压和安全审计 5 类。

## 一句话总结

**所有层都接入统一监控平台，最后由统一告警中心做通知和响应。**
