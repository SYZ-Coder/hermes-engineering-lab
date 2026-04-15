# 服务化与企业架构专题

这组文档整理自“那么，Hermes 安装之后他是一个服务是嘛？”这段对话之后的全部内容，重点围绕：

- Hermes 服务化接入方式
- Spring Boot 如何请求 Hermes
- Hermes API Server 的理解与使用
- Hermes 与 Spring Boot 的企业架构设计
- 权限、审计、多租户、高可用、监控、灾备

## 目录

1. [Hermes 安装后是不是一个服务，以及 Spring Boot 如何请求](./01-Hermes-安装后是不是一个服务以及-Spring-Boot-如何请求.md)
2. [Hermes API Server 的请求响应结构详细说明](./02-Hermes-API-Server-的请求响应结构详细说明.md)
3. [Spring Boot 调 Hermes 的完整调用链路图](./03-Spring-Boot-调-Hermes-的完整调用链路图.md)
4. [Spring Boot 调 Hermes 的最小请求示例说明](./04-Spring-Boot-调-Hermes-的最小请求示例说明.md)
5. [Hermes API Server 与普通大模型 API 的区别对照表](./05-Hermes-API-Server-与普通大模型-API-的区别对照表.md)
6. [为什么 Hermes 更适合作为独立服务而不是直接嵌进 Spring Boot](./06-为什么-Hermes-更适合作为独立服务而不是直接嵌进-Spring-Boot.md)
7. [Hermes 与 Spring Boot 的推荐企业架构图](./07-Hermes-与-Spring-Boot-的推荐企业架构图.md)
8. [Hermes 与 Spring Boot 权限与审计架构图](./08-Hermes-与-Spring-Boot-权限与审计架构图.md)
9. [Hermes 与 Spring Boot 多租户隔离架构图](./09-Hermes-与-Spring-Boot-多租户隔离架构图.md)
10. [Hermes 与 Spring Boot 高可用架构图](./10-Hermes-与-Spring-Boot-高可用架构图.md)
11. [Hermes 与 Spring Boot 监控告警架构图](./11-Hermes-与-Spring-Boot-监控告警架构图.md)
12. [Hermes 与 Spring Boot 灾备架构图](./12-Hermes-与-Spring-Boot-灾备架构图.md)

## 推荐阅读顺序

如果团队是第一次系统看这部分内容，建议顺序：

1. 先看 01、02、03、04，建立“Spring Boot 怎么把 Hermes 当服务来调”的基本理解。
2. 再看 05、06，理解 Hermes 为什么不是普通模型接口，以及为什么更适合作为独立服务。
3. 最后看 07 到 12，进入企业架构、权限、隔离、高可用和灾备层面。
