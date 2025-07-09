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
- [6. GPS定位管理](#6-gps定位管理)
  - [6.1 接收GPS数据推送](#61-接收gps数据推送)
  - [6.2 获取老人轨迹数据](#62-获取老人轨迹数据)
  - [6.3 获取老人最新位置](#63-获取老人最新位置)
  - [6.4 绑定GPS设备](#64-绑定gps设备)
  - [6.5 获取设备绑定列表](#65-获取设备绑定列表)
  - [6.6 解绑GPS设备](#66-解绑gps设备)
- [7. 电子围栏管理](#7-电子围栏管理)
  - [7.1 创建围栏](#71-创建围栏)
  - [7.2 更新围栏](#72-更新围栏)
  - [7.3 删除围栏](#73-删除围栏)
  - [7.4 获取围栏列表](#74-获取围栏列表)
  - [7.5 获取围栏详情](#75-获取围栏详情)
  - [7.6 获取老人的围栏列表](#76-获取老人的围栏列表)

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
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1628756438000
}
```

### 1.3 状态码

| 状态码 | 说明 |
| ----- | ---- |
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

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
- [6. GPS定位管理](#6-gps定位管理)
  - [6.1 接收GPS数据推送](#61-接收gps数据推送)
  - [6.2 获取老人轨迹数据](#62-获取老人轨迹数据)
  - [6.3 获取老人最新位置](#63-获取老人最新位置)
  - [6.4 绑定GPS设备](#64-绑定gps设备)
  - [6.5 获取设备绑定列表](#65-获取设备绑定列表)
  - [6.6 解绑GPS设备](#66-解绑gps设备)
- [7. 电子围栏管理](#7-电子围栏管理)
  - [7.1 创建围栏](#71-创建围栏)
  - [7.2 更新围栏](#72-更新围栏)
  - [7.3 删除围栏](#73-删除围栏)
  - [7.4 获取围栏列表](#74-获取围栏列表)
  - [7.5 获取围栏详情](#75-获取围栏详情)
  - [7.6 获取老人的围栏列表](#76-获取老人的围栏列表)

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
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1628756438000
}
```

### 1.3 状态码

| 状态码 | 说明 |
| ----- | ---- |
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 2. 认证管理

### 2.1 用户登录

- **接口URL**: `/api/auth/login`
- **请求方式**: POST
- **接口描述**: 用户登录接口

#### 请求参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/user/status/1/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "更新成功",
  "data": true,
  "timestamp": 1628756438000
}
```

## 4. 仪表盘数据

### 4.1 获取仪表盘统计数据

- **接口URL**: `/api/dashboard/stats`
- **请求方式**: GET
- **接口描述**: 获取仪表盘统计数据
- **权限要求**: 需要认证

#### 请求参数

无

### 7.2 更新围栏

- **接口URL**: `/api/fence/update/{id}`
- **请求方式**: PUT
- **接口描述**: 更新电子围栏
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 围栏ID（路径参数） |
| name | String | 否 | 围栏名称 |
| type | Integer | 否 | 围栏类型（1：圆形，2：多边形） |
| center | Object | 否 | 中心点（圆形围栏必须） |
| radius | Double | 否 | 半径（圆形围栏必须，单位：米） |
| points | Array | 否 | 多边形顶点列表（多边形围栏必须） |
| description | String | 否 | 描述 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/fence/update/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "活动范围（更新）",
  "type": 1,
  "center": {
    "latitude": 39.904989,
    "longitude": 116.405285
  },
  "radius": 2000,
  "description": "老人日常活动范围（已更新）"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "更新成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 7.3 删除围栏

- **接口URL**: `/api/fence/delete/{id}`
- **请求方式**: DELETE
- **接口描述**: 删除电子围栏
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 围栏ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /api/fence/delete/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "删除成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 7.4 获取围栏列表

- **接口URL**: `/api/fence/list`
- **请求方式**: GET
- **接口描述**: 获取电子围栏列表
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| page | Integer | 否 | 页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |
| elderlyId | Long | 否 | 老人ID | - |
| type | Integer | 否 | 围栏类型 | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| current | Long | 当前页码 |
| size | Long | 每页大小 |
| records | Array | 围栏列表 |

#### 请求示例

```http
GET /api/fence/list?page=1&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "pages": 10,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "elderlyId": 1,
        "elderlyName": "张三",
        "name": "活动范围",
        "type": 1,
        "center": {
          "latitude": 39.904989,
          "longitude": 116.405285
        },
        "radius": 1000,
        "points": null,
        "description": "老人日常活动范围",
        "createTime": "2023-08-08 10:00:00",
        "updateTime": "2023-08-08 14:30:00"
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 7.5 获取围栏详情

- **接口URL**: `/api/fence/{id}`
- **请求方式**: GET
- **接口描述**: 获取电子围栏详情
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 围栏ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 围栏ID |
| elderlyId | Long | 老人ID |
| elderlyName | String | 老人姓名 |
| name | String | 围栏名称 |
| type | Integer | 围栏类型（1：圆形，2：多边形） |
| center | Object | 中心点（圆形围栏） |
| radius | Double | 半径（圆形围栏，单位：米） |
| points | Array | 多边形顶点列表（多边形围栏） |
| description | String | 描述 |
| createTime | String | 创建时间 |
| updateTime | String | 更新时间 |

#### 请求示例

```http
GET /api/fence/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "elderlyId": 1,
    "elderlyName": "张三",
    "name": "活动范围",
    "type": 1,
    "center": {
      "latitude": 39.904989,
      "longitude": 116.405285
    },
    "radius": 1000,
    "points": null,
    "description": "老人日常活动范围",
    "createTime": "2023-08-08 10:00:00",
    "updateTime": "2023-08-08 14:30:00"
  },
  "timestamp": 1628756438000
}
```

### 7.6 获取老人的围栏列表

- **接口URL**: `/api/fence/elderly/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 获取指定老人的电子围栏列表
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 围栏ID |
| name | String | 围栏名称 |
| type | Integer | 围栏类型（1：圆形，2：多边形） |
| center | Object | 中心点（圆形围栏） |
| radius | Double | 半径（圆形围栏，单位：米） |
| points | Array | 多边形顶点列表（多边形围栏） |
| description | String | 描述 |
| createTime | String | 创建时间 |

#### 请求示例

```http
GET /api/fence/elderly/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
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
      "name": "活动范围",
      "type": 1,
      "center": {
        "latitude": 39.904989,
        "longitude": 116.405285
      },
      "radius": 1000,
      "points": null,
      "description": "老人日常活动范围",
      "createTime": "2023-08-08 10:00:00"
    }
  ],
  "timestamp": 1628756438000
}
```

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| elderlyCount | Integer | 老人总数 |
| deviceCount | Integer | 设备总数 |
| alertCount | Integer | 预警总数 |
| serviceCount | Integer | 服务总数 |

#### 请求示例

```http
GET /api/dashboard/stats
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "elderlyCount": 100,
    "deviceCount": 50,
    "alertCount": 10,
    "serviceCount": 20
  },
  "timestamp": 1628756438000
}
```

### 4.2 获取健康预警趋势数据

- **接口URL**: `/api/dashboard/alert-trend`
- **请求方式**: GET
- **接口描述**: 获取健康预警趋势数据
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| days | Integer | 否 | 天数 | 7 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| dates | Array | 日期列表 |
| counts | Array | 预警数量列表 |

#### 请求示例

```http
GET /api/dashboard/alert-trend?days=7
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "dates": ["2023-08-01", "2023-08-02", "2023-08-03", "2023-08-04", "2023-08-05", "2023-08-06", "2023-08-07"],
    "counts": [5, 3, 7, 2, 4, 6, 1]
  },
  "timestamp": 1628756438000
}
```

### 4.3 获取健康记录分布数据

- **接口URL**: `/api/dashboard/health-distribution`
- **请求方式**: GET
- **接口描述**: 获取健康记录分布数据
- **权限要求**: 需要认证

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| types | Array | 类型列表 |
| values | Array | 数量列表 |

#### 请求示例

```http
GET /api/dashboard/health-distribution
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "types": ["血压", "血糖", "心率", "体温", "其他"],
    "values": [30, 25, 20, 15, 10]
  },
  "timestamp": 1628756438000
}
```

### 4.4 获取系统公告列表

- **接口URL**: `/api/dashboard/notices`
- **请求方式**: GET
- **接口描述**: 获取系统公告列表
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| size | Integer | 否 | 获取条数 | 5 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 公告ID |
| title | String | 标题 |
| content | String | 内容 |
| type | Integer | 类型（1：通知，2：公告，3：提醒） |
| createTime | String | 创建时间 |

#### 请求示例

```http
GET /api/dashboard/notices?size=5
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
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
      "content": "系统将于今晚22:00-24:00进行升级维护",
      "type": 1,
      "createTime": "2023-08-08 10:00:00"
    }
  ],
  "timestamp": 1628756438000
}
```

### 4.5 获取天气信息

- **接口URL**: `/api/dashboard/weather`
- **请求方式**: GET
- **接口描述**: 获取天气信息
- **权限要求**: 需要认证

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| city | String | 城市 |
| weather | String | 天气 |
| temperature | String | 温度 |
| humidity | String | 湿度 |
| wind | String | 风向风力 |
| updateTime | String | 更新时间 |

#### 请求示例

```http
GET /api/dashboard/weather
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
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
    "temperature": "25℃",
    "humidity": "45%",
    "wind": "东北风3级",
    "updateTime": "2023-08-08 14:30:00"
  },
  "timestamp": 1628756438000
}
```

## 5. 健康预警管理

### 5.1 分页查询健康预警列表

- **接口URL**: `/api/health-alert/list`
- **请求方式**: GET
- **接口描述**: 分页查询健康预警列表
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| page | Integer | 否 | 页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |
| elderlyId | Long | 否 | 老人ID | - |
| type | Integer | 否 | 预警类型 | - |
| status | Integer | 否 | 处理状态（0：未处理，1：已处理） | - |
| startTime | String | 否 | 开始时间 | - |
| endTime | String | 否 | 结束时间 | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| current | Long | 当前页码 |
| size | Long | 每页大小 |
| records | Array | 预警列表 |

#### 请求示例

```http
GET /api/health-alert/list?page=1&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "pages": 10,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "elderlyId": 1,
        "elderlyName": "张三",
        "type": 1,
        "typeName": "血压异常",
        "content": "血压偏高：150/95mmHg",
        "status": 0,
        "createTime": "2023-08-08 10:00:00",
        "handleTime": null,
        "handleUser": null,
        "handleResult": null
      }
    ]
  },
  "timestamp": 1628756438000
}
``` 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
### 5.2 获取健康预警详情

- **接口URL**: `/api/health-alert/{id}`
- **请求方式**: GET
- **接口描述**: 获取健康预警详情
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 预警ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 预警ID |
| elderlyId | Long | 老人ID |
| elderlyName | String | 老人姓名 |
| type | Integer | 预警类型 |
| typeName | String | 预警类型名称 |
| content | String | 预警内容 |
| status | Integer | 处理状态（0：未处理，1：已处理） |
| createTime | String | 创建时间 |
| handleTime | String | 处理时间 |
| handleUser | String | 处理人 |
| handleResult | String | 处理结果 |

#### 请求示例

```http
GET /api/health-alert/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "elderlyId": 1,
    "elderlyName": "张三",
    "type": 1,
    "typeName": "血压异常",
    "content": "血压偏高：150/95mmHg",
    "status": 0,
    "createTime": "2023-08-08 10:00:00",
    "handleTime": null,
    "handleUser": null,
    "handleResult": null
  },
  "timestamp": 1628756438000
}
```

### 5.3 新增健康预警

- **接口URL**: `/api/health-alert/add`
- **请求方式**: POST
- **接口描述**: 新增健康预警
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |
| type | Integer | 是 | 预警类型 |
| content | String | 是 | 预警内容 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 添加结果 |

#### 请求示例

```http
POST /api/health-alert/add
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "elderlyId": 1,
  "type": 1,
  "content": "血压偏高：150/95mmHg"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "添加成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 5.4 修改健康预警

