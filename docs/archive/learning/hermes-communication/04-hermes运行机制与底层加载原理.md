# Hermes 运行机制与底层加载原理

## 总体运行机制

根据官方源码和开发文档，Hermes 的一次执行主链路如下：

1. 入口启动
2. 处理 profile 和 `HERMES_HOME`
3. 加载 `.env` 与 `config.yaml`
4. 创建 `AIAgent`
5. 构建 system prompt
6. 发现工具并生成 tool schemas
7. 解析 provider / api mode
8. 调用模型
9. 若有 `tool_calls` 则执行工具并回填消息
10. 无工具调用时返回最终文本
11. 持久化 session 到 SQLite

## 启动阶段

### `hermes_cli/main.py`

官方说明中，这个文件会先调用：

- `_apply_profile_override()`

作用是：

- 预解析 `--profile/-p`
- 在模块导入前设置 `HERMES_HOME`

## 配置与环境加载

CLI 与 Gateway 都会先加载：

- `~/.hermes/.env`
- `config.yaml`

Gateway 还会将部分配置桥接到环境变量。

## 工具加载原理

### 1. `model_tools._discover_tools()`

该函数会 import 多个工具模块，例如：

- `tools.web_tools`
- `tools.terminal_tool`
- `tools.file_tools`
- `tools.browser_tool`
- `tools.code_execution_tool`
- `tools.delegate_tool`

### 2. 模块 import 触发注册

每个工具模块在导入时调用：

- `registry.register(...)`

于是 `tools/registry.py` 中的注册中心收集所有工具信息。

### 3. MCP / plugin discovery

基础工具发现后，`model_tools.py` 继续尝试：

- `discover_mcp_tools()`
- `discover_plugins()`

## Tool schema 生成原理

`get_tool_definitions()` 的工作包括：

1. 根据 enabled/disabled toolsets 解析工具范围
2. 通过 `registry.get_definitions(...)` 获取 schema
3. 根据 `check_fn` 过滤不可用工具
4. 动态修正某些工具的 schema 描述

## Agent Loop 原理

官方 `AGENTS.md` 给出的核心循环是：

```python
while api_call_count < self.max_iterations and self.iteration_budget.remaining > 0:
    response = client.chat.completions.create(model=model, messages=messages, tools=tool_schemas)
    if response.tool_calls:
        for tool_call in response.tool_calls:
            result = handle_function_call(tool_call.name, tool_call.args, task_id)
            messages.append(tool_result_message(result))
        api_call_count += 1
    else:
        return response.content
```

在真实实现里，`run_agent.py` 还额外处理：

- retries
- invalid tool name repair
- invalid JSON arguments
- concurrent / sequential tool execution
- fallback runtime
- context compression
- prompt caching

## Provider / API mode 加载原理

`hermes_cli/runtime_provider.py` 负责把运行时解析成：

- `chat_completions`
- `codex_responses`
- `anthropic_messages`

并结合：

- provider
- base URL
- API key
- config

## 上下文压缩与缓存

### `agent/context_compressor.py`

负责：

- 裁剪旧 tool output
- 保护头尾上下文
- 总结中间对话

### `agent/prompt_caching.py`

官方源码明确采用：

- system_and_3 strategy

即：

- system prompt
- 最后 3 条非 system 消息

## 会话持久化原理

`hermes_state.py` 官方说明明确指出：

- 使用 SQLite
- 使用 WAL
- 使用 FTS5
- 保存 sessions/messages/messages_fts

这意味着 Hermes 不是简单把会话写成文本文件，而是维护正式的结构化会话数据库。

## 一句话总结

Hermes 的底层加载原理是：先确定 profile 与配置，再动态发现工具与扩展，随后由 `AIAgent` 驱动模型循环，并在需要时调用工具、压缩上下文、使用缓存，最后把会话持久化到 SQLite。
