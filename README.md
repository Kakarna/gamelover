# GameLover 游戏数据聚合平台

## 项目简介

GameLover 是一个基于微服务架构的游戏数据聚合平台，目前支持鸣潮（Wuthering Waves）的游戏数据查询与分析。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7 + Spring Cloud 2021
- **服务注册/发现**: Nacos
- **API网关**: Spring Cloud Gateway
- **ORM**: MyBatis-Plus
- **数据库**: MySQL 8.0 + Redis + MongoDB
- **消息队列**: Kafka
- **分布式追踪**: Sleuth + Zipkin
- **API文档**: Knife4j
- **容器化**: Docker

### 前端（待开发）
- **框架**: Vue 3 + TypeScript + Vite
- **UI**: Naive UI（二次元风格）

## 项目结构

```
gamelover
├── gamelover-common              # 公共模块
│   ├── result                   # 统一返回结果
│   ├── exception                # 全局异常处理
│   ├── entity                   # 基础实体类
│   └── config                   # 通用配置
├── gamelover-gateway            # API网关服务（端口 8080）
├── gamelover-user-service       # 用户服务（端口 8081）
├── gamelover-game-data-service  # 游戏数据服务（端口 8082）
├── gamelover-sync-service       # 数据同步服务（端口 8083）
├── nacos-config                 # Nacos配置文件示例
├── docker-compose.yml           # Docker编排文件
└── pom.xml                      # 父级POM
```

## 快速开始

### 1. 启动基础设施

```bash
docker-compose up -d
```

这将启动以下服务：
- Nacos: http://localhost:8848/nacos (nacos/nacos)
- MySQL: localhost:3306 (root/root123)
- Redis: localhost:6379
- MongoDB: localhost:27017 (admin/admin123)
- Kafka: localhost:29092
- Zipkin: http://localhost:9411

### 2. 配置Nacos

将 `nacos-config` 目录下的配置文件导入到Nacos配置中心。

### 3. 构建项目

```bash
mvn clean package -DskipTests
```

### 4. 启动服务

按以下顺序启动服务：
1. gamelover-gateway
2. gamelover-user-service
3. gamelover-game-data-service
4. gamelover-sync-service

### 5. 访问API文档

- 网关Swagger: http://localhost:8080/doc.html

## 开发顺序建议

1. **先调研星穹铁道API**：了解米游社API或官方API的数据结构
2. **设计数据库表**：基于API返回的数据设计MySQL和MongoDB的存储结构
3. **开发sync-service**：实现数据同步功能，对接游戏API
4. **开发game-data-service**：实现游戏数据的查询和分析功能
5. **开发user-service**：实现用户管理功能
6. **开发前端**：基于后端接口开发二次元风格的前端界面

## 注意事项

- 游戏API对接是关键，需要先研究清楚米游社的API接口
- 抽卡记录、角色练度等数据建议存储在MongoDB中
- 用户信息等结构化数据存储在MySQL中
- 热点数据使用Redis缓存

---

## 开发规范

为了保证代码质量和开发效率，请遵循以下规范：

| 文档 | 说明 |
|------|------|
| [开发规范总览.md](./开发规范总览.md) | 项目整体规范 |
| [Git分支管理规范.md](./Git分支管理规范.md) | Git分支和PR流程 |
| [代码提交规范.md](./代码提交规范.md) | Git Commit 格式 |
| [开发流程规范.md](./开发流程规范.md) | 完整开发流程 |
| [接口文档规范.md](./接口文档规范.md) | API文档编写 |
| [项目启动清单.md](./项目启动清单.md) | 新环境启动检查 |
| [CHANGELOG.md](./CHANGELOG.md) | 版本变更日志 |
| [技术选型.md](./技术选型.md) | 技术栈说明 |
| [Nacos启动失败原因及解决方法.md](./Nacos启动失败原因及解决方法.md) | 常见问题排查 |

---

## 许可证

MIT License
