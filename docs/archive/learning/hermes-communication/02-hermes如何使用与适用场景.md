# Hermes 如何使用？适用哪些场景？

## 官方给出的基本使用方式

根据 README，Hermes 的官方安装方式是：

```bash
curl -fsSL https://raw.githubusercontent.com/NousResearch/hermes-agent/main/scripts/install.sh | bash
```

安装完成后，README 给出的常见入口包括：

```bash
hermes
hermes model
hermes tools
hermes config set
hermes gateway
hermes setup
hermes claw migrate
hermes update
hermes doctor
```

## 两个官方入口

README 明确说明 Hermes 有两个入口：

1. CLI 入口
- `hermes`

2. Messaging Gateway 入口
- `hermes gateway`

CLI 与 Messaging 平台共享大量 slash commands，例如：

- `/new`
- `/reset`
- `/model`
- `/personality`
- `/retry`
- `/undo`
- `/compress`
- `/usage`
- `/skills`

## 落地使用方式

### 1. 本地终端使用

适合：

- 日常问答
- 代码与文件操作
- 工具调用
- 终端自动化

### 2. 消息平台使用

适合：

- Telegram/Discord/Slack/WhatsApp/Signal 等远程交互
- 长时间在云端运行的 Agent
- 跨设备持续对话

### 3. 定时任务与自动化

官方 README 明确提到：

- daily reports
- nightly backups
- weekly audits

### 4. 子代理并行工作

官方 README 明确提到：

- Spawn isolated subagents for parallel workstreams

### 5. MCP 扩展使用

官方文档明确支持：

- Connect any MCP server for extended capabilities

### 6. 研究与训练场景

官方 README 明确提到：

- Batch trajectory generation
- Atropos RL environments
- trajectory compression

## 适用场景总结

根据官方资料，Hermes 适合：

- 本地 CLI Agent
- 云端常驻 Agent
- 多平台消息机器人
- 自动化与调度任务
- 代码与工具执行型任务
- 需要长期记忆和跨会话搜索的场景
- 研究、数据生成、RL 环境场景

## 官方限制

- README 明确写明：Windows 原生不支持，建议使用 WSL2
- Android/Termux 需要走官方单独说明的安装路径
