# Hermes 学习笔记版

## 一页结论

Hermes 是 Nous Research 官方发布的自我改进 AI Agent。官方强调它具备：

- 交互式 Agent：CLI + 多平台消息网关
- 执行型 Agent：终端、文件、浏览器、代码执行、子代理、定时任务、MCP
- 学习型 Agent：memory、session search、skills、自我改进闭环
- 研究型 Agent：trajectory、RL environments、context compression

## 1. Hermes 是什么

官方 README 的定义是：

- self-improving AI agent

核心运行对象是：

- `run_agent.py` 中的 `AIAgent`

## 2. Hermes 有什么用

- 日常交互式 Agent
- 工具执行型 Agent
- 持续学习 / 跨会话记忆 Agent
- 研究与训练基础设施

## 3. 如何使用

官方安装方式：

```bash
curl -fsSL https://raw.githubusercontent.com/NousResearch/hermes-agent/main/scripts/install.sh | bash
```

常见命令：

```bash
hermes
hermes model
hermes tools
hermes gateway
hermes setup
```

## 4. 两个官方入口

- CLI：`hermes`
- Messaging Gateway：`hermes gateway`

## 5. 核心架构

关键模块：

- `run_agent.py`
- `model_tools.py`
- `tools/registry.py`
- `toolsets.py`
- `cli.py`
- `gateway/run.py`
- `hermes_state.py`
- `agent/prompt_builder.py`
- `agent/context_compressor.py`
- `agent/prompt_caching.py`

## 6. 官方架构特点

- Agent Runtime 中心化
- 自注册工具体系
- Toolset 组合与平台预设
- 多 provider / 多 API mode
- 上下文压缩
- prompt caching
- SQLite + FTS5 会话存储
- MCP + plugins 扩展

## 7. 运行机制

核心执行链：

1. 入口启动
2. profile 与 `HERMES_HOME`
3. 配置与环境加载
4. 创建 `AIAgent`
5. 构建 prompt
6. 发现工具并生成 tool schema
7. 调模型
8. 若有工具调用则执行工具
9. 返回最终文本
10. 持久化 session

## 8. 上下文治理

Hermes 官方单独提供：

- `context_compressor.py`
- `prompt_caching.py`

说明它把长上下文治理与成本优化视为系统级能力。

## 9. 存储方式

官方会话存储为：

- SQLite `state.db`
- WAL
- FTS5

## 10. 与 OpenClaw 的总体区别

- Hermes：以 Agent Runtime 为中心
- OpenClaw：以 Gateway 为中心

## 一句话总结

Hermes 是一个把 Agent 对话循环、工具系统、记忆系统、上下文治理、消息平台接入和研究能力统一在同一运行时之上的官方 AI Agent 系统。