- **接口URL**: `/api/health-alert/update/{id}`
- **请求方式**: PUT
- **接口描述**: 修改健康预警
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 预警ID（路径参数） |
| type | Integer | 否 | 预警类型 |
| content | String | 否 | 预警内容 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 修改结果 |

#### 请求示例

```http
PUT /api/health-alert/update/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "type": 1,
  "content": "血压偏高：160/100mmHg"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "修改成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 5.5 删除健康预警

- **接口URL**: `/api/health-alert/delete/{id}`
- **请求方式**: DELETE
- **接口描述**: 删除健康预警
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 预警ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /api/health-alert/delete/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "删除成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 5.6 处理健康预警

- **接口URL**: `/api/health-alert/handle/{id}`
- **请求方式**: PUT
- **接口描述**: 处理健康预警
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 预警ID（路径参数） |
| handleResult | String | 是 | 处理结果 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 处理结果 |

#### 请求示例

```http
PUT /api/health-alert/handle/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "handleResult": "已联系家属，建议就医检查"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "处理成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 5.7 获取未处理的健康预警数量

- **接口URL**: `/api/health-alert/unhandled-count`
- **请求方式**: GET
- **接口描述**: 获取未处理的健康预警数量
- **权限要求**: 需要认证

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| count | Integer | 未处理数量 |

