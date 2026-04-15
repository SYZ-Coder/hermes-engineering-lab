# Hermes 与 Spring Boot 权限与审计架构图

最推荐的做法是：

**权限主控仍然放在 Spring Boot 业务层，Hermes 不直接成为权限中心；Hermes 负责记录“智能决策过程”，Spring Boot 负责记录“正式业务操作审计”。**

## 推荐权限与审计总体架构图

```mermaid
flowchart TD
    subgraph Client["用户与入口"]
        A1["前端用户"]
        A2["运营后台"]
        A3["客服工作台"]
        A4["自动化任务"]
    end

    subgraph Access["接入与权限主控层"]
        B1["Spring Boot API 网关"]
        B2["认证鉴权"]
        B3["RBAC / ABAC 权限中心"]
        B4["租户 / 组织隔离"]
    end

    subgraph HermesLayer["Hermes 智能编排层"]
        C1["Hermes API Server / Webhook"]
        C2["Hermes Agent Runtime"]
        C3["会话系统"]
        C4["工具调用决策"]
        C5["智能审计日志"]
    end

    subgraph BizLayer["Spring Boot 业务服务层"]
        D1["用户服务"]
        D2["订单服务"]
        D3["审批服务"]
        D4["工单服务"]
        D5["知识服务"]
        D6["业务审计日志"]
    end

    subgraph Infra["基础设施"]
        E1["身份系统 / SSO / OAuth2"]
        E2["数据库"]
        E3["日志平台 / SIEM"]
        E4["审计存储"]
        E5["告警系统"]
    end

    A1 --> B1
    A2 --> B1
    A3 --> B1
    A4 --> B1

    B1 --> B2
    B2 --> B3
    B3 --> B4

    B1 --> C1
    C1 --> C2
    C2 --> C3
    C2 --> C4
    C2 --> C5

    C4 --> D1
    C4 --> D2
    C4 --> D3
    C4 --> D4
    C4 --> D5

    D1 --> D6
    D2 --> D6
    D3 --> D6
    D4 --> D6
    D5 --> D6

    B2 --> E1
    D1 --> E2
    D2 --> E2
    D3 --> E2
    D4 --> E2
    D5 --> E2

    C5 --> E3
    D6 --> E3
    C5 --> E4
    D6 --> E4
    E3 --> E5
```

## 权限边界一句话版

**Hermes 可以感知权限上下文，但 Spring Boot 负责最终权限裁决。**

## 审计边界一句话版

**Hermes 记录“智能过程”，Spring Boot 记录“正式业务操作”。**
