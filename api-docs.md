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

```

## 6. 电子围栏管理

### 6.1 创建围栏

- **接口URL**: `/api/geo-fence/create`
- **请求方式**: POST
- **接口描述**: 创建新的电子围栏
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |
| fenceName | String | 是 | 围栏名称 |
| fenceType | Integer | 是 | 围栏类型（1：圆形，2：多边形） |
| centerLat | Double | 是 | 中心点纬度 |
| centerLng | Double | 是 | 中心点经度 |
| radius | Integer | 否 | 半径（米，圆形围栏必填） |
| coordinates | String | 否 | 坐标点（多边形围栏必填） |
| enterAlert | Boolean | 是 | 进入提醒 |
| exitAlert | Boolean | 是 | 离开提醒 |
| alertType | Integer | 是 | 提醒方式（1：短信，2：应用推送，3：两者都有） |
| emergencyContacts | String | 否 | 紧急联系人手机号（多个用逗号分隔） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 创建结果 |

#### 请求示例

```http
POST /api/geo-fence/create
Content-Type: application/json

{
  "elderlyId": 123,
  "fenceName": "家庭围栏",
  "fenceType": 1,
  "centerLat": 39.9042,
  "centerLng": 116.4074,
  "radius": 500,
  "enterAlert": true,
  "exitAlert": true,
  "alertType": 3,
  "emergencyContacts": "13800138000,13900139000"
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

### 6.2 更新围栏

- **接口URL**: `/api/geo-fence/update/{id}`
- **请求方式**: PUT
- **接口描述**: 更新指定的电子围栏
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Long | 是 | 围栏ID（路径参数） |
| 其他参数 | - | - | 同创建围栏 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/geo-fence/update/1
Content-Type: application/json

{
  "elderlyId": 123,
  "fenceName": "更新后的围栏",
  "fenceType": 1,
  "centerLat": 39.9042,
  "centerLng": 116.4074,
  "radius": 600,
  "enterAlert": true,
  "exitAlert": true,
  "alertType": 3,
  "emergencyContacts": "13800138000"
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

### 6.3 删除围栏

- **接口URL**: `/api/geo-fence/delete/{fenceId}`
- **请求方式**: DELETE
- **接口描述**: 删除指定的电子围栏
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| fenceId | Long | 是 | 围栏ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /api/geo-fence/delete/1
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

### 6.4 分页查询所有围栏

- **接口URL**: `/api/geo-fence/list`
- **请求方式**: GET
- **接口描述**: 分页查询所有电子围栏
- **权限要求**: 需要登录

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| page | Integer | 否 | 页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |

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
GET /api/geo-fence/list?page=1&size=10
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
        "fenceId": 1,
        "elderlyId": 123,
        "fenceName": "家庭围栏",
        "fenceType": 1,
        "centerLat": 39.9042,
        "centerLng": 116.4074,
        "radius": 500,
        "status": 1,
        "createTime": "2024-01-15T10:30:00"
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 6.5 根据老人ID查询围栏

- **接口URL**: `/api/geo-fence/list/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 查询指定老人的所有围栏
- **权限要求**: 需要登录

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 围栏列表 |

#### 请求示例

```http
GET /api/geo-fence/list/123
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "fenceId": 1,
      "elderlyId": 123,
      "fenceName": "家庭围栏",
      "fenceType": 1,
      "centerLat": 39.9042,
      "centerLng": 116.4074,
      "radius": 500,
      "status": 1
    }
  ],
  "timestamp": 1628756438000
}
```

### 6.6 查询围栏详情

- **接口URL**: `/api/geo-fence/detail/{fenceId}`
- **请求方式**: GET
- **接口描述**: 查询指定围栏的详细信息
- **权限要求**: 需要登录

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| fenceId | Long | 是 | 围栏ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| fenceId | Long | 围栏ID |
| elderlyId | Long | 老人ID |
| fenceName | String | 围栏名称 |
| fenceType | Integer | 围栏类型 |
| centerLat | Double | 中心点纬度 |
| centerLng | Double | 中心点经度 |
| radius | Integer | 半径 |
| coordinates | String | 坐标点 |
| status | Integer | 状态 |
| enterAlert | Boolean | 进入提醒 |
| exitAlert | Boolean | 离开提醒 |
| alertType | Integer | 提醒方式 |
| emergencyContacts | String | 紧急联系人 |
| createTime | String | 创建时间 |
| updateTime | String | 更新时间 |

#### 请求示例

```http
GET /api/geo-fence/detail/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "fenceId": 1,
    "elderlyId": 123,
    "fenceName": "家庭围栏",
    "fenceType": 1,
    "centerLat": 39.9042,
    "centerLng": 116.4074,
    "radius": 500,
    "coordinates": "39.9042,116.4074;39.9052,116.4084",
    "status": 1,
    "enterAlert": true,
    "exitAlert": true,
    "alertType": 3,
    "emergencyContacts": "13800138000,13900139000",
    "createTime": "2024-01-15T10:30:00",
    "updateTime": "2024-01-15T10:30:00"
  },
  "timestamp": 1628756438000
}
```

### 6.7 查询围栏事件记录

- **接口URL**: `/api/geo-fence/events/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 查询指定老人的围栏事件记录
- **权限要求**: 需要登录

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| elderlyId | Long | 是 | 老人ID | - |
| page | Integer | 否 | 页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| current | Long | 当前页码 |
| size | Long | 每页大小 |
| records | Array | 事件列表 |

