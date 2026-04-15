# Hermes vs OpenClaw 模块调用链对照版

## 一句话总览

- Hermes：`入口 -> AIAgent -> tool resolution -> model loop -> tools -> session db`
- OpenClaw：`入口 -> Gateway -> session/run orchestration -> embedded agent runtime -> plugins/capabilities -> stream back`

## 1. Hermes 模块调用链

```mermaid
flowchart TD
    H0["hermes / hermes gateway"] --> H1["hermes_cli/main.py"]
    H1 --> H2["Profile override"]
    H1 --> H3["cli.py"]
    H1 --> H4["gateway/run.py"]
    H3 --> H5["AIAgent"]
    H4 --> H5
    H5 --> H6["prompt_builder.py"]
    H5 --> H7["runtime_provider.py"]
    H5 --> H8["model_tools.py"]
    H5 --> H9["context_compressor.py"]
    H5 --> H10["prompt_caching.py"]
    H5 --> H11["hermes_state.py"]
    H8 --> H12["tools/registry.py"]
    H8 --> H13["toolsets.py"]
    H12 --> H14["tools/*.py"]
```

## 2. OpenClaw 模块调用链

```mermaid
flowchart TD
    O0["CLI / App / Web UI / Automations / Nodes"] --> O1["Gateway"]
    O1 --> O2["Typed WS API / RPC"]
    O2 --> O3["Session resolution"]
    O3 --> O4["Serialized run lane"]
    O4 --> O5["Embedded agent runtime"]
    O5 --> O6["Bootstrap / workspace context"]
    O5 --> O7["Skills snapshot"]
    O5 --> O8["Memory plugin"]
    O5 --> O9["Plugin capabilities"]
    O9 --> O10["Tools / Hooks / Services / Routes / Context engines"]
    O5 --> O11["Lifecycle / assistant / tool stream"]
    O11 --> O1
```

## 3. 对照总结

| 阶段 | Hermes | OpenClaw |
|---|---|---|
| 第一核心模块 | `AIAgent` | Gateway |
| 工具能力来源 | `model_tools.py` + registry + toolsets | plugin capability registry |
| 记忆接入 | memory + `session_search` + SQLite | memory plugin + workspace memory |
| 结果回传 | `AIAgent` 直接回 CLI/Gateway | runtime events 回 Gateway，再转发 |

## 一句话总结

- Hermes：Agent 驱动调用链
- OpenClaw：Gateway 驱动调用链
