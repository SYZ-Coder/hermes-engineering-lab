# Spring Boot 调 Hermes 的最小请求示例说明

如果你们团队现在要先跑通一条最小链路，最推荐的目标就是：

**Spring Boot -> Hermes API Server -> `/v1/chat/completions`**

先不要一开始就做：
- Webhook
- MCP
- 长任务事件流
- 多工具复杂编排

先把最小同步请求跑通，价值最大。

## 最小请求到底要传什么？

最小只需要两类核心信息：

### 1. `model`
告诉 Hermes 这次希望走哪个模型。

### 2. `messages`
告诉 Hermes 这次用户到底说了什么。

最小结构可以理解成这样：

```json
{
  "model": "你的模型名",
  "messages": [
    {
      "role": "user",
      "content": "请帮我分析这条订单异常"
    }
  ]
}
```

这已经足够让 Hermes 开始一次最基本的 Agent 执行。

## Spring Boot 里最小调用动作怎么理解？

你可以把它理解成 4 步：

### 第一步：准备 Hermes 地址
比如：
- `http://127.0.0.1:8642/v1`

### 第二步：拼请求体
也就是：
- model
- messages

### 第三步：POST 到 `/v1/chat/completions`
这是最常见入口。

### 第四步：从响应里取最终文本
然后 Spring Boot 再把这个结果包装回自己的业务接口。

## 最小链路适合什么场景？

最适合这些“先验证价值”的场景：

- 智能问答
- 智能摘要
- 智能解释一条异常
- 智能生成一段业务说明
- 智能分析一段文本输入

## 第二步就应该补什么？

最小请求跑通之后，下一步最值得补的是：

### 会话连续性
也就是不要让每次请求都像第一次。

这时 Spring Boot 最好自己持有一个业务会话 id，然后传给 Hermes。
这样 Hermes 才能把多次请求接成一条连续主线。

## 一个很实用的理解方式

你可以把 Spring Boot 调 Hermes 的最小请求理解成：

**Spring Boot 先把 Hermes 当成“能理解任务的 HTTP 服务”接进来。**
