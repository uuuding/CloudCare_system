# 云护CloudCare智慧医养系统 API 接口文档

## 目录

- [1. 接口规范](#1-接口规范)
  - [1.1 请求规范](#11-请求规范)
  - [1.2 响应规范](#12-响应规范)
  - [1.3 状态码](#13-状态码)
- [2. 认证管理](#2-认证管理)
  - [2.1 用户登录](#21-用户登录)
  - [2.2 用户注册](#22-用户注册)
  - [2.3 退出登录](#23-退出登录)
- [3. 用户管理](#3-用户管理)
  - [3.1 获取当前登录用户信息](#31-获取当前登录用户信息)
  - [3.2 更新用户基本信息](#32-更新用户基本信息)
  - [3.3 修改密码](#33-修改密码)
  - [3.4 更新用户头像](#34-更新用户头像)
  - [3.5 分页查询用户列表](#35-分页查询用户列表)
  - [3.6 获取用户详情](#36-获取用户详情)
  - [3.7 新增用户](#37-新增用户)
  - [3.8 更新用户](#38-更新用户)
  - [3.9 删除用户](#39-删除用户)
  - [3.10 重置密码](#310-重置密码)
  - [3.11 更新用户状态](#311-更新用户状态)
- [4. 仪表盘数据](#4-仪表盘数据)
  - [4.1 获取仪表盘统计数据](#41-获取仪表盘统计数据)
  - [4.2 获取健康预警趋势数据](#42-获取健康预警趋势数据)
  - [4.3 获取健康记录分布数据](#43-获取健康记录分布数据)
  - [4.4 获取系统公告列表](#44-获取系统公告列表)
  - [4.5 获取天气信息](#45-获取天气信息)
- [5. 健康预警管理](#5-健康预警管理)
  - [5.1 分页查询健康预警列表](#51-分页查询健康预警列表)
  - [5.2 获取健康预警详情](#52-获取健康预警详情)
  - [5.3 新增健康预警](#53-新增健康预警)
  - [5.4 修改健康预警](#54-修改健康预警)
  - [5.5 删除健康预警](#55-删除健康预警)
  - [5.6 处理健康预警](#56-处理健康预警)
  - [5.7 获取未处理的健康预警数量](#57-获取未处理的健康预警数量)
  - [5.8 获取老人的健康预警列表](#58-获取老人的健康预警列表)
  - [5.9 获取最近的健康预警列表](#59-获取最近的健康预警列表)

## 1. 接口规范

### 1.1 请求规范

- 基础路径: `/api`
- 请求方式: GET, POST, PUT, DELETE
- 请求头:
  - Content-Type: application/json
  - Authorization: Bearer {token} (需要认证的接口)

### 1.2 响应规范

所有接口返回统一的JSON格式：

```json
{
  "success": true,      // 请求是否成功
  "code": 200,         // 状态码
  "message": "操作成功", // 提示信息
  "data": {},          // 返回数据
  "timestamp": 1628756438000 // 时间戳
}
```

### 1.3 状态码

| 状态码 | 说明 |
| ----- | ---- |
| 200 | 成功 |
| 400 | 参数校验失败 |
| 401 | 未认证 |
| 403 | 未授权 |
| 404 | 资源不存在 |
| 405 | 方法不允许 |
| 408 | 请求超时 |
| 500 | 服务器内部错误 |

## 2. 认证管理

### 2.1 用户登录

- **接口URL**: `/api/auth/login`
- **请求方式**: POST
- **接口描述**: 用户登录接口

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| token | String | 认证令牌 |
| userId | Long | 用户ID |
| username | String | 用户名 |
| userType | Integer | 用户类型（1：管理员，2：医生，3：老人） |

#### 请求示例

```http
POST /api/auth/login?username=admin&password=123456
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1,
    "username": "admin",
    "userType": 1
  },
  "timestamp": 1628756438000
}
```

### 2.2 用户注册

- **接口URL**: `/api/auth/register`
- **请求方式**: POST
- **接口描述**: 用户注册接口

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| realName | String | 是 | 真实姓名 |
| phone | String | 是 | 手机号 |
| userType | Integer | 是 | 用户类型（1：管理员，2：医生，3：老人） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 注册结果 |

#### 请求示例

```http
POST /api/auth/register?username=user1&password=123456&realName=张三&phone=13800138000&userType=3
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "注册成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 2.3 退出登录

- **接口URL**: `/api/auth/logout`
- **请求方式**: POST
- **接口描述**: 用户退出登录接口

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 退出结果 |

#### 请求示例

```http
POST /api/auth/logout
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "退出成功",
  "data": true,
  "timestamp": 1628756438000
}
```

## 3. 用户管理

### 3.1 获取当前登录用户信息

- **接口URL**: `/api/user/info`
- **请求方式**: GET
- **接口描述**: 获取当前登录用户的详细信息

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| userId | Long | 用户ID |
| username | String | 用户名 |
| realName | String | 真实姓名 |
| avatar | String | 头像URL |
| phone | String | 手机号 |
| email | String | 邮箱 |
| userType | Integer | 用户类型 |
| status | Integer | 状态（0：禁用，1：正常） |
| roles | Array | 角色列表 |
| permissions | Array | 权限列表 |

#### 请求示例

```http
GET /api/user/info
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "username": "admin",
    "realName": "管理员",
    "avatar": "https://example.com/avatar.jpg",
    "phone": "13800138000",
    "email": "admin@example.com",
    "userType": 1,
    "status": 1,
    "roles": ["ROLE_ADMIN"],
    "permissions": ["system:user:list", "system:user:add"]
  },
  "timestamp": 1628756438000
}
```

### 3.2 更新用户基本信息

- **接口URL**: `/api/user/info`
- **请求方式**: PUT
- **接口描述**: 更新当前登录用户的基本信息

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| realName | String | 否 | 真实姓名 |
| phone | String | 否 | 手机号 |
| email | String | 否 | 邮箱 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/user/info
Content-Type: application/json

{
  "realName": "张三",
  "phone": "13800138001",
  "email": "zhangsan@example.com"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.3 修改密码

- **接口URL**: `/api/user/password`
- **请求方式**: PUT
- **接口描述**: 修改当前登录用户的密码

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| oldPassword | String | 是 | 旧密码 |
| newPassword | String | 是 | 新密码 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 修改结果 |

#### 请求示例

```http
PUT /api/user/password?oldPassword=123456&newPassword=654321
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.4 更新用户头像

- **接口URL**: `/api/user/avatar`
- **请求方式**: PUT
- **接口描述**: 更新当前登录用户的头像

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| avatar | String | 是 | 头像URL |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/user/avatar?avatar=https://example.com/avatar.jpg
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.5 分页查询用户列表

- **接口URL**: `/api/user/page`
- **请求方式**: GET
- **接口描述**: 分页查询用户列表信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| pageNum | Integer | 否 | 页码 | 1 |
| pageSize | Integer | 否 | 每页条数 | 10 |
| username | String | 否 | 用户名 | - |
| phone | String | 否 | 手机号 | - |
| status | Integer | 否 | 状态（0：禁用，1：正常） | - |
| userType | Integer | 否 | 用户类型（1：管理员，2：医生，3：老人） | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| records | Array | 用户列表 |
| total | Long | 总记录数 |
| size | Long | 每页条数 |
| current | Long | 当前页码 |
| pages | Long | 总页数 |

#### 请求示例

```http
GET /api/user/page?pageNum=1&pageSize=10&username=admin
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "userId": 1,
        "username": "admin",
        "realName": "管理员",
        "phone": "13800138000",
        "userType": 1,
        "status": 1
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  },
  "timestamp": 1628756438000
}
```

### 3.6 获取用户详情

- **接口URL**: `/api/user/{userId}`
- **请求方式**: GET
- **接口描述**: 根据用户ID获取用户详情信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| userId | Long | 是 | 用户ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| userId | Long | 用户ID |
| username | String | 用户名 |
| realName | String | 真实姓名 |
| avatar | String | 头像URL |
| phone | String | 手机号 |
| email | String | 邮箱 |
| userType | Integer | 用户类型 |
| status | Integer | 状态 |

#### 请求示例

```http
GET /api/user/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "username": "admin",
    "realName": "管理员",
    "avatar": "https://example.com/avatar.jpg",
    "phone": "13800138000",
    "email": "admin@example.com",
    "userType": 1,
    "status": 1
  },
  "timestamp": 1628756438000
}
```

### 3.7 新增用户

- **接口URL**: `/api/user`
- **请求方式**: POST
- **接口描述**: 新增用户信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| realName | String | 是 | 真实姓名 |
| phone | String | 是 | 手机号 |
| email | String | 否 | 邮箱 |
| userType | Integer | 是 | 用户类型 |
| status | Integer | 否 | 状态 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 新增结果 |

#### 请求示例

```http
POST /api/user
Content-Type: application/json

{
  "username": "user1",
  "password": "123456",
  "realName": "张三",
  "phone": "13800138001",
  "email": "zhangsan@example.com",
  "userType": 3,
  "status": 1
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.8 更新用户

- **接口URL**: `/api/user`
- **请求方式**: PUT
- **接口描述**: 更新用户信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| userId | Long | 是 | 用户ID |
| realName | String | 否 | 真实姓名 |
| phone | String | 否 | 手机号 |
| email | String | 否 | 邮箱 |
| userType | Integer | 否 | 用户类型 |
| status | Integer | 否 | 状态 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/user
Content-Type: application/json

{
  "userId": 1,
  "realName": "管理员",
  "phone": "13800138000",
  "email": "admin@example.com",
  "userType": 1,
  "status": 1
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.9 删除用户

- **接口URL**: `/api/user/{userId}`
- **请求方式**: DELETE
- **接口描述**: 根据用户ID删除用户信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| userId | Long | 是 | 用户ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /api/user/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.10 重置密码

- **接口URL**: `/api/user/reset-password/{userId}`
- **请求方式**: PUT
- **接口描述**: 重置用户密码为默认密码
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| userId | Long | 是 | 用户ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 重置后的密码 |

#### 请求示例

```http
PUT /api/user/reset-password/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "重置密码成功",
  "data": "123456",
  "timestamp": 1628756438000
}
```

### 3.11 更新用户状态

- **接口URL**: `/api/user/status/{userId}/{status}`
- **请求方式**: PUT
- **接口描述**: 更新用户状态（0：禁用，1：正常）
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| userId | Long | 是 | 用户ID |
| status | Integer | 是 | 状态（0：禁用，1：正常） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/user/status/1/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

## 4. 仪表盘数据

### 4.1 获取仪表盘统计数据

- **接口URL**: `/api/dashboard/statistics`
- **请求方式**: GET
- **接口描述**: 获取仪表盘统计数据
- **权限要求**: 需要dashboard:view权限

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| userCount | Integer | 用户总数 |
| elderlyCount | Integer | 老人总数 |
| doctorCount | Integer | 医生总数 |
| warningCount | Integer | 预警总数 |
| todayWarningCount | Integer | 今日预警数 |
| unprocessedWarningCount | Integer | 未处理预警数 |

#### 请求示例

```http
GET /api/dashboard/statistics
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "userCount": 100,
    "elderlyCount": 80,
    "doctorCount": 15,
    "warningCount": 50,
    "todayWarningCount": 5,
    "unprocessedWarningCount": 10
  },
  "timestamp": 1628756438000
}
```

### 4.2 获取健康预警趋势数据

- **接口URL**: `/api/dashboard/warning/trend`
- **请求方式**: GET
- **接口描述**: 获取健康预警趋势数据
- **权限要求**: 需要dashboard:view权限

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| timeRange | String | 否 | 时间范围：week-本周, month-本月, year-全年 | week |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| dates | Array | 日期列表 |
| values | Array | 对应日期的预警数量 |

#### 请求示例

```http
GET /api/dashboard/warning/trend?timeRange=week
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "dates": ["2023-08-01", "2023-08-02", "2023-08-03", "2023-08-04", "2023-08-05", "2023-08-06", "2023-08-07"],
    "values": [5, 8, 3, 10, 7, 4, 6]
  },
  "timestamp": 1628756438000
}
```

### 4.3 获取健康记录分布数据

- **接口URL**: `/api/dashboard/record/distribution`
- **请求方式**: GET
- **接口描述**: 获取健康记录分布数据
- **权限要求**: 需要dashboard:view权限

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| type | String | 否 | 分布类型：type-类型分布, level-等级分布 | type |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| names | Array | 分类名称列表 |
| values | Array | 对应分类的数量 |

#### 请求示例

```http
GET /api/dashboard/record/distribution?type=type
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "names": ["血压异常", "血糖异常", "心率异常", "体温异常", "其他"],
    "values": [25, 18, 15, 10, 5]
  },
  "timestamp": 1628756438000
}
```

### 4.4 获取系统公告列表

- **接口URL**: `/api/dashboard/announcements`
- **请求方式**: GET
- **接口描述**: 获取系统公告列表

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| limit | Integer | 否 | 获取条数 | 5 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 公告ID |
| title | String | 公告标题 |
| content | String | 公告内容 |
| createTime | String | 创建时间 |

#### 请求示例

```http
GET /api/dashboard/announcements?limit=5
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "title": "系统升级通知",
      "content": "系统将于2023年8月10日进行升级维护，请提前做好准备。",
      "createTime": "2023-08-01 10:00:00"
    },
    {
      "id": 2,
      "title": "新功能上线",
      "content": "健康预警功能已上线，欢迎使用。",
      "createTime": "2023-08-02 14:30:00"
    }
  ],
  "timestamp": 1628756438000
}
```

### 4.5 获取天气信息

- **接口URL**: `/api/dashboard/weather`
- **请求方式**: GET
- **接口描述**: 获取天气信息

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| city | String | 否 | 城市名称 | 北京 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| city | String | 城市名称 |
| weather | String | 天气状况 |
| temperature | String | 温度 |
| humidity | String | 湿度 |
| wind | String | 风向风力 |
| updateTime | String | 更新时间 |

#### 请求示例

```http
GET /api/dashboard/weather?city=北京
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "city": "北京",
    "weather": "晴",
    "temperature": "28℃",
    "humidity": "45%",
    "wind": "东北风3级",
    "updateTime": "2023-08-08 14:30:00"
  },
  "timestamp": 1628756438000
}
```

## 5. 健康预警管理

### 5.1 分页查询健康预警列表

- **接口URL**: `/api/health/warning/list`
- **请求方式**: GET
- **接口描述**: 分页查询健康预警列表
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| pageNum | Integer | 否 | 页码 | 1 |
| pageSize | Integer | 否 | 每页条数 | 10 |
| elderId | Long | 否 | 老人ID | - |
| warningType | Integer | 否 | 预警类型 | - |
| warningLevel | Integer | 否 | 预警等级 | - |
| status | Integer | 否 | 处理状态 | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| records | Array | 预警列表 |
| total | Long | 总记录数 |
| size | Long | 每页条数 |
| current | Long | 当前页码 |
| pages | Long | 总页数 |

#### 请求示例

```http
GET /api/health/warning/list?pageNum=1&pageSize=10&warningLevel=3
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "warningId": 1,
        "elderId": 10,
        "elderName": "张老",
        "warningType": 1,
        "warningTypeName": "血压异常",
        "warningLevel": 3,
        "warningLevelName": "严重",
        "warningContent": "血压过高：180/110mmHg",
        "status": 0,
        "statusName": "未处理",
        "createTime": "2023-08-08 10:30:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  },
  "timestamp": 1628756438000
}
```

### 5.2 获取健康预警详情

- **接口URL**: `/api/health/warning/{warningId}`
- **请求方式**: GET
- **接口描述**: 获取健康预警详情
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| warningId | Long | 是 | 预警ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| warningId | Long | 预警ID |
| elderId | Long | 老人ID |
| elderName | String | 老人姓名 |
| warningType | Integer | 预警类型 |
| warningTypeName | String | 预警类型名称 |
| warningLevel | Integer | 预警等级 |
| warningLevelName | String | 预警等级名称 |
| warningContent | String | 预警内容 |
| status | Integer | 处理状态 |
| statusName | String | 处理状态名称 |
| processTime | String | 处理时间 |
| processUserId | Long | 处理人ID |
| processUserName | String | 处理人姓名 |
| processResult | String | 处理结果 |
| createTime | String | 创建时间 |

#### 请求示例

```http
GET /api/health/warning/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "warningId": 1,
    "elderId": 10,
    "elderName": "张老",
    "warningType": 1,
    "warningTypeName": "血压异常",
    "warningLevel": 3,
    "warningLevelName": "严重",
    "warningContent": "血压过高：180/110mmHg",
    "status": 1,
    "statusName": "已处理",
    "processTime": "2023-08-08 11:30:00",
    "processUserId": 2,
    "processUserName": "李医生",
    "processResult": "已联系家属，建议立即就医",
    "createTime": "2023-08-08 10:30:00"
  },
  "timestamp": 1628756438000
}
```

### 5.3 新增健康预警

- **接口URL**: `/api/health/warning`
- **请求方式**: POST
- **接口描述**: 新增健康预警
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderId | Long | 是 | 老人ID |
| warningType | Integer | 是 | 预警类型 |
| warningLevel | Integer | 是 | 预警等级 |
| warningContent | String | 是 | 预警内容 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 新增结果 |

#### 请求示例

```http
POST /api/health/warning
Content-Type: application/json

{
  "elderId": 10,
  "warningType": 1,
  "warningLevel": 3,
  "warningContent": "血压过高：180/110mmHg"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 5.4 修改健康预警

- **接口URL**: `/api/health/warning`
- **请求方式**: PUT
- **接口描述**: 修改健康预警
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| warningId | Long | 是 | 预警ID |
| warningType | Integer | 否 | 预警类型 |
| warningLevel | Integer | 否 | 预警等级 |
| warningContent | String | 否 | 预警内容 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 修改结果 |

#### 请求示例

```http
PUT /api/health/warning
Content-Type: application/json

{
  "warningId": 1,
  "warningLevel": 2,
  "warningContent": "血压偏高：160/100mmHg"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 5.5 删除健康预警

- **接口URL**: `/api/health/warning/{warningIds}`
- **请求方式**: DELETE
- **接口描述**: 删除健康预警
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| warningIds | Long[] | 是 | 预警ID数组 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /api/health/warning/1,2,3
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 5.6 处理健康预警

- **接口URL**: `/api/health/warning/process`
- **请求方式**: PUT
- **接口描述**: 处理健康预警
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| warningId | Long | 是 | 预警ID |
| processResult | String | 是 | 处理结果 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 处理结果 |

#### 请求示例

```http
PUT /api/health/warning/process
Content-Type: application/json

{
  "warningId": 1,
  "processResult": "已联系家属，建议立即就医"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 5.7 获取未处理的健康预警数量

- **接口URL**: `/api/health/warning/unprocessed/count`
- **请求方式**: GET
- **接口描述**: 获取未处理的健康预警数量
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Integer | 未处理的预警数量 |

#### 请求示例

```http
GET /api/health/warning/unprocessed/count
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": 5,
  "timestamp": 1628756438000
}
```

### 5.8 获取老人的健康预警列表

- **接口URL**: `/api/health/warning/elder/{elderId}`
- **请求方式**: GET
- **接口描述**: 获取老人的健康预警列表
- **权限要求**: 管理员、医生、护士、老人或家属角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderId | Long | 是 | 老人ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 预警列表 |

#### 请求示例

```http
GET /api/health/warning/elder/10
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "warningId": 1,
      "elderId": 10,
      "elderName": "张老",
      "warningType": 1,
      "warningTypeName": "血压异常",
      "warningLevel": 3,
      "warningLevelName": "严重",
      "warningContent": "血压过高：180/110mmHg",
      "status": 1,
      "statusName": "已处理",
      "processTime": "2023-08-08 11:30:00",
      "processResult": "已联系家属，建议立即就医",
      "createTime": "2023-08-08 10:30:00"
    }
  ],
  "timestamp": 1628756438000
}
```

### 5.9 获取最近的健康预警列表

- **接口URL**: `/api/health/warning/recent/{limit}`
- **请求方式**: GET
- **接口描述**: 获取最近的健康预警列表
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| limit | Integer | 是 | 获取条数 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 预警列表 |

#### 请求示例

```http
GET /api/health/warning/recent/5
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "warningId": 1,
      "elderId": 10,
      "elderName": "张老",
      "warningType": 1,
      "warningTypeName": "血压异常",
      "warningLevel": 3,
      "warningLevelName": "严重",
      "warningContent": "血压过高：180/110mmHg",
      "status": 1,
      "statusName": "已处理",
      "createTime": "2023-08-08 10:30:00"
    }
  ],
  "timestamp": 1628756438000
}
```