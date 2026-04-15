# Hermes vs OpenClaw 深度对照表

## 总览

| 维度 | Hermes | OpenClaw |
|---|---|---|
| 官方主定位 | self-improving AI agent | Gateway-centric agent system |
| 系统中心 | `AIAgent` | Gateway |
| 主要入口 | CLI + Gateway | Gateway 为中心 |
| 工具主轴 | built-in tools + toolsets + MCP/plugins | plugin capability model |
| 记忆主轴 | bounded memory + session search | workspace memory + memory plugin |
| 技能主轴 | progressive disclosure + skill manage | 多来源技能 + snapshot |

## 1. 系统中心

- Hermes：`AIAgent`
- OpenClaw：Gateway

## 2. 执行主链

### Hermes

- 入口
- `AIAgent`
- 工具解析
- 模型循环
- 工具执行
- 会话持久化

### OpenClaw

- client / node
- Gateway
- session resolution
- serialized run
- embedded agent runtime
- plugin capability execution
- stream 回 Gateway

## 3. 工具体系

### Hermes

- `tools/registry.py`
- `model_tools.py`
- `toolsets.py`
- `tools/*.py`
- MCP / plugins discovery

### OpenClaw

- plugins
- capability registry
- tools / hooks / services / routes / context engines

## 4. 记忆体系

### Hermes

- `MEMORY.md`
- `USER.md`
- `session_search`
- SQLite + FTS5

### OpenClaw

- `MEMORY.md`
- daily memory files
- active memory plugin
- memory tools

## 5. 技能体系

### Hermes

- `~/.hermes/skills/`
- progressive disclosure
- skill 管理工具

### OpenClaw

- 多来源 skills
- precedence
- session snapshot

## 一句话总结

- Hermes 的官方资料更强调 Agent 本体与其工具、记忆、上下文治理能力。
- OpenClaw 的官方资料更强调 Gateway、typed WS API 和 plugin capability 体系。