#### 请求示例

```http
GET /api/geo-fence/events/123?page=1&size=10
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 20,
    "pages": 2,
    "current": 1,
    "size": 10,
    "records": [
      {
        "eventId": 1,
        "elderlyId": 123,
        "fenceId": 1,
        "eventType": "EXIT",
        "eventTime": "2024-01-15T10:30:00",
        "latitude": 39.9042,
        "longitude": 116.4074,
        "isRead": false
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 6.8 获取老人当前位置

- **接口URL**: `/api/geo-fence/location/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 获取指定老人的当前位置
- **权限要求**: 需要登录

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| elderlyId | Long | 老人ID |
| latitude | Double | 纬度 |
| longitude | Double | 经度 |
| address | String | 地址 |
| updateTime | String | 更新时间 |

#### 请求示例

```http
GET /api/geo-fence/location/123
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "elderlyId": 123,
    "latitude": 39.9042,
    "longitude": 116.4074,
    "address": "北京市朝阳区",
    "updateTime": "2024-01-15T10:30:00"
  },
  "timestamp": 1628756438000
}
```

## 7. 短信服务管理

### 7.1 发送普通短信

- **接口URL**: `/api/sms/send`
- **请求方式**: POST
- **接口描述**: 发送普通短信
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| phone | String | 是 | 手机号 |
| content | String | 是 | 短信内容 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 发送结果 |
| messageId | String | 消息ID |
| message | String | 结果描述 |

#### 请求示例

```http
POST /api/sms/send
Content-Type: application/json

{
  "phone": "13800138000",
  "content": "您好，这是一条测试短信。"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "success": true,
    "messageId": "SMS_123456789",
    "message": "短信发送成功"
  },
  "timestamp": 1628756438000
}
```

### 7.2 批量发送短信

- **接口URL**: `/api/sms/send/batch`
- **请求方式**: POST
- **接口描述**: 批量发送短信
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| phones | Array | 是 | 手机号列表 |
| content | String | 是 | 短信内容 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| successCount | Integer | 成功数量 |
| failCount | Integer | 失败数量 |
| results | Array | 详细结果 |

#### 请求示例

```http
POST /api/sms/send/batch
Content-Type: application/json

{
  "phones": ["13800138000", "13900139000"],
  "content": "您好，这是一条批量短信。"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "successCount": 2,
    "failCount": 0,
    "results": [
      {
        "phone": "13800138000",
        "success": true,
        "messageId": "SMS_123456789"
      },
      {
        "phone": "13900139000",
        "success": true,
        "messageId": "SMS_123456790"
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 7.3 发送模板短信

- **接口URL**: `/api/sms/send/template`
- **请求方式**: POST
- **接口描述**: 根据模板发送短信
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| phone | String | 是 | 手机号 |
| templateCode | String | 是 | 模板代码 |
| templateParams | Object | 否 | 模板参数 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 发送结果 |
| messageId | String | 消息ID |
| message | String | 结果描述 |

#### 请求示例

```http
POST /api/sms/send/template
Content-Type: application/json

