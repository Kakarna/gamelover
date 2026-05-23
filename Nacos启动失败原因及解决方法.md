# Nacos 启动失败原因及解决方法总结

## 一、问题概述

在 Docker 环境下部署 Nacos 2.x 时，连接 MySQL 8.0 数据库时遇到多种问题，包括认证失败、数据库权限不足、数据源配置问题等，导致 Nacos 容器反复启动失败。

---

## 二、主要原因

### 1. MySQL 用户认证插件不兼容

**错误信息：**
```
com.mysql.cj.exceptions.CJException: Public Key Retrieval is not allowed
```

**原因：** MySQL 8.0 默认使用 `caching_sha2_password` 认证插件，而 Nacos 使用的 JDBC 驱动不支持该插件的公钥检索机制。

---

### 2. MySQL 连接参数配置不完整

**原因：** Nacos 连接 MySQL 时，`allowPublicKeyRetrieval` 参数未正确传递或被覆盖。

---

### 3. Docker 网络配置问题

**原因：** Nacos 容器无法通过服务名 `mysql` 解析到 MySQL 容器，需要自定义网络。

---

### 4. Nacos 版本与数据库配置不匹配

**原因：** 使用 `latest` 标签拉取的 Nacos 镜像可能与本地 MySQL 版本存在兼容性问题。

---

### 5. 数据库权限不足

**错误信息：**
```
Caused by: java.lang.IllegalStateException: No DataSource set
```

**原因：** `gamelover` 用户对 `nacos_config` 数据库没有足够的权限，导致无法读取或写入数据。

---

### 6. Nacos 与业务数据共用同一数据库

**原因：** 最初 Nacos 配置表与业务表都放在 `gamelover` 数据库中，不利于管理和维护。应该将 Nacos 配置表单独放在 `nacos_config` 数据库中。

---

### 7. 旧容器状态异常

**原因：** 旧的 Nacos 容器已经处于异常状态，需要完全删除并重新创建。

---

## 三、解决方法

### 1. 修改 MySQL 认证插件

```sql
ALTER USER 'gamelover'@'%' IDENTIFIED WITH mysql_native_password BY 'gamelover123';
FLUSH PRIVILEGES;
```

或创建用户时指定：
```sql
CREATE USER 'gamelover'@'%' IDENTIFIED WITH mysql_native_password BY 'gamelover123';
GRANT ALL PRIVILEGES ON gamelover.* TO 'gamelover'@'%';
FLUSH PRIVILEGES;
```

---

### 2. 配置 MySQL 服务端允许公钥检索

在 MySQL 配置文件 `my.cnf` 中添加：
```ini
[mysqld]
default-authentication-plugin=mysql_native_password
allow-public-key-retrieval=true
```

---

### 3. 完善 Nacos 连接参数

在 `docker-compose.yml` 中配置完整的连接参数：
```yaml
nacos:
  environment:
    - MYSQL_SERVICE_DB_PARAM: characterEncoding=utf8&connectTimeout=3000&socketTimeout=5000&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai
```

---

### 4. 使用自定义 Docker 网络

```yaml
services:
  nacos:
    depends_on:
      - mysql
    networks:
      - gamelover-network
  mysql:
    networks:
      - gamelover-network

networks:
  gamelover-network:
    driver: bridge
```

---

### 5. 指定稳定版本的 Nacos 镜像

```yaml
nacos:
  image: nacos/nacos-server:v2.2.3
```

---

### 6. 关闭 Nacos 认证（开发环境）

```yaml
nacos:
  environment:
    - NACOS_AUTH_ENABLE=false
```

---

### 7. 为 MySQL 用户授予数据库权限

```sql
-- 授予 gamelover 用户对 nacos_config 数据库的全部权限
GRANT ALL PRIVILEGES ON nacos_config.* TO 'gamelover'@'%';
FLUSH PRIVILEGES;
```

---

### 8. 创建独立的 nacos_config 数据库并导入表结构

```powershell
# 1. 删除旧的 nacos_config 数据库并重新创建
docker exec gamelover-mysql mysql -uroot -proot123 -e "DROP DATABASE IF EXISTS nacos_config; CREATE DATABASE nacos_config DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 授予权限
docker exec gamelover-mysql mysql -uroot -proot123 -e "GRANT ALL PRIVILEGES ON nacos_config.* TO 'gamelover'@'%'; FLUSH PRIVILEGES;"

# 3. 导入 Nacos 表结构
docker cp d:\MyTotalProject\GameLover\sql\mysql-schema.sql gamelover-mysql:/tmp/
docker exec gamelover-mysql mysql -uroot -proot123 -e "source /tmp/mysql-schema.sql"
```

