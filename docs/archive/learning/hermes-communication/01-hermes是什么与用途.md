# Hermes 是什么？有什么用？

## 定义

Hermes 官方 README 将其定义为：

- “The self-improving AI agent built by Nous Research.”

官方文档与源码说明表明，Hermes 的核心是 `run_agent.py` 中的 `AIAgent`，它负责完整的对话循环、工具调用、上下文管理与会话持久化。

## 主要用途

根据官方 README、开发文档和仓库结构，Hermes 的用途主要包括：

1. 作为交互式 AI Agent 使用
- 既可以在 CLI 中使用，也可以通过消息网关在 Telegram、Discord、Slack、WhatsApp、Signal、Email 等平台使用。

2. 作为具备工具执行能力的 Agent 使用
- 官方工具体系覆盖终端、文件、网页、浏览器、代码执行、子代理委托、定时任务、MCP 等能力。

3. 作为具备持续学习能力的 Agent 使用
- README 明确强调 built-in learning loop、memory、session search、skills self-improve、cross-session recall。

4. 作为研究与训练基础设施使用
- README 和开发文档明确提到 batch trajectory generation、Atropos RL environments、trajectory compression。

## 官方强调的核心特点

- Self-improving AI agent
- Built-in learning loop
- 多平台消息接入
- Skills 系统
- Persistent memory
- Session search
- Scheduled automations
- Delegation / parallelization
- Multi-backend terminal environments
- Research-ready

## 一句话总结

Hermes 是一个由 Nous Research 官方发布的、以 `AIAgent` 为核心的自我改进型 AI Agent 系统，既能做日常交互，也能做工具执行、长期记忆、自动化任务，以及研究训练相关工作。
