# example-spring-mcp-server

这是一个最小 Spring Boot MCP 风格示例服务，用来帮助理解：

- Spring Boot 如何把业务能力暴露给 Hermes
- Hermes 通过 HTTP MCP 方式接入外部工具时，服务端大致需要提供什么形态
- 多个工具如何共享同一套内存数据状态

## 1. 启动

先确保 Maven 使用的是 Java 17 或更高版本。

```powershell
$env:JAVA_HOME = 'D:\worksoft\jdk20Install'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn spring-boot:run
```

服务默认端口：

- `http://127.0.0.1:8090/mcp`

## 2. 当前示例提供的方法与工具

方法：

- `initialize`
- `tools/list`
- `tools/call`
- `ping`

工具：

- `query_order`
- `query_user`
- `approve_order`

## 3. 当前内存数据表

用户：

- `U20001` -> Alice / VIP / LOW
- `U20002` -> Bob / NORMAL / MEDIUM

订单：

- `A10086` -> `U20001` / `PENDING_APPROVAL`
- `A10087` -> `U20002` / `PAID`

## 4. 推荐演示顺序

先查订单：

```bash
curl -X POST "http://127.0.0.1:8090/mcp" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"query_order","arguments":{"orderId":"A10086"}}}'
```

再审批订单：

```bash
curl -X POST "http://127.0.0.1:8090/mcp" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"approve_order","arguments":{"orderId":"A10086","approver":"finance_manager"}}}'
```

最后再次查订单：

```bash
curl -X POST "http://127.0.0.1:8090/mcp" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"query_order","arguments":{"orderId":"A10086"}}}'
```

你会看到 `A10086` 的状态从 `PENDING_APPROVAL` 变成 `APPROVED`，并且 `approvedBy` 被更新。

## 5. 其他调用示例

查询用户：

```bash
curl -X POST "http://127.0.0.1:8090/mcp" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"query_user","arguments":{"userId":"U20001"}}}'
```

查看工具列表：

```bash
curl -X POST "http://127.0.0.1:8090/mcp" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":5,"method":"tools/list","params":{}}'
```

## 6. Hermes 侧接入思路

在 Hermes 的 `mcp_servers` 配置里，把这个 Spring Boot 服务地址配置成 HTTP MCP 服务，然后 Hermes 就能把这些业务能力当作外部工具使用。

## 7. 说明

这是一个最小教学示例，重点是帮助理解 Spring Boot 如何暴露外部工具能力给 Hermes。生产接入时，还需要按你实际采用的 MCP 规范、鉴权方式和部署方式继续补充。