#### 请求示例

```http
GET /api/health-alert/unhandled-count
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "count": 5
  },
  "timestamp": 1628756438000
}
```

### 5.8 获取老人的健康预警列表

- **接口URL**: `/api/health-alert/elderly/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 获取指定老人的健康预警列表
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| elderlyId | Long | 是 | 老人ID（路径参数） | - |
| page | Integer | 否 | 页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |
| status | Integer | 否 | 处理状态（0：未处理，1：已处理） | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| current | Long | 当前页码 |
| size | Long | 每页大小 |
| records | Array | 预警列表 |

#### 请求示例

```http
GET /api/health-alert/elderly/1?page=1&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 50,
    "pages": 5,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "elderlyId": 1,
        "elderlyName": "张三",
        "type": 1,
        "typeName": "血压异常",
        "content": "血压偏高：150/95mmHg",
        "status": 0,
        "createTime": "2023-08-08 10:00:00",
        "handleTime": null,
        "handleUser": null,
        "handleResult": null
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 5.9 获取最近的健康预警列表

- **接口URL**: `/api/health-alert/recent`
- **请求方式**: GET
- **接口描述**: 获取最近的健康预警列表
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| size | Integer | 否 | 获取条数 | 5 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 预警ID |
| elderlyId | Long | 老人ID |
| elderlyName | String | 老人姓名 |
| type | Integer | 预警类型 |
| typeName | String | 预警类型名称 |
| content | String | 预警内容 |
| status | Integer | 处理状态（0：未处理，1：已处理） |
| createTime | String | 创建时间 |

