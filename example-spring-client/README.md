# example-spring-client

这是一个最小可运行的 Spring Boot Maven 示例工程，用来演示：

- Spring Boot 如何调用 Hermes API Server
- Spring Boot 如何把事件推送给 Hermes Webhook
- 同时提供 `RestTemplate` 与 `WebClient` 两种实现
- Hermes 地址从 `application.yml` 读取

## 1. 前置条件

先确保 Hermes 已暴露可访问接口，例如：

- API Server：`http://127.0.0.1:8642/v1`
- Webhook：`http://127.0.0.1:8642/webhooks/{route}`

## 2. 配置

默认配置文件：

- `src/main/resources/application.yml`

```yaml
server:
  port: 8080

hermes:
  base-url: http://127.0.0.1:8642/v1
  model: hermes
```

## 3. 启动

先确保 Maven 使用的是 Java 17 或更高版本。

如果当前机器执行 `mvn -v` 仍显示 Java 8，可以先临时切换：

```powershell
$env:JAVA_HOME = 'D:\worksoft\jdk20Install'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn spring-boot:run
```

如果本机已经正确配置了 Java 17+，也可以直接执行：

```bash
mvn spring-boot:run
```

## 4. 调用本地示例接口

### 4.1 RestTemplate 调 Chat Completions

```bash
curl -X POST "http://127.0.0.1:8080/demo/hermes/chat" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"请分析这条业务告警并给出处理建议"}'
```

### 4.2 RestTemplate 调 Webhook

```bash
curl -X POST "http://127.0.0.1:8080/demo/hermes/webhook" \
  -H "Content-Type: application/json" \
  -d '{"route":"business-alert","event":"order_failed","message":"支付回调失败"}'
```

### 4.3 WebClient 调 Chat Completions

```bash
curl -X POST "http://127.0.0.1:8080/demo/hermes/chat-webclient" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"请用 WebClient 方式转发到 Hermes"}'
```

### 4.4 WebClient 调 Webhook

```bash
curl -X POST "http://127.0.0.1:8080/demo/hermes/webhook-webclient" \
  -H "Content-Type: application/json" \
  -d '{"route":"business-alert","event":"order_failed","message":"请用 WebClient 转发 webhook"}'
```

## 5. 工程结构

- `service/HermesApiService.java`：`RestTemplate` 版封装
- `service/HermesWebClientService.java`：`WebClient` 版封装
- `controller/HermesDemoController.java`：演示接口入口
- `dto/HermesPromptRequest.java`：聊天入参
- `dto/WebhookEventRequest.java`：Webhook 入参