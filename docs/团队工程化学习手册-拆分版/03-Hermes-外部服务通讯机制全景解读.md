# Hermes 外部服务通讯机制全景解读

很多团队第一次接触 Hermes 时，最关心的不是它会不会聊天，而是：

**它到底怎么跟外部系统打通？**

### 1. 不是一条路，而是两大方向

Hermes 和外部系统之间的通讯，可以稳定地分成两个方向：

#### 方向一：外部系统请求 Hermes

比如：

- Spring Boot 调 Hermes
- Web 前端调 Hermes
- 业务系统把事件推给 Hermes
- 编辑器或客户端接 Hermes

#### 方向二：Hermes 请求外部系统

比如：

- Hermes 查订单系统
- Hermes 查用户系统
- Hermes 调审批接口
- Hermes 调企业知识库
- Hermes 连 MCP 服务

### 2. 核心思路

**外部系统进 Hermes，给你标准入口；Hermes 出去调外部能力，给你标准扩展。**

### 3. 外部系统如何请求 Hermes

主要有三类入口：

#### API Server

最标准的“服务对服务调用”方式。

#### Webhook

最标准的“事件驱动接入”方式。

#### ACP

更偏协议化接入：编辑器、IDE 插件、Agent 客户端。

### 4. Hermes 怎么请求外部系统

最标准的外部能力接入方式是：

#### MCP

你可以把 MCP 理解成：

**把外部系统包装成 Hermes 可调用的工具。**

### 5. 为什么 MCP 对企业系统特别友好

因为企业内部服务通常都已经是：

- HTTP 服务
- 有清晰业务边界
- 有稳定数据能力
- 更适合被受控调用

### 6. Spring Boot 在这里扮演的角色

Spring Boot 可以同时扮演两种角色：

- 作为 Hermes 的调用方：调 Hermes
- 作为 Hermes 的能力提供方：被 Hermes 通过 MCP 调用

### 7. 一句话总结

**Hermes 把“外部系统如何进入自己”和“自己如何调用外部能力”清晰分成了两套机制：前者主要靠 API Server / Webhook / ACP，后者主要靠 MCP 及其他扩展能力；而 Spring Boot 恰好可以在这两边都很好地接入。**

---