#### 请求示例

```http
GET /api/health-alert/recent?size=5
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
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
      "elderlyId": 1,
      "elderlyName": "张三",
      "type": 1,
      "typeName": "血压异常",
      "content": "血压偏高：150/95mmHg",
      "status": 0,
      "createTime": "2023-08-08 10:00:00"
    }
  ],
  "timestamp": 1628756438000
}
```

## 6. GPS定位管理

### 6.1 接收GPS数据推送

- **接口URL**: `/api/gps/push`
- **请求方式**: POST
- **接口描述**: 接收GPS设备推送的定位数据
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| deviceId | String | 是 | 设备ID |
| latitude | Double | 是 | 纬度 |
| longitude | Double | 是 | 经度 |
| altitude | Double | 否 | 海拔 |
| speed | Double | 否 | 速度 |
| direction | Double | 否 | 方向 |
| timestamp | Long | 是 | 时间戳 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 处理结果 |

#### 请求示例

```http
POST /api/gps/push
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "deviceId": "GPS001",
  "latitude": 39.904989,
  "longitude": 116.405285,
  "altitude": 44.5,
  "speed": 0.0,
  "direction": 0.0,
  "timestamp": 1628756438000
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "处理成功",
  "data": true,
  "timestamp": 1628756438000
}
```
### 6.2 获取老人轨迹数据

