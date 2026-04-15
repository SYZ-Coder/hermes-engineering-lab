# Hermes 架构设计

## 总体架构

根据官方开发文档与仓库说明，Hermes 是一个以 `AIAgent` 为核心、由 CLI 与 Gateway 共用同一 Agent Runtime 的系统。

核心模块包括：

- `run_agent.py`：`AIAgent`，核心对话循环
- `model_tools.py`：工具发现、编排、调用分发
- `tools/registry.py`：工具注册中心
- `toolsets.py`：工具集合与平台预设
- `cli.py`：CLI 交互入口
- `gateway/run.py`：多平台消息网关入口
- `hermes_state.py`：SQLite + FTS5 会话存储
- `agent/prompt_builder.py`：系统提示拼装
- `agent/context_compressor.py`：上下文压缩
- `agent/prompt_caching.py`：Prompt caching

## 官方给出的依赖链

官方 `AGENTS.md` 明确给出的文件依赖链是：

```text
tools/registry.py
  -> tools/*.py
  -> model_tools.py
  -> run_agent.py / cli.py / batch_runner.py / environments/
```

## 架构设计特点

### 1. 以 Agent Runtime 为核心

官方开发文档把 `AIAgent` 作为核心执行对象，CLI 与 Gateway 都最终汇入同一对话循环。

### 2. 自注册工具体系

每个工具模块在 import 时调用 `registry.register()` 注册自身：

- schema
- handler
- toolset
- check_fn

### 3. Toolset 分层设计

Hermes 不是简单暴露全部工具，而是通过 `toolsets.py` 按平台和场景组织工具集合，例如：

- `hermes-cli`
- `hermes-telegram`
- `hermes-discord`
- `hermes-gateway`

### 4. Provider / API mode 抽象

官方运行时支持多种 API mode：

- `chat_completions`
- `codex_responses`
- `anthropic_messages`

### 5. 上下文治理

官方架构中单独存在：

- `context_compressor.py`
- `prompt_caching.py`

说明 Hermes 明确把上下文压缩和缓存当作系统级能力处理。

### 6. 会话持久化

Hermes 官方把 `hermes_state.py` 作为正式会话存储层，采用：

- SQLite
- WAL
- FTS5

### 7. 多 Profile 隔离

官方文档明确说明通过 `_apply_profile_override()` 和 `HERMES_HOME` 实现 profile 隔离。

### 8. 可扩展边界

官方支持两类外部扩展：

- MCP discovery
- plugin discovery

## 一句话总结

Hermes 的官方架构是一种“Agent Runtime 中心化”的设计：CLI、Gateway、工具系统、记忆系统、上下文压缩、缓存和会话存储都围绕 `AIAgent` 统一组织。
