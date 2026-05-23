# Docker镜像版本（兼容版本）

## 基础设施镜像

| 服务 | 镜像 | 版本 | 说明 |
|------|------|------|------|
| Nacos | nacos/nacos-server | v2.2.3 | 服务注册与配置中心 |
| MySQL | mysql | 8.0.33 | 关系型数据库 |
| Redis | redis | 7.2-alpine | 缓存数据库（轻量） |
| MongoDB | mongo | 6.0.8 | 文档数据库 |
| Kafka | confluentinc/cp-kafka | 7.4.0 | 消息队列 |
| Zookeeper | confluentinc/cp-zookeeper | 7.4.0 | Kafka依赖 |
| Zipkin | openzipkin/zipkin | 2.24 | 分布式追踪 |

## 拉取命令

```powershell
# Nacos（推荐先拉取）
docker pull nacos/nacos-server:v2.2.3

# MySQL
docker pull mysql:8.0.33

# Redis（使用alpine轻量版）
docker pull redis:7.2-alpine

# MongoDB
docker pull mongo:6.0.8

# Kafka + Zookeeper（必须版本匹配）
docker pull confluentinc/cp-kafka:7.4.0
docker pull confluentinc/cp-zookeeper:7.4.0

# Zipkin
docker pull openzipkin/zipkin:2.24
```

## 快速拉取全部镜像

```powershell
docker pull nacos/nacos-server:v2.2.3 `
&& docker pull mysql:8.0.33 `
&& docker pull redis:7.2-alpine `
&& docker pull mongo:6.0.8 `
&& docker pull confluentinc/cp-kafka:7.4.0 `
&& docker pull confluentinc/cp-zookeeper:7.4.0 `
&& docker pull openzipkin/zipkin:2.24
```

## 版本兼容性说明

- **Nacos v2.2.3**: 支持Spring Boot 2.7.x，兼容Java 11
- **MySQL 8.0.33**: 与Nacos完美兼容，支持UTF8MB4字符集
- **Redis 7.2**: 最新稳定版，轻量级alpine镜像
- **MongoDB 6.0.8**: 稳定版，支持副本集
- **Kafka 7.4.0**: 与Spring Boot 2.7.x兼容
- **Zipkin 2.24**: 支持MySQL存储

## 拉取完成后

镜像拉取完成后，再次运行：

```powershell
cd "d:\MyTotalProject\GameLover"
docker-compose up -d
```
