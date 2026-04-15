# 学习资料导航

这里仅保留与 **Hermes 和外部服务通讯** 直接相关的文档，便于集中学习 Spring Boot / 外部系统如何与 Hermes 集成。

## 目录

- `hermes-communication/`
  只保留外部通讯相关原理、机制、使用与实战文档。

## 推荐阅读顺序

1. `hermes-communication/01-hermes是什么与用途.md`
2. `hermes-communication/02-hermes如何使用与适用场景.md`
3. `hermes-communication/03-hermes架构设计.md`
4. `hermes-communication/04-hermes运行机制与底层加载原理.md`
5. `hermes-communication/14-hermes-外部服务通讯源码解读.md`
6. `hermes-communication/15-hermes-对接springboot实战说明.md`

## 适合关注的重点

- Hermes 对外暴露服务时，优先看 API Server 路径
- 外部系统事件接入 Hermes 时，优先看 Webhook 路径
- Hermes 主动调用外部服务时，优先看 MCP 路径
- 如果要结合 Spring Boot 落地，重点看 14 和 15 两份文档