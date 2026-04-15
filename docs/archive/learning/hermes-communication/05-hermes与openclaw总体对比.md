# Hermes 与 OpenClaw 总体对比

## 说明

本对比只基于：

- Hermes 官方 README / 文档 / 当前仓库源码说明
- OpenClaw 官方文档

不做额外推理。

## 总体定位对比

| 维度 | Hermes | OpenClaw |
|---|---|---|
| 官方主定位 | self-improving AI agent | Gateway-centric agent system |
| 系统中心 | `AIAgent` | Gateway |
| 核心叙事 | learning loop、memory、tools、skills | gateway、typed WS API、plugin capabilities |
| 主入口 | `hermes` + `hermes gateway` | Gateway 为中心，CLI / app / UI / automations / nodes 接入 |

## 运行主链对比

### Hermes

主链路是：

- CLI / Gateway
- `AIAgent`
- tool resolution
- model loop
- tools
- session db

### OpenClaw

主链路是：

- client / node
- Gateway
- session resolution
- serialized run
- embedded agent runtime
- plugin capabilities
- stream 回 Gateway

## 工具体系对比

### Hermes

官方强调：

- built-in tools
- toolsets
- registry
- MCP
- plugins

### OpenClaw

官方强调：

- plugin capability model
- tools
- hooks
- services
- routes
- context engines

## 记忆与技能对比

### Hermes

官方强调：

- bounded memory
- `MEMORY.md`
- `USER.md`
- `session_search`
- skills progressive disclosure

### OpenClaw

官方强调：

- workspace markdown memory
- active memory plugin
- skills snapshot
- 多来源 skills 优先级

## 一句话总结

- Hermes 更像“以 Agent Runtime 为中心，把工具、记忆、压缩、缓存、CLI、Gateway 统一起来”的设计。
- OpenClaw 更像“以 Gateway 为中心，把 agent runtime、plugins、channels、nodes、skills、memory surfaces 统一起来”的设计。