- **接口URL**: `/api/gps/track/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 获取指定老人的轨迹数据
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| elderlyId | Long | 是 | 老人ID（路径参数） | - |
| startTime | String | 是 | 开始时间 | - |
| endTime | String | 是 | 结束时间 | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| points | Array | 轨迹点列表 |

#### 请求示例

```http
GET /api/gps/track/1?startTime=2023-08-08 00:00:00&endTime=2023-08-08 23:59:59
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "points": [
      {
        "latitude": 39.904989,
        "longitude": 116.405285,
        "altitude": 44.5,
        "speed": 0.0,
        "direction": 0.0,
        "timestamp": 1628756438000
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 6.3 获取老人最新位置

- **接口URL**: `/api/gps/location/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 获取指定老人的最新位置
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| latitude | Double | 纬度 |
| longitude | Double | 经度 |
| altitude | Double | 海拔 |
| speed | Double | 速度 |
| direction | Double | 方向 |
| timestamp | Long | 时间戳 |
| address | String | 地址 |

#### 请求示例

```http
GET /api/gps/location/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "latitude": 39.904989,
    "longitude": 116.405285,
    "altitude": 44.5,
    "speed": 0.0,
    "direction": 0.0,
    "timestamp": 1628756438000,
    "address": "北京市东城区东长安街1号"
  },
  "timestamp": 1628756438000
}
```

### 6.4 绑定GPS设备

- **接口URL**: `/api/gps/bind`
- **请求方式**: POST
- **接口描述**: 绑定GPS设备
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |
| deviceId | String | 是 | 设备ID |
| deviceName | String | 是 | 设备名称 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 绑定结果 |

#### 请求示例

```http
POST /api/gps/bind
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "elderlyId": 1,
  "deviceId": "GPS001",
  "deviceName": "手环"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "绑定成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 6.5 获取设备绑定列表

- **接口URL**: `/api/gps/bind/list`
- **请求方式**: GET
- **接口描述**: 获取设备绑定列表
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| page | Integer | 否 | 页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |
| elderlyId | Long | 否 | 老人ID | - |
| deviceId | String | 否 | 设备ID | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| current | Long | 当前页码 |
| size | Long | 每页大小 |
| records | Array | 绑定列表 |

#### 请求示例

