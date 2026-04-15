# Hermes 安装后是不是一个服务，以及 Spring Boot 如何请求

可以这样理解：

**Hermes 安装后，不默认就是一个常驻服务。**
它首先是一个 **Agent 运行时 / 命令行程序**。只有当你显式启动对应入口时，它才会表现成“服务”。

最常见有 3 种形态：

1. **CLI 形态**
- 你在终端里直接运行 `hermes`
- 这是本地交互式 Agent
- 不是给 Spring Boot 直接调的

2. **API 服务形态**
- 你启动 Hermes 的 `API Server`
- 它会对外暴露 HTTP 接口
- 这时 Spring Boot 就可以像调普通 HTTP 服务一样去调 Hermes

3. **事件入口 / 网关形态**
- 你启动 `Webhook` 或消息网关
- 外部系统把事件推给 Hermes
- Hermes 把事件转成 Agent 任务处理

所以答案是：

**Hermes 可以变成一个服务，但不是“安装完天然就是服务”；要看你启动的是哪种运行模式。**

## 如果你的问题是：Spring Boot 能不能直接请求 Hermes？

答案是：

**可以，但前提是你启动了 Hermes 的 API Server。**

这时候 Spring Boot 的调用方式，就是标准 HTTP 调用：

1. 先请求 Hermes 服务地址
2. 再调用 Hermes 暴露的对话接口
3. Hermes 收到请求后进入统一 Agent Runtime
4. 必要时调用工具、记忆、会话等能力
5. 最后返回结果

## Spring Boot 请求的到底是什么？

最常见就是请求 Hermes 的 **API Server**。

你可以把它理解成：

**Hermes 对外暴露了一层 HTTP Agent 服务接口。**

Spring Boot 这时不是在调一个普通 LLM SDK，而是在调一个已经封装好这些能力的 Agent 服务：

- 多轮会话
- 工具调用
- 上下文治理
- 记忆能力
- 长任务处理
- 统一 Agent Runtime

也就是说，Spring Boot 并不是直接操作模型，而是在请求：

**Hermes 的 Agent 能力服务。**

## Spring Boot 调 Hermes 的最常见方式是什么？

最常见是这条链路：

**Spring Boot -> Hermes API Server -> Hermes Agent Runtime**

它是最标准、最像“服务调服务”的方式。

## 是不是“Spring 直接请求服务地址，加上 Hermes 对话接口”就行？

**大方向上是对的。**

也就是：

1. 先把 Hermes API Server 启起来
2. Spring Boot 按 HTTP 客户端方式访问它
3. 调 Hermes 的对话接口
4. 拿回结果

但更准确一点说，不只是“一个对话接口”这么简单，而是你通常会关心这些层面：

### 1. 基础地址
也就是 Hermes 服务地址，比如：
- `http://host:8642/v1`

### 2. 具体接口
最常见是：
- `chat/completions`
- 或者 `responses`

### 3. 会话连续性
如果你希望多轮请求属于同一个 Hermes 会话，Spring Boot 还需要传递：
- session 标识
- 或 response 关联标识

### 4. 长任务模式
如果任务比较长，可能不是简单同步请求，而是：
- run
- event stream
- 异步结果拉取

## 一句话结论

**Hermes 安装后本身不是默认常驻服务；但你可以把它启动成 API Server，这时 Spring Boot 就可以直接请求 Hermes 的服务地址和对话接口，把 Hermes 当成一个标准的 Agent HTTP 服务来调用。**
