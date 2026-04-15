# 文档导航

这里是 `D:\spring_AI\hermes_spring\docs` 的团队学习与交付文档首页。

## 推荐入口

1. [《Hermes 团队工程化学习手册（拆分版）》](./《Hermes 团队工程化学习手册（拆分版）》.md)
适合团队日常按主题阅读，每一个问答独立成文，阅读负担最小。

2. [团队工程化学习手册-拆分版/README.md](./团队工程化学习手册-拆分版/README.md)
查看拆分版目录下 15 份独立专题文档。

3. [团队学习图谱-Hermes架构流程与通讯时序](./团队学习图谱-Hermes架构流程与通讯时序.md)
适合先看图再看文，快速理解整体架构、时序图和交互链路。

4. [团队阅读索引-精简版](./团队阅读索引-精简版.md)
适合按角色快速找到推荐阅读顺序。

## 代码示例入口

如果你想从文档直接进入代码示例，建议优先看这 3 个工程：

- `D:\spring_AI\hermes_spring\example-spring-client`
  Spring Boot 调 Hermes 的最小示例（API Server + Webhook + RestTemplate + WebClient）。
- `D:\spring_AI\hermes_spring\example-spring-mcp-server`
  Spring Boot 作为 MCP 服务供 Hermes 调用的最小示例。
- `D:\spring_AI\hermes_spring\example-spring-integration-modes`
  单模块同时演示三种方式：API Server、Webhook、MCP。

### 文档与代码的双向阅读建议

如果你重点看 Hermes 和 Spring Boot 的三种接入方式，建议按下面这条链路阅读：

1. [09-Hermes-外部直接交互控制入口与-Spring-Boot-实践](./团队工程化学习手册-拆分版/09-Hermes-外部直接交互控制入口与-Spring-Boot-实践.md)
2. [14-Hermes-与-Spring-Boot-三种接入方式对照表](./团队工程化学习手册-拆分版/14-Hermes-与-Spring-Boot-三种接入方式对照表.md)
3. [15-Spring-Boot-接-Hermes-的标准落地手册](./团队工程化学习手册-拆分版/15-Spring-Boot-接-Hermes-的标准落地手册.md)
4. [example-spring-integration-modes/README.md](../example-spring-integration-modes/README.md)

## 专题扩展

如果你想系统学习“Spring Boot 如何把 Hermes 当服务接入，以及后续企业架构怎么设计”，可以继续看：

- [服务化与企业架构专题](./服务化与企业架构专题/README.md)

## 归档资料

以下资料已归档到 `archive/`，保留供回查使用，但不再作为主阅读入口：

- [archive/团队学习笔记-Hermes源码学习与外部通讯整理.md](./archive/团队学习笔记-Hermes源码学习与外部通讯整理.md)
- `archive/learning/`：早期专题资料整理目录

