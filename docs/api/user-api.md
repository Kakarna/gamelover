# 用户服务接口文档

## 一、接口概述

本文档定义 GameLover 平台用户服务（user-service）的所有接口。

**服务地址**：`http://localhost:8081`

**接口版本**：v1

**认证方式**：JWT Token（在 Header 中携带 `Authorization: Bearer {token}`）

---

## 二、接口列表

| 序号 | 接口名称 | 接口路径 | 请求方法 | 需要认证 |
|------|---------|---------|---------|---------|
| 1 | 用户注册 | `/api/v1/user/register` | POST | 否 |
| 2 | 用户登录 | `/api/v1/user/login` | POST | 否 |
| 3 | 获取当前用户信息 | `/api/v1/user/info` | GET | 是 |
| 4 | 更新用户信息 | `/api/v1/user/update` | PUT | 是 |
| 5 | 修改密码 | `/api/v1/user/password` | PUT | 是 |
| 6 | 绑定游戏账号 | `/api/v1/user/game-account/bind` | POST | 是 |
| 7 | 解绑游戏账号 | `/api/v1/user/game-account/unbind` | DELETE | 是 |
| 8 | 获取游戏账号列表 | `/api/v1/user/game-account/list` | GET | 是 |

---

## 三、详细接口定义

---

### 1. 用户注册

#### 接口基本信息

| 项目 | 内容 |
|------|------|
| 接口名称 | 用户注册 |
| 接口路径 | `/api/v1/user/register` |
| 请求方法 | POST |
| Content-Type | application/json |
| 是否需要认证 | 否 |

#### 请求参数（Body）

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| username | String | 是 | 用户名（3-20字符，字母数字下划线） | admin |
| password | String | 是 | 密码（6-20字符） | 123456 |
| email | String | 否 | 邮箱 | admin@example.com |
| phone | String | 否 | 手机号 | 13800138000 |
| nickname | String | 否 | 昵称（默认等于用户名） | 管理员 |

#### 响应参数

| 参数名 | 类型 | 说明 |
|--------|------|------|
| userUid | Long | 用户UID |
| username | String | 用户名 |
| nickname | String | 昵称 |

#### 请求示例

```bash
curl -X POST http://localhost:8081/api/v1/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456",
    "email": "admin@example.com",
    "nickname": "管理员"
  }'
```

#### 响应示例

##### 成功响应

```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userUid": 10001,
    "username": "admin",
    "nickname": "管理员"
  },
  "timestamp": 1716547200000
}
```

##### 失败响应

```json
{
  "code": 10002,
  "message": "用户名已存在",
  "data": null,
  "timestamp": 1716547200000
}
```

#### 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 注册成功 |
| 10002 | 用户名已存在 |
| 400 | 参数错误（格式不正确） |

---

### 2. 用户登录

#### 接口基本信息

| 项目 | 内容 |
|------|------|
| 接口名称 | 用户登录 |
| 接口路径 | `/api/v1/user/login` |
| 请求方法 | POST |
| Content-Type | application/json |
| 是否需要认证 | 否 |

#### 请求参数（Body）

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| account | String | 是 | 账号（用户名/邮箱/手机号） | admin |
| password | String | 是 | 密码（原始密码） | 123456 |

#### 响应参数

| 参数名 | 类型 | 说明 |
|--------|------|------|
| token | String | JWT Token |
| userInfo | Object | 用户信息 |
| └── userUid | Long | 用户UID |
| └── username | String | 用户名 |
| └── nickname | String | 昵称 |
| └── avatarUrl | String | 头像URL |

#### 请求示例

```bash
curl -X POST http://localhost:8081/api/v1/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "account": "admin",
    "password": "123456"
  }'
```

#### 响应示例

##### 成功响应

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "userUid": 10001,
      "username": "admin",
      "nickname": "管理员",
      "avatarUrl": "https://example.com/avatar.jpg"
    }
  },
  "timestamp": 1716547200000
}
```

##### 失败响应

```json
{
  "code": 10003,
  "message": "密码错误",
  "data": null,
  "timestamp": 1716547200000
}
```

#### 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 登录成功 |
| 10001 | 用户不存在 |
| 10003 | 密码错误 |
| 400 | 参数错误 |

#### 注意事项

1. 密码传输为原始密码，后端使用 BCrypt 验证
2. Token 有效期为 7 天
3. 登录成功后，后续请求需要在 Header 中携带 Token

---

### 3. 获取当前用户信息

#### 接口基本信息

| 项目 | 内容 |
|------|------|
| 接口名称 | 获取当前用户信息 |
| 接口路径 | `/api/v1/user/info` |
| 请求方法 | GET |
| Content-Type | - |
| 是否需要认证 | 是 |

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 说明 |
|--------|------|------|
| userUid | Long | 用户UID |
| username | String | 用户名 |
| nickname | String | 昵称 |
| avatarUrl | String | 头像URL |
| email | String | 邮箱 |
| phone | String | 手机号 |
| registerType | String | 注册类型 |
| lastLoginTime | String | 最后登录时间 |
| status | Integer | 状态（0-禁用，1-正常） |

#### 请求示例

```bash
curl -X GET http://localhost:8081/api/v1/user/info \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### 响应示例

##### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userUid": 10001,
    "username": "admin",
    "nickname": "管理员",
    "avatarUrl": "https://example.com/avatar.jpg",
    "email": "admin@example.com",
    "phone": "13800138000",
    "registerType": "LOCAL",
    "lastLoginTime": "2026-05-24 12:00:00",
    "status": 1
  },
  "timestamp": 1716547200000
}
```

##### 失败响应

```json
{
  "code": 401,
  "message": "未登录或Token已过期",
  "data": null,
  "timestamp": 1716547200000
}
```

#### 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 获取成功 |
| 401 | 未登录或Token已过期 |

---

### 4. 更新用户信息

#### 接口基本信息

| 项目 | 内容 |
|------|------|
| 接口名称 | 更新用户信息 |
| 接口路径 | `/api/v1/user/update` |
| 请求方法 | PUT |
| Content-Type | application/json |
| 是否需要认证 | 是 |

#### 请求参数（Body）

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| nickname | String | 否 | 昵称 | 新昵称 |
| avatarUrl | String | 否 | 头像URL | https://example.com/new-avatar.jpg |
| email | String | 否 | 邮箱 | new@example.com |
| phone | String | 否 | 手机号 | 13900139000 |

#### 响应参数

| 参数名 | 类型 | 说明 |
|--------|------|------|
| userUid | Long | 用户UID |
| username | String | 用户名 |
| nickname | String | 昵称 |
| avatarUrl | String | 头像URL |
| email | String | 邮箱 |
| phone | String | 手机号 |

#### 请求示例

```bash
curl -X PUT http://localhost:8081/api/v1/user/update \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "nickname": "新昵称",
    "avatarUrl": "https://example.com/new-avatar.jpg"
  }'
```

#### 响应示例

##### 成功响应

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "userUid": 10001,
    "username": "admin",
    "nickname": "新昵称",
    "avatarUrl": "https://example.com/new-avatar.jpg",
    "email": "admin@example.com",
    "phone": "13800138000"
  },
  "timestamp": 1716547200000
}
```

#### 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 更新成功 |
| 400 | 参数错误 |
| 401 | 未登录 |

---

### 5. 修改密码

#### 接口基本信息

| 项目 | 内容 |
|------|------|
| 接口名称 | 修改密码 |
| 接口路径 | `/api/v1/user/password` |
| 请求方法 | PUT |
| Content-Type | application/json |
| 是否需要认证 | 是 |

#### 请求参数（Body）

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| oldPassword | String | 是 | 旧密码 | 123456 |
| newPassword | String | 是 | 新密码（6-20字符） | 654321 |

#### 响应参数

无（返回成功即可）

#### 请求示例

```bash
curl -X PUT http://localhost:8081/api/v1/user/password \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "123456",
    "newPassword": "654321"
  }'
```

#### 响应示例

##### 成功响应

```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null,
  "timestamp": 1716547200000
}
```

##### 失败响应

```json
{
  "code": 10003,
  "message": "旧密码错误",
  "data": null,
  "timestamp": 1716547200000
}
```

#### 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 修改成功 |
| 10003 | 旧密码错误 |
| 400 | 参数错误 |

---

### 6. 绑定游戏账号

#### 接口基本信息

| 项目 | 内容 |
|------|------|
| 接口名称 | 绑定游戏账号 |
| 接口路径 | `/api/v1/user/game-account/bind` |
| 请求方法 | POST |
| Content-Type | application/json |
| 是否需要认证 | 是 |

#### 请求参数（Body）

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| gameCode | String | 是 | 游戏编码 | WUWA |
| gameUid | String | 是 | 游戏内UID | 123456789 |
| gameNickname | String | 否 | 游戏内昵称 | 漂泊者 |
| serverId | String | 否 | 服务器ID | os_abcd_001 |
| serverName | String | 否 | 服务器名称 | 手机游戏-A |
| authToken | String | 否 | API认证Token | - |
| authCookie | String | 否 | Cookie | - |