```http
GET /api/gps/bind/list?page=1&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "pages": 10,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "elderlyId": 1,
        "elderlyName": "张三",
        "deviceId": "GPS001",
        "deviceName": "手环",
        "bindTime": "2023-08-08 10:00:00",
        "status": 1
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 6.6 解绑GPS设备

- **接口URL**: `/api/gps/unbind/{id}`
- **请求方式**: DELETE
- **接口描述**: 解绑GPS设备
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 绑定ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 解绑结果 |

#### 请求示例

```http
DELETE /api/gps/unbind/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "解绑成功",
  "data": true,
  "timestamp": 1628756438000
}
```

## 7. 电子围栏管理

### 7.1 创建围栏

- **接口URL**: `/api/fence/create`
- **请求方式**: POST
- **接口描述**: 创建电子围栏
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |
| name | String | 是 | 围栏名称 |
| type | Integer | 是 | 围栏类型（1：圆形，2：多边形） |
| center | Object | 否 | 中心点（圆形围栏必须） |
| radius | Double | 否 | 半径（圆形围栏必须，单位：米） |
| points | Array | 否 | 多边形顶点列表（多边形围栏必须） |
| description | String | 否 | 描述 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 围栏ID |

#### 请求示例

```http
POST /api/fence/create
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "elderlyId": 1,
  "name": "活动范围",
  "type": 1,
  "center": {
    "latitude": 39.904989,
    "longitude": 116.405285
  },
  "radius": 1000,
  "description": "老人日常活动范围"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1
  },
  "timestamp": 1628756438000
}
```

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| token | String | 访问令牌 |
| refreshToken | String | 刷新令牌 |
| expiresIn | Long | 过期时间（秒） |

#### 请求示例

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200
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
| confirmPassword | String | 是 | 确认密码 |
| realName | String | 是 | 真实姓名 |
| phone | String | 是 | 手机号 |
| email | String | 否 | 邮箱 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 注册结果 |

#### 请求示例

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "zhangsan",
  "password": "123456",
  "confirmPassword": "123456",
  "realName": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com"
}
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
- **权限要求**: 需要认证

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 退出结果 |

#### 请求示例

```http
POST /api/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
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
- **权限要求**: 需要认证

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 用户ID |
| username | String | 用户名 |
| realName | String | 真实姓名 |
| avatar | String | 头像URL |
| phone | String | 手机号 |
| email | String | 邮箱 |
| roles | Array | 角色列表 |
| permissions | Array | 权限列表 |
| createTime | String | 创建时间 |
| updateTime | String | 更新时间 |

#### 请求示例

```http
GET /api/user/info
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "管理员",
    "avatar": "https://example.com/avatar.jpg",
    "phone": "13800138000",
    "email": "admin@example.com",
    "roles": ["ROLE_ADMIN"],
    "permissions": ["user:add", "user:edit", "user:delete"],
    "createTime": "2023-08-01 10:00:00",
    "updateTime": "2023-08-08 14:30:00"
  },
  "timestamp": 1628756438000
}
```

### 3.2 更新用户基本信息

- **接口URL**: `/api/user/update`
- **请求方式**: PUT
- **接口描述**: 更新当前登录用户的基本信息
- **权限要求**: 需要认证

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
PUT /api/user/update
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "realName": "张三",
  "phone": "13900139000",
  "email": "zhangsan@example.com"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "更新成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.3 修改密码

- **接口URL**: `/api/user/password`
- **请求方式**: PUT
- **接口描述**: 修改当前登录用户的密码
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| oldPassword | String | 是 | 原密码 |
| newPassword | String | 是 | 新密码 |
| confirmPassword | String | 是 | 确认密码 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 修改结果 |

#### 请求示例

```http
PUT /api/user/password
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "oldPassword": "123456",
  "newPassword": "654321",
  "confirmPassword": "654321"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "密码修改成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.4 更新用户头像

- **接口URL**: `/api/user/avatar`
- **请求方式**: POST
- **接口描述**: 更新当前登录用户的头像
- **权限要求**: 需要认证

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| file | File | 是 | 头像文件（图片） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| url | String | 头像URL |

#### 请求示例

```http
POST /api/user/avatar
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: multipart/form-data

