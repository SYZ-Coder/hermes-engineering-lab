# Changelog

这个文件用于记录 `hermes_spring` 学习项目的重要结构调整、文档沉淀和示例工程增强。

## 2026-04-15

### 新增

- 新增 `docs/《Hermes 团队工程化学习手册》.md`
- 新增 `docs/《Hermes 团队工程化学习手册（拆分版）》.md`
- 新增 `docs/团队工程化学习手册-拆分版/`，按“一个问答一个文档”拆分 15 篇专题
- 新增 `docs/团队学习图谱-Hermes架构流程与通讯时序.md`
- 新增 `docs/团队学习笔记-Hermes源码学习与外部通讯整理.md`
- 新增 `docs/团队阅读索引-精简版.md`
- 新增 `docs/learning/` 通讯专题资料整理
- 新增 `example-spring-client/` 最小 Spring Boot -> Hermes 示例工程
- 新增 `example-spring-mcp-server/` 最小 Spring Boot MCP 示例工程
- 新增 `LICENSE`
- 新增 `CONTRIBUTING.md`

### 调整

- 将资料整理为更适合 GitHub 展示的学习型项目结构
- 将 Hermes 与 Spring Boot 的接入方式按 API Server / Webhook / MCP 三条主线重组
- 为两个示例工程补充类、方法、配置项注释，增强教学可读性
- 将团队学习材料拆分为总手册、拆分版手册、图谱手册、阅读索引四层结构

### 说明

- 当前项目定位为学习与实践资料仓库，不包含 Hermes 官方完整源码镜像
- 后续建议优先通过文档、示例工程和接入规范持续迭代