#### 响应参数

| 参数名 | 类型 | 说明 |
|--------|------|------|
| bindUid | Long | 绑定UID |
| gameCode | String | 游戏编码 |
| gameUid | String | 游戏内UID |
| gameNickname | String | 游戏内昵称 |
| isMain | Integer | 是否主账号 |

#### 请求示例

```bash
curl -X POST http://localhost:8081/api/v1/user/game-account/bind \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "gameCode": "WUWA",
    "gameUid": "123456789",
    "gameNickname": "漂泊者",
    "serverId": "os_abcd_001",
    "serverName": "手机游戏-A"
  }'
```

#### 响应示例

##### 成功响应

```json
{
  "code": 200,
  "message": "绑定成功",
  "data": {
    "bindUid": 1001,
    "gameCode": "WUWA",
    "gameUid": "123456789",
    "gameNickname": "漂泊者",
    "isMain": 1
  },
  "timestamp": 1716547200000
}
```

#### 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 绑定成功 |
| 20001 | 游戏账号已绑定 |
| 400 | 参数错误 |

---

### 7. 解绑游戏账号

#### 接口基本信息

| 项目 | 内容 |
|------|------|
| 接口名称 | 解绑游戏账号 |
| 接口路径 | `/api/v1/user/game-account/unbind` |
| 请求方法 | DELETE |
| 是否需要认证 | 是 |

#### 请求参数（Query）

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| bindUid | Long | 是 | 绑定UID | 1001 |

#### 响应参数

无

#### 请求示例

```bash
curl -X DELETE "http://localhost:8081/api/v1/user/game-account/unbind?bindUid=1001" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### 响应示例

##### 成功响应

```json
{
  "code": 200,
  "message": "解绑成功",
  "data": null,
  "timestamp": 1716547200000
}
```

#### 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 解绑成功 |
| 404 | 绑定记录不存在 |

---

### 8. 获取游戏账号列表

#### 接口基本信息

| 项目 | 内容 |
|------|------|
| 接口名称 | 获取游戏账号列表 |
| 接口路径 | `/api/v1/user/game-account/list` |
| 请求方法 | GET |
| 是否需要认证 | 是 |

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 说明 |
|--------|------|------|
| bindUid | Long | 绑定UID |
| gameCode | String | 游戏编码 |
| gameUid | String | 游戏内UID |
| gameNickname | String | 游戏内昵称 |
| serverId | String | 服务器ID |
| serverName | String | 服务器名称 |
| isMain | Integer | 是否主账号 |
| syncStatus | Integer | 同步状态（0-暂停，1-正常） |
| lastSyncTime | String | 最后同步时间 |
| createTime | String | 绑定时间 |

#### 请求示例

```bash
curl -X GET http://localhost:8081/api/v1/user/game-account/list \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### 响应示例

##### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "bindUid": 1001,
      "gameCode": "WUWA",
      "gameUid": "123456789",
      "gameNickname": "漂泊者",
      "serverId": "os_abcd_001",
      "serverName": "手机游戏-A",
      "isMain": 1,
      "syncStatus": 1,
      "lastSyncTime": "2026-05-24 12:00:00",
      "createTime": "2026-05-20 10:00:00"
    }
  ],
  "timestamp": 1716547200000
}
```

---

## 四、错误码汇总

### 系统级错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或Token过期 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 业务级错误码

| 错误码 | 说明 |
|--------|------|
| 10001 | 用户不存在 |
| 10002 | 用户名已存在 |
| 10003 | 密码错误 |
| 20001 | 游戏账号已绑定 |
| 20002 | 游戏账号未绑定 |

---

## 五、认证说明

### Token 获取

1. 调用 `/api/v1/user/login` 接口
2. 登录成功后获取 Token
3. 在后续请求的 Header 中携带 Token

### Header 格式

```
Authorization: Bearer {token}
```

### 示例

```bash
curl -X GET http://localhost:8081/api/v1/user/info \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 六、游戏编码说明

| 游戏编码 | 游戏名称 |
|---------|---------|
| WUWA | 鸣潮（Wuthering Waves） |
| HSR | 崩坏：星穹铁道 |
| Genshin | 原神 |

---

## 七、文档信息

| 项目 | 内容 |
|------|------|
| 文档版本 | v1.0 |
| 创建日期 | 2026-05-24 |
| 最后更新 | 2026-05-24 |
| 负责人 | - |