[二进制文件数据]
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "上传成功",
  "data": {
    "url": "https://example.com/avatar.jpg"
  },
  "timestamp": 1628756438000
}
```

### 3.5 分页查询用户列表

- **接口URL**: `/api/user/list`
- **请求方式**: GET
- **接口描述**: 分页查询用户列表
- **权限要求**: 管理员

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| page | Integer | 否 | 页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |
| username | String | 否 | 用户名 | - |
| realName | String | 否 | 真实姓名 | - |
| phone | String | 否 | 手机号 | - |
| status | Integer | 否 | 状态（0：禁用，1：启用） | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| current | Long | 当前页码 |
| size | Long | 每页大小 |
| records | Array | 用户列表 |

#### 请求示例

```http
GET /api/user/list?page=1&size=10&username=admin
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "pages": 10,
    "current": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "username": "admin",
        "realName": "管理员",
        "avatar": "https://example.com/avatar.jpg",
        "phone": "13800138000",
        "email": "admin@example.com",
        "status": 1,
        "createTime": "2023-08-01 10:00:00",
        "updateTime": "2023-08-08 14:30:00"
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 3.6 获取用户详情

- **接口URL**: `/api/user/{id}`
- **请求方式**: GET
- **接口描述**: 获取指定用户的详细信息
- **权限要求**: 管理员

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 用户ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 用户ID |
| username | String | 用户名 |
| realName | String | 真实姓名 |
| avatar | String | 头像URL |
| phone | String | 手机号 |
| email | String | 邮箱 |
| status | Integer | 状态（0：禁用，1：启用） |
| roles | Array | 角色列表 |
| createTime | String | 创建时间 |
| updateTime | String | 更新时间 |

#### 请求示例

```http
GET /api/user/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "管理员",
    "avatar": "https://example.com/avatar.jpg",
    "phone": "13800138000",
    "email": "admin@example.com",
    "status": 1,
    "roles": ["ROLE_ADMIN"],
    "createTime": "2023-08-01 10:00:00",
    "updateTime": "2023-08-08 14:30:00"
  },
  "timestamp": 1628756438000
}
```

### 3.7 新增用户

- **接口URL**: `/api/user/add`
- **请求方式**: POST
- **接口描述**: 新增用户
- **权限要求**: 管理员

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| realName | String | 是 | 真实姓名 |
| phone | String | 是 | 手机号 |
| email | String | 否 | 邮箱 |
| roles | Array | 是 | 角色列表 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 添加结果 |

#### 请求示例

```http
POST /api/user/add
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "username": "zhangsan",
  "password": "123456",
  "realName": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "roles": ["ROLE_USER"]
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "添加成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.8 更新用户

- **接口URL**: `/api/user/update/{id}`
- **请求方式**: PUT
- **接口描述**: 更新指定用户信息
- **权限要求**: 管理员

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 用户ID（路径参数） |
| realName | String | 否 | 真实姓名 |
| phone | String | 否 | 手机号 |
| email | String | 否 | 邮箱 |
| roles | Array | 否 | 角色列表 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/user/update/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "realName": "张三",
  "phone": "13900139000",
  "email": "zhangsan@example.com",
  "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "更新成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.9 删除用户

- **接口URL**: `/api/user/delete/{id}`
- **请求方式**: DELETE
- **接口描述**: 删除指定用户
- **权限要求**: 管理员

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 用户ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /api/user/delete/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "删除成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 3.10 重置密码

- **接口URL**: `/api/user/reset-password/{id}`
- **请求方式**: PUT
- **接口描述**: 重置指定用户的密码
- **权限要求**: 管理员

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 用户ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| password | String | 新密码 |

#### 请求示例

```http
PUT /api/user/reset-password/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "重置成功",
  "data": {
    "password": "123456"
  },
  "timestamp": 1628756438000
}
```

### 3.11 更新用户状态

- **接口URL**: `/api/user/status/{id}/{status}`
- **请求方式**: PUT
- **接口描述**: 更新指定用户的状态
- **权限要求**: 管理员

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 用户ID（路径参数） |
| status | Integer | 是 | 状态（0：禁用，1：启用）（路径参数） |

#### 响应参数

| 参数名 | 类型 |