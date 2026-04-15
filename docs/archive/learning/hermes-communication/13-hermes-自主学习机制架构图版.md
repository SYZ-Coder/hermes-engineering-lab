# Hermes 自主学习机制架构图版

## 说明

本文是 Hermes 自主学习机制的图解版，只基于当前仓库源码与项目内官方文档整理，用 Mermaid 图帮助理解其设计思路、运行机制与闭环结构。

---

## 1. Hermes 自主学习总体闭环图

```mermaid
flowchart TD
    A["用户任务与对话"] --> B["AIAgent 主循环"]
    B --> C["工具调用 / 试错 / 完成任务"]

    C --> D["系统提示中的学习规则"]
    D --> E["memory：保存稳定事实"]
    D --> F["skill_manage：保存可复用方法"]

    C --> G["运行时计数器触发"]
    G --> H["后台 review 线程"]
    H --> E
    H --> F

    C --> I["session 持久化到 SQLite"]
    I --> J["session_search 跨会话回忆"]

    C --> K["压缩前 flush_memories"]
    K --> E

    C --> L["外部 memory provider"]
    L --> M["prefetch / sync / pre-compress / delegation hooks"]

    E --> N["下一次 session 注入 frozen snapshot"]
    F --> O["下一次任务加载 / 使用 / patch skill"]
    J --> B
    N --> B
    O --> B
```

---

## 2. 自主学习触发机制图

```mermaid
flowchart TD
    A["用户发起新一轮对话"] --> B["增加 user turn 计数"]
    B --> C{"达到 memory_nudge_interval 吗？"}
    C -- 是 --> D["标记 should_review_memory"]
    C -- 否 --> E["继续主任务"]

    E --> F["工具调用迭代增加"]
    F --> G{"达到 skill_nudge_interval 吗？"}
    G -- 是 --> H["标记 should_review_skills"]
    G -- 否 --> I["继续主循环"]

    D --> J["主响应完成后启动后台 review"]
    H --> J
```

---

## 3. 后台 review 执行图

```mermaid
sequenceDiagram
    participant 主代理 as 主 AIAgent
    participant review线程 as 后台 review 线程
    participant review代理 as review AIAgent
    participant 共享存储 as memory / skills store

    主代理->>review线程: _spawn_background_review(...)
    review线程->>review线程: 选择 review prompt
    review线程->>review代理: 创建新的 AIAgent fork
    review代理->>review代理: 使用 messages_snapshot + review_prompt 运行
    review代理->>共享存储: 写入 memory 或 skill
    review线程-->>主代理: 完成（无用户可见输出）
```

---

## 4. memory 学习机制图

```mermaid
flowchart LR
    A["任务执行过程中发现稳定事实"] --> B["memory 工具"]
    B --> C["MemoryStore"]
    C --> D["MEMORY.md"]
    C --> E["USER.md"]

    D --> F["下次 session load_from_disk()"]
    E --> F
    F --> G["frozen snapshot"]
    G --> H["系统提示注入"]
```

---

## 5. skill 学习机制图

```mermaid
flowchart LR
    A["完成复杂任务 / 修复 tricky error / 发现 workflow"] --> B["skill_manage"]
    B --> C["create / patch / edit"]
    C --> D["skills 目录"]
    D --> E["清理 skills prompt cache"]
    E --> F["未来任务重新加载 skill"]
    F --> G["若发现问题，立即 patch"]
```

---

## 6. session_search 在学习闭环中的位置

```mermaid
flowchart TD
    A["会话结束"] --> B["写入 SQLite state.db"]
    B --> C["sessions / messages / FTS5"]
    C --> D["未来任务中触发 session_search"]
    D --> E["检索相关历史会话"]
    E --> F["摘要返回给当前主循环"]
```

---

## 7. flush_memories 机制图

```mermaid
flowchart TD
    A["即将压缩上下文 / reset / exit"] --> B["flush_memories()"]
    B --> C["插入 flush 提示消息"]
    C --> D["只开放 memory 工具发起一次 API 调用"]
    D --> E{"模型是否生成 memory tool call？"}
    E -- 是 --> F["执行 memory 写入"]
    E -- 否 --> G["无写入"]
    F --> H["清理 flush artifacts"]
    G --> H
```

---

## 8. 外部 memory provider 接入图

```mermaid
flowchart TD
    A["MemoryManager"] --> B["built-in provider"]
    A --> C["最多一个 external provider"]

    A --> D["build_system_prompt"]
    A --> E["prefetch_all"]
    A --> F["sync_all"]
    A --> G["queue_prefetch_all"]
    A --> H["on_pre_compress"]
    A --> I["on_memory_write"]
    A --> J["on_delegation"]
```

---

## 9. 自主学习分层图

```mermaid
flowchart TD
    subgraph 第一层["第一层：规则层"]
        A1["系统提示中的 MEMORY_GUIDANCE"]
        A2["系统提示中的 SKILLS_GUIDANCE"]
        A3["系统提示中的 SESSION_SEARCH_GUIDANCE"]
    end

    subgraph 第二层["第二层：触发层"]
        B1["memory nudge 计数器"]
        B2["skill nudge 计数器"]
        B3["flush_memories"]
    end

    subgraph 第三层["第三层：执行层"]
        C1["memory 工具"]
        C2["skill_manage 工具"]
        C3["后台 review 线程"]
        C4["session_search"]
    end

    subgraph 第四层["第四层：存储层"]
        D1["MEMORY.md / USER.md"]
        D2["skills 目录"]
        D3["SQLite state.db"]
        D4["trajectory JSONL"]
    end

    第一层 --> 第二层
    第二层 --> 第三层
    第三层 --> 第四层
```

---

## 10. 一句话理解图

```mermaid
flowchart LR
    A["对话与任务执行"] --> B["沉淀稳定事实到 memory"]
    A --> C["沉淀可复用方法到 skill"]
    A --> D["沉淀完整历史到 session DB"]
    B --> E["未来 session 注入"]
    C --> F["未来任务复用与修补"]
    D --> G["未来 session_search 回忆"]
```

---

## 11. 最压缩总结

Hermes 的自主学习图解可以概括为：

- system prompt 规定学习规则
- memory 保存稳定事实
- skill 保存可复用方法
- session_search 回忆历史会话
- background review 周期性复盘
- flush_memories 在压缩前抢救记忆
- memory provider 把学习闭环扩展到外部后端
