# Hermes API Server 的请求响应结构详细说明

如果你把 Hermes 作为服务给 Spring Boot 调，最常见的入口就是：

- `POST /v1/chat/completions`
- 其次是 `POST /v1/responses`

你可以先把它理解成：

**Hermes 对外暴露了一层“类 OpenAI 风格”的 HTTP Agent 接口。**

也就是说，Spring Boot 调 Hermes 时，整体感觉会很像调一个大模型服务，但背后实际跑的是 **Hermes Agent Runtime**。

## 最常见请求结构：`/v1/chat/completions`

这条接口最适合：
- 同步问答
- 单次任务请求
- 先快速跑通 Spring Boot -> Hermes

它的请求核心一般包括这几类信息：

### 1. 模型信息
告诉 Hermes 这次希望用哪个模型。

### 2. 消息数组 `messages`
这是最核心的数据。
通常会包含：
- `system`
- `user`
- `assistant`

但在 Hermes 里，这个 `messages` 不只是普通聊天上下文，它后面还会继续挂接：
- 工具调用信息
- 工具结果
- 会话上下文
- 记忆和技能相关背景

### 3. 可选参数
比如：
- 是否流式返回
- 温度
- 最大输出长度
- 会话标识

如果你是 Spring Boot 来接，一开始可以只关心最小字段：
- `model`
- `messages`

## 最小请求思路可以怎么理解？

Spring Boot 发给 Hermes 的最小请求，本质上可以理解成：

```json
{
  "model": "某个模型",
  "messages": [
    {
      "role": "user",
      "content": "请帮我分析这条业务异常"
    }
  ]
}
```

但在真实工程里，通常还会增加一层：

- Spring Boot 自己生成业务会话 id
- 再把这个会话 id 传给 Hermes
- 这样 Hermes 才能把多轮请求接成同一条会话主线

## Hermes 返回什么？

Hermes 返回给 Spring Boot 的，一般还是一个标准 HTTP JSON 响应。

如果从外部看，最重要的返回内容通常是：

### 1. 最终文本结果
也就是这次 Agent 执行后的回答。

### 2. 响应元信息
比如：
- id
- model
- created
- choices

### 3. 如果走流式
那就不是一次性 JSON，而是分片事件流。

## Hermes 和普通 LLM 接口最不一样的地方是什么？

最关键的是：

**你调到的不是“纯模型”，而是“带 Agent 行为的模型服务”。**

这意味着 Hermes 内部可能会做这些事：

- 读取会话历史
- 拼 system prompt
- 带入长期记忆
- 带入技能
- 解析当前可用工具
- 让模型决定要不要调用工具
- 真正执行工具
- 把工具结果回填
- 再继续推理
- 最终才返回结果

## `/v1/responses` 和 `/v1/chat/completions` 有什么理解差异？

可以简单这么理解：

### `/v1/chat/completions`
更像：
- 最熟悉
- 最容易上手
- 最适合先跑通

### `/v1/responses`
更像：
- 更强调响应对象本身
- 更适合做连续响应链
- 更适合一些带状态续接的处理方式

## 如果任务比较长，怎么办？

如果任务比较长，不建议永远都同步阻塞等结果。
这时更适合用：

- `runs`
- `events`

它的思路是：

1. Spring Boot 先发起一个 run
2. Hermes 返回 `run_id`
3. Spring Boot 再根据 `run_id` 拉状态或接事件
4. 最终拿结果

## 一句话总结

**Hermes API Server 表面像大模型接口，但内核是 Agent 运行时接口。**