{
  "phone": "13800138000",
  "templateCode": "EMERGENCY_ALERT",
  "templateParams": {
    "elderlyName": "张三",
    "location": "北京市朝阳区",
    "time": "2024-01-15 10:30"
  }
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "success": true,
    "messageId": "SMS_123456789",
    "message": "短信发送成功"
  },
  "timestamp": 1628756438000
}
```

### 7.4 查询账户余额

- **接口URL**: `/api/sms/balance`
- **请求方式**: GET
- **接口描述**: 查询短信账户余额
- **权限要求**: 管理员角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| balance | Integer | 余额（条数） |
| lastUpdateTime | String | 最后更新时间 |

#### 请求示例

```http
GET /api/sms/balance
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "balance": 1000,
    "lastUpdateTime": "2024-01-15T10:30:00"
  },
  "timestamp": 1628756438000
}
```

### 7.5 获取短信发送记录

- **接口URL**: `/api/sms/records`
- **请求方式**: GET
- **接口描述**: 获取短信发送记录
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| page | Integer | 否 | 页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |
| phone | String | 否 | 手机号 | - |
| startTime | String | 否 | 开始时间 | - |
| endTime | String | 否 | 结束时间 | - |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| current | Long | 当前页码 |
| size | Long | 每页大小 |
| records | Array | 发送记录列表 |

#### 请求示例

```http
GET /api/sms/records?page=1&size=10&phone=13800138000
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
        "phone": "13800138000",
        "content": "您好，这是一条测试短信。",
        "status": "SUCCESS",
        "messageId": "SMS_123456789",
        "sendTime": "2024-01-15T10:30:00",
        "deliveryTime": "2024-01-15T10:30:05"
      }
    ]
  },
  "timestamp": 1628756438000
}
```

## 8. 老人档案管理

### 8.1 查询所有老人档案

- **接口URL**: `/api/elderly-profile/index`
- **请求方式**: GET
- **接口描述**: 查询所有老人档案
- **权限要求**: 需要登录

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 老人档案列表 |

#### 请求示例

```http
GET /api/elderly-profile/index
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "elderlyId": 1,
      "name": "张三",
      "age": 75,
      "gender": "男",
      "phone": "13800138000",
      "address": "北京市朝阳区",
      "emergencyContact": "李四",
      "emergencyPhone": "13900139000",
      "createTime": "2024-01-15T10:30:00"
    }
  ],
  "timestamp": 1628756438000
}
```

### 8.2 获取老人列表

- **接口URL**: `/api/elderly-profile/list`
- **请求方式**: GET
- **接口描述**: 获取老人列表（用于下拉选择）
- **权限要求**: 需要登录

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 老人列表 |

#### 请求示例

```http
GET /api/elderly-profile/list
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "elderlyId": 1,
      "name": "张三",
      "age": 75,
      "phone": "13800138000"
    }
  ],
  "timestamp": 1628756438000
}
```

### 8.3 根据条件查询老人档案

- **接口URL**: `/api/elderly-profile/search`
- **请求方式**: GET
- **接口描述**: 根据条件查询老人档案
- **权限要求**: 需要登录

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| name | String | 否 | 姓名 |
| age | Integer | 否 | 年龄 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 老人档案列表 |

#### 请求示例

```http
GET /api/elderly-profile/search?name=张&age=75
```

#### 响应示例

```json
[
  {
    "elderlyId": 1,
    "name": "张三",
    "age": 75,
    "gender": "男",
    "phone": "13800138000",
    "address": "北京市朝阳区"
  }
]
```

### 8.4 新增老人档案

- **接口URL**: `/api/elderly-profile/add`
- **请求方式**: POST
- **接口描述**: 新增老人档案
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| name | String | 是 | 姓名 |
| age | Integer | 是 | 年龄 |
| gender | String | 是 | 性别 |
| phone | String | 是 | 手机号 |
| address | String | 否 | 地址 |
| emergencyContact | String | 否 | 紧急联系人 |
| emergencyPhone | String | 否 | 紧急联系人电话 |
| medicalHistory | String | 否 | 病史 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 新增结果 |

#### 请求示例

```http
POST /api/elderly-profile/add
Content-Type: application/json

