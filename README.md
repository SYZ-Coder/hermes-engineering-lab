# Hermes Spring Learning Project

这是一个围绕 **Hermes 与 Spring Boot 集成实践** 整理出来的开源学习型项目。

项目目标不是复刻 Hermes 全量源码，而是把团队最常需要的几类内容收拢到一起：

- Hermes 工程化理解与学习材料
- Hermes 与外部服务通讯机制整理
- Spring Boot 调 Hermes 的最小示例
- Spring Boot 作为 MCP 服务供 Hermes 调用的最小示例
- 单模块同时演示三种接入方式的整合示例
- 面向团队传阅、培训、二次沉淀的文档结构

## 项目内容

- `docs/`
  团队学习文档、拆分版专题手册、架构图谱、阅读索引

- `example-spring-client/`
  Spring Boot 调 Hermes 的最小示例工程
  包含：
  - API Server 调用示例
  - Webhook 调用示例
  - RestTemplate / WebClient 两种客户端写法

- `example-spring-mcp-server/`
  Spring Boot 作为 MCP 风格能力服务供 Hermes 调用的最小示例工程
  包含：
  - `query_order`
  - `query_user`
  - `approve_order`

- `example-spring-integration-modes/`
  单模块同时演示三种集成方式：API Server、Webhook、MCP

- `README-快速开始.md`
  面向第一次接触本项目的同学的快速入口

## 项目结构

```text
hermes_spring/
├─ README.md
├─ README-快速开始.md
├─ LICENSE
├─ CONTRIBUTING.md
├─ CHANGELOG.md
├─ pom.xml
├─ docs/
│  ├─ README.md
│  ├─ 《Hermes 团队工程化学习手册（拆分版）》.md
│  ├─ 团队阅读索引-精简版.md
│  ├─ 团队学习图谱-Hermes架构流程与通讯时序.md
│  ├─ 团队工程化学习手册-拆分版/
│  └─ archive/
├─ example-spring-client/
├─ example-spring-mcp-server/
└─ example-spring-integration-modes/
```

## 推荐入口

### 1. 如果你想先看整体

先看：

- `docs/《Hermes 团队工程化学习手册（拆分版）》.md`
- `docs/README.md`

### 2. 如果你想先看图

先看：

- `docs/团队学习图谱-Hermes架构流程与通讯时序.md`

### 3. 如果你想直接看 Spring Boot 如何对接

先看：

- `README-快速开始.md`
- `example-spring-client/README.md`
- `example-spring-mcp-server/README.md`
- `example-spring-integration-modes/README.md`

## 适合谁使用

这个项目特别适合：

- 想学习 Hermes 工程化设计思路的团队
- 想把 Hermes 接到 Spring Boot 项目里的 Java 团队
- 想把 Spring Boot 服务能力暴露给 Hermes 调用的团队
- 想做内部培训、知识沉淀、开源分享的团队

## 推荐阅读顺序

1. `README-快速开始.md`
2. `docs/README.md`
3. `docs/《Hermes 团队工程化学习手册（拆分版）》.md`
4. `example-spring-client/README.md`
5. `example-spring-mcp-server/README.md`
6. `example-spring-integration-modes/README.md`

## 项目定位说明

这是一个 **学习与实践导向** 的开源项目：

- 重点在于理解 Hermes 的工程化能力
- 重点在于 Spring Boot 与 Hermes 的接入实践
- 重点在于把知识整理成团队可复用的材料

它不是 Hermes 官方源码镜像，也不是 Hermes 的二次发行版。

如果后续你们团队继续补充内容，建议优先延续这三个方向：

1. 文档持续补充
2. 示例工程持续增强
3. 团队内部规范持续沉淀