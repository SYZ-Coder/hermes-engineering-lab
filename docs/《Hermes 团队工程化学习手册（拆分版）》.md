# 《Hermes 团队工程化学习手册（拆分版）》

这是一份面向团队阅读的拆分版学习手册。
与总手册不同，这里采用“一个问答一个 Markdown”的方式组织内容，目的是：

- 方便按主题单独阅读
- 方便在团队内部按专题转发
- 方便后续对某一篇单独补充和修订
- 避免总文档过大导致阅读负担过重

## 阅读建议

如果你是第一次接触 Hermes，建议按下面顺序阅读：

1. [自主学习机制全景解读](./团队工程化学习手册-拆分版/01-Hermes-自主学习机制全景解读.md)
2. [工具系统为什么设计得比较稳](./团队工程化学习手册-拆分版/02-Hermes-工具系统为什么设计得比较稳.md)
3. [外部服务通讯机制全景解读](./团队工程化学习手册-拆分版/03-Hermes-外部服务通讯机制全景解读.md)
4. [子代理机制为什么设计得比较克制](./团队工程化学习手册-拆分版/04-Hermes-子代理机制为什么设计得比较克制.md)
5. [会话系统为什么对工程落地很重要](./团队工程化学习手册-拆分版/05-Hermes-会话系统为什么对工程落地很重要.md)
6. [上下文治理为什么是核心竞争力之一](./团队工程化学习手册-拆分版/06-Hermes-上下文治理为什么是核心竞争力之一.md)
7. [为什么适合接 Spring Boot](./团队工程化学习手册-拆分版/07-Hermes-为什么适合接-Spring-Boot.md)
8. [工程化价值到底对团队意味着什么](./团队工程化学习手册-拆分版/08-Hermes-工程化价值到底对团队意味着什么.md)
9. [外部直接交互控制入口与 Spring Boot 实践](./团队工程化学习手册-拆分版/09-Hermes-外部直接交互控制入口与-Spring-Boot-实践.md)
10. [记忆系统为什么不是简单的 memory 文件](./团队工程化学习手册-拆分版/10-Hermes-记忆系统为什么不是简单的-memory-文件.md)
11. [会话系统为什么比普通聊天历史重要得多](./团队工程化学习手册-拆分版/11-Hermes-会话系统为什么比普通聊天历史重要得多.md)
12. [工程化价值到底体现在哪些地方](./团队工程化学习手册-拆分版/12-Hermes-工程化价值到底体现在哪些地方.md)
13. [与 Spring Boot 交互控制架构图](./团队工程化学习手册-拆分版/13-Hermes-与-Spring-Boot-交互控制架构图.md)
14. [与 Spring Boot 三种接入方式对照表](./团队工程化学习手册-拆分版/14-Hermes-与-Spring-Boot-三种接入方式对照表.md)
15. [Spring Boot 接 Hermes 的标准落地手册](./团队工程化学习手册-拆分版/15-Spring-Boot-接-Hermes-的标准落地手册.md)

## 配套代码示例

如果你想边看文档边看代码，建议配合下面三个工程：

- `D:\spring_AI\hermes_spring\example-spring-client`
- `D:\spring_AI\hermes_spring\example-spring-mcp-server`
- `D:\spring_AI\hermes_spring\example-spring-integration-modes`

其中 `example-spring-integration-modes` 最适合配合以下 3 篇一起阅读：

- 第 9 篇：外部直接交互控制入口与 Spring Boot 实践
- 第 14 篇：与 Spring Boot 三种接入方式对照表
- 第 15 篇：Spring Boot 接 Hermes 的标准落地手册

直接查看代码入口：
- [example-spring-integration-modes/README.md](../example-spring-integration-modes/README.md)

## 使用方式

### 如果你是团队负责人 / 架构师
优先阅读：
- 外部服务通讯机制全景解读
- 为什么适合接 Spring Boot
- 工程化价值到底对团队意味着什么
- 与 Spring Boot 交互控制架构图
- Spring Boot 接 Hermes 的标准落地手册

### 如果你是 Java / Spring Boot 开发
优先阅读：
- 外部直接交互控制入口与 Spring Boot 实践
- 与 Spring Boot 三种接入方式对照表
- Spring Boot 接 Hermes 的标准落地手册
- [example-spring-integration-modes/README.md](../example-spring-integration-modes/README.md)

### 如果你是 AI / Agent 方向同学
优先阅读：
- 自主学习机制全景解读
- 工具系统为什么设计得比较稳
- 子代理机制为什么设计得比较克制
- 会话系统为什么对工程落地很重要
- 上下文治理为什么是核心竞争力之一
- 记忆系统为什么不是简单的 memory 文件
- 会话系统为什么比普通聊天历史重要得多

## 说明

- 原早期背景资料已经归档到 `docs/archive/`，供回查使用。
- 当前主阅读路径以“拆分版专题文档 + 图谱 + 精简索引”为主。
- 这份拆分版更适合团队日常学习、分享和持续维护。