{
  "name": "张三",
  "age": 75,
  "gender": "男",
  "phone": "13800138000",
  "address": "北京市朝阳区",
  "emergencyContact": "李四",
  "emergencyPhone": "13900139000",
  "medicalHistory": "高血压"
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

### 8.5 更新老人档案

- **接口URL**: `/api/elderly-profile/update`
- **请求方式**: PUT
- **接口描述**: 更新老人档案
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |
| 其他参数 | - | - | 同新增老人档案 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /api/elderly-profile/update
Content-Type: application/json

{
  "elderlyId": 1,
  "name": "张三",
  "age": 76,
  "gender": "男",
  "phone": "13800138000",
  "address": "北京市朝阳区"
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

### 8.6 删除老人档案

- **接口URL**: `/api/elderly-profile/delete/{id}`
- **请求方式**: DELETE
- **接口描述**: 删除老人档案
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| id | Integer | 是 | 老人ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /api/elderly-profile/delete/1
```

#### 响应示例

```json
true
```

### 8.7 获取老人既往病史

- **接口URL**: `/api/elderly-profile/chronic-diseases/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 获取指定老人的既往病史
- **权限要求**: 需要登录

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | String | 是 | 老人ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 既往病史列表 |

#### 请求示例

```http
GET /api/elderly-profile/chronic-diseases/1
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
      "diseaseName": "高血压",
      "diagnosisDate": "2020-01-15",
      "severity": "轻度",
      "treatment": "药物治疗",
      "notes": "需要定期监测血压"
    }
  ],
  "timestamp": 1628756438000
}
```

## 9. GPS定位服务

### 9.1 接收GPS数据推送

- **接口URL**: `/api/gps/push`
- **请求方式**: POST
- **接口描述**: 接收GPS设备推送的定位数据
- **权限要求**: 无（设备接口）

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| method | String | 是 | 数据类型标识（status、alarm、pic、voice、video、PushTest） |
| serialNumber | String | 是 | 推送序号，毫秒时间戳字符串 |
| data | String | 是 | 对应的设备数据数组（JSON格式） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 处理结果（成功返回serialNumber） |

#### 请求示例

```http
POST /api/gps/push
Content-Type: application/x-www-form-urlencoded

method=status&serialNumber=1628756438000&data=[{"macid":"AA:BB:CC:DD:EE:FF","lat":39.9042,"lng":116.4074,"time":"2024-01-15 10:30:00"}]
```

#### 响应示例

```
1628756438000
```

### 9.2 设备绑定

- **接口URL**: `/api/gps/bind`
- **请求方式**: POST
- **接口描述**: 将GPS设备与老人进行绑定
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| macid | String | 是 | 设备MAC地址 |
| elderlyId | Long | 是 | 老人ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 绑定结果 |
| message | String | 结果描述 |

#### 请求示例

```http
POST /api/gps/bind
Content-Type: application/json

{
  "macid": "AA:BB:CC:DD:EE:FF",
  "elderlyId": 123
}
```

#### 响应示例

```json
{
  "success": true,
  "message": "设备绑定成功"
}
```

### 9.3 获取设备绑定列表

- **接口URL**: `/api/gps/bindings`
- **请求方式**: GET
- **接口描述**: 获取所有设备绑定关系
- **权限要求**: 管理员或医生角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 查询结果 |
| data | Array | 绑定关系列表 |

#### 请求示例

```http
GET /api/gps/bindings
```

#### 响应示例

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "macid": "AA:BB:CC:DD:EE:FF",
      "elderlyId": 123,
      "elderlyName": "张三",
      "bindTime": "2024-01-15T10:30:00",
      "status": 1
    }
  ]
}
```

### 9.4 解绑设备

- **接口URL**: `/api/gps/unbind/{macid}`
- **请求方式**: DELETE
- **接口描述**: 解绑GPS设备
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| macid | String | 是 | 设备MAC地址 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 解绑结果 |
| message | String | 结果描述 |

#### 请求示例

```http
DELETE /api/gps/unbind/AA:BB:CC:DD:EE:FF
```

#### 响应示例

```json
{
  "success": true,
  "message": "设备解绑成功"
}
```

### 9.5 GPS接口测试

- **接口URL**: `/api/gps/test`
- **请求方式**: GET
- **接口描述**: 测试GPS接口服务状态
- **权限要求**: 无

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 服务状态 |

#### 请求示例

```http
GET /api/gps/test
```

#### 响应示例

```
GPS接口服务正常运行
```

## 10. 系统配置管理

### 10.1 更新系统配置

- **接口URL**: `/api/system/config`
- **请求方式**: PUT
- **接口描述**: 更新系统配置参数
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| configParams | Object | 是 | 配置参数对象 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 更新结果 |

#### 请求示例

```http
PUT /api/system/config
Content-Type: application/json

{
  "maxFileSize": "20MB",
  "sessionTimeout": "60",
  "enableLogging": true,
  "smsProvider": "aliyun"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": "系统配置更新成功",
  "timestamp": 1628756438000
}
```

### 10.2 获取系统配置

- **接口URL**: `/api/system/config`
- **请求方式**: GET
- **接口描述**: 获取当前系统配置
- **权限要求**: 管理员角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Object | 系统配置对象 |

#### 请求示例

```http
GET /api/system/config
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "maxFileSize": "10MB",
    "sessionTimeout": "30",
    "enableLogging": true,
    "smsProvider": "aliyun",
    "gpsUpdateInterval": 60,
    "fenceCheckInterval": 30
  },
  "timestamp": 1628756438000
}
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