---

### 9. 删除并重新创建 Nacos 容器

```powershell
# 删除旧的 Nacos 容器
docker rm -f gamelover-nacos

# 使用 docker-compose 重新启动
docker-compose up -d nacos

# 等待并查看日志
Start-Sleep -Seconds 10
docker logs gamelover-nacos --tail 100
```

---

## 四、最终有效配置

### docker-compose.yml 关键配置

```yaml
services:
  nacos:
    image: nacos/nacos-server:v2.2.3
    container_name: gamelover-nacos
    environment:
      - MODE=standalone
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_DB_NAME=nacos_config  # Nacos 使用独立的数据库
      - MYSQL_SERVICE_USER=gamelover
      - MYSQL_SERVICE_PASSWORD=gamelover123
      - MYSQL_SERVICE_DB_PARAM=characterEncoding=utf8&connectTimeout=3000&socketTimeout=5000&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai
      - NACOS_AUTH_ENABLE=false
    volumes:
      - ./data/nacos/logs:/home/nacos/logs
    ports:
      - "8848:8848"
      - "9848:9848"
    depends_on:
      - mysql
    networks:
      - gamelover-network

  mysql:
    image: mysql:8.0
    container_name: gamelover-mysql
    environment:
      - MYSQL_ROOT_PASSWORD=root123
      - MYSQL_DATABASE=gamelover  # 业务数据库
      - MYSQL_USER=gamelover
      - MYSQL_PASSWORD=gamelover123
      - MYSQL_ROOT_HOST=%
      - TZ=Asia/Shanghai
    volumes:
      - ./data/mysql:/var/lib/mysql
      - ./sql:/docker-entrypoint-initdb.d
      - ./conf/my.cnf:/etc/mysql/conf.d/my.cnf
    ports:
      - "3306:3306"
    networks:
      - gamelover-network

networks:
  gamelover-network:
    driver: bridge
```

### my.cnf 配置

```ini
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
default-authentication-plugin=mysql_native_password
skip-name-resolve
allow-public-key-retrieval=true
max_connections=500
```

---

## 五、排查命令

```powershell
# 查看容器状态
docker ps -a | Select-String nacos

# 查看日志
docker logs gamelover-nacos --tail 50

# 查看完整启动日志
docker logs gamelover-nacos

# 重启容器
docker-compose restart nacos

# 检查MySQL用户
docker exec gamelover-mysql mysql -uroot -proot123 -e "SELECT user, host, plugin FROM mysql.user WHERE user='gamelover'"

# 检查数据库权限
docker exec gamelover-mysql mysql -uroot -proot123 -e "SHOW GRANTS FOR 'gamelover'@'%'"

# 检查 nacos_config 数据库是否存在
docker exec gamelover-mysql mysql -uroot -proot123 -e "SHOW DATABASES"

# 检查 nacos_config 数据库中的表
docker exec gamelover-mysql mysql -uroot -proot123 -e "USE nacos_config; SHOW TABLES"

# 测试 gamelover 用户是否能正常连接到 nacos_config 数据库
docker exec gamelover-mysql mysql -ugamelover -pgamelover123 -e "USE nacos_config; SELECT COUNT(*) FROM config_info"

# 删除并重新创建 Nacos 容器（终极方案）
docker rm -f gamelover-nacos; docker-compose up -d nacos
```

---

## 六、完整问题解决流程（2026-05-24 案例）

本次遇到的问题及解决步骤：

### 问题现象
Nacos 启动失败，日志显示 `Caused by: java.lang.IllegalStateException: No DataSource set`

### 解决步骤
1. **检查数据库权限** - 发现 `gamelover` 用户没有 `nacos_config` 数据库权限
2. **重新初始化数据库** - 完全删除并重新创建 `nacos_config` 数据库
3. **导入 Nacos 表结构** - 执行 `mysql-schema.sql` 导入完整表结构
4. **授予完整权限** - 给 `gamelover` 用户授予 `nacos_config` 数据库的所有权限
5. **删除旧容器并重启** - 彻底删除 Nacos 容器并重新创建

### 验证结果
- ✅ Nacos 成功启动，日志显示 `Nacos started successfully in stand alone mode. use external storage`
- ✅ 访问 http://localhost:8848/nacos 正常（账号密码：nacos/nacos）

