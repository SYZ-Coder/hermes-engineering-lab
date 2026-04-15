# Hermes vs OpenClaw 选型建议版

## 说明

本文件只根据官方资料给出“适用条件对照”，不做主观拍板。

## 1. 如果你更看重 Agent 本体能力

更偏 Hermes 官方设计。

因为 Hermes 官方持续强调：

- self-improving AI agent
- learning loop
- built-in memory
- session search
- context compression
- prompt caching
- delegate_task
- toolsets

适用条件：

- 你更关注 agent 自己如何执行、记忆、压缩上下文和调用工具
- 你希望从 CLI 直接使用并逐步扩展到网关
- 你希望有内建的会话数据库和搜索能力

## 2. 如果你更看重 Gateway 与插件平台

更偏 OpenClaw 官方设计。

因为 OpenClaw 官方持续强调：

- single long-lived Gateway
- typed WebSocket API
- nodes + control-plane clients
- plugin capability model
- central registry

适用条件：

- 你希望所有客户端、自动化、节点都围绕一个网关
- 你更在意路由、事件、RPC、capability ownership
- 你希望插件是系统的一等核心

## 3. 记忆维度

| 需求 | 更偏 Hermes | 更偏 OpenClaw |
|---|---|---|
| bounded curated memory | 是 | 否 |
| session 全文搜索 | 是 | 官方主轴不是这个 |
| workspace markdown memory 主导 | 否 | 是 |
| memory plugin 主导 | 次要 | 是 |

## 4. 技能维度

| 需求 | 更偏 Hermes | 更偏 OpenClaw |
|---|---|---|
| 单一技能目录 | 是 | 否 |
| agent 自己管理技能 | 是 | 官方重点较弱 |
| 多来源 skills 优先级 | 次要 | 是 |
| session snapshot 技能语义 | 较弱 | 是 |

## 5. 扩展维度

| 需求 | 更偏 Hermes | 更偏 OpenClaw |
|---|---|---|
| 主要扩工具与 MCP | 是 | 否 |
| 主要扩 provider / route / service / hooks | 次要 | 是 |
| manifest-first capability registry | 次要 | 是 |

## 6. 研究与训练维度

更偏 Hermes。

官方明确提供：

- Atropos RL environments
- trajectory compression
- batch runner
- data generation

## 一句话总结

- 如果你把“Agent 自身能力”放在第一位，更偏 Hermes。
- 如果你把“Gateway 与插件平台”放在第一位，更偏 OpenClaw。
