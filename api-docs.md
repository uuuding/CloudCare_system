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
- [8. 短信服务管理](#8-短信服务管理)
  - [8.1 发送普通短信](#81-发送普通短信)
  - [8.2 批量发送短信](#82-批量发送短信)
  - [8.3 发送模板短信](#83-发送模板短信)
  - [8.4 批量发送模板短信](#84-批量发送模板短信)
  - [8.5 查询账户余额](#85-查询账户余额)
  - [8.6 获取短信发送记录](#86-获取短信发送记录)
  - [8.7 获取短信发送统计](#87-获取短信发送统计)
  - [8.8 获取短信模板列表](#88-获取短信模板列表)
- [9. 医疗预约管理](#9-医疗预约管理)
  - [9.1 分页查询医疗预约列表](#91-分页查询医疗预约列表)
  - [9.2 根据ID查询医疗预约详情](#92-根据id查询医疗预约详情)
  - [9.3 创建医疗预约](#93-创建医疗预约)
  - [9.4 更新医疗预约](#94-更新医疗预约)
  - [9.5 删除医疗预约](#95-删除医疗预约)
  - [9.6 更新预约状态](#96-更新预约状态)
- [10. 设备管理](#10-设备管理)
    - [10.1 获取设备信息列表](#101-获取设备信息列表)
    - [10.2 根据ID获取设备信息](#102-根据id获取设备信息)
    - [10.3 创建设备信息](#103-创建设备信息)
    - [10.4 更新设备信息](#104-更新设备信息)
    - [10.5 删除设备信息](#105-删除设备信息)
  - [11. 老人观察记录管理](#11-老人观察记录管理)
    - [11.1 获取观察记录列表](#111-获取观察记录列表)
    - [11.2 根据ID获取观察记录](#112-根据id获取观察记录)
    - [11.3 创建观察记录](#113-创建观察记录)
    - [11.4 更新观察记录](#114-更新观察记录)
    - [11.5 删除观察记录](#115-删除观察记录)
- [12. 机构管理](#12-机构管理)
  - [12.1 分页查询机构信息列表](#121-分页查询机构信息列表)
  - [12.2 根据ID查询机构信息](#122-根据id查询机构信息)
  - [12.3 新增机构信息](#123-新增机构信息)
  - [12.4 修改机构信息](#124-修改机构信息)
  - [12.5 删除机构信息](#125-删除机构信息)
  - [12.6 获取机构统计信息](#126-获取机构统计信息)
- [13. 系统配置管理](#13-系统配置管理)
    - [13.1 获取系统配置](#131-获取系统配置)
    - [13.2 更新系统配置](#132-更新系统配置)

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

## 8. 医疗预约管理

### 8.1 分页查询医疗预约列表

- **接口URL**: `/api/medical/appointment/page`
- **请求方式**: GET
- **接口描述**: 分页查询医疗预约列表，支持多条件筛选
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 | 默认值 |
| ----- | --- | ------- | ---- | ----- |
| elderName | String | 否 | 老人姓名（模糊查询） | - |
| doctorName | String | 否 | 医生姓名（模糊查询） | - |
| appointmentType | Integer | 否 | 预约类型（1-体检 2-门诊 3-专科 4-急诊） | - |
| status | Integer | 否 | 预约状态（1-待确认 2-已确认 3-已完成 4-已取消） | - |
| startTime | String | 否 | 预约开始时间（格式：yyyy-MM-dd HH:mm:ss） | - |
| endTime | String | 否 | 预约结束时间（格式：yyyy-MM-dd HH:mm:ss） | - |
| current | Integer | 否 | 当前页码 | 1 |
| size | Integer | 否 | 每页大小 | 10 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| total | Long | 总记录数 |
| pages | Long | 总页数 |
| current | Long | 当前页码 |
| size | Long | 每页大小 |
| records | Array | 预约列表 |

#### 预约数据结构

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| appointmentId | Long | 预约ID |
| elderId | Long | 老人ID |
| elderName | String | 老人姓名 |
| doctorId | Long | 医生ID |
| doctorName | String | 医生姓名 |
| appointmentTime | String | 预约时间 |
| appointmentType | Integer | 预约类型 |
| appointmentTypeText | String | 预约类型文本 |
| status | Integer | 预约状态 |
| statusText | String | 预约状态文本 |
| remark | String | 备注 |
| createTime | String | 创建时间 |
| updateTime | String | 更新时间 |

#### 请求示例

```http
GET /api/medical/appointment/page?elderName=张&appointmentType=2&status=1&current=1&size=10
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
        "appointmentId": 1,
        "elderId": 3,
        "elderName": "张老先生",
        "doctorId": 1,
        "doctorName": "李医生",
        "appointmentTime": "2024-01-20T09:00:00+08:00",
        "appointmentType": 2,
        "appointmentTypeText": "门诊",
        "status": 1,
        "statusText": "待确认",
        "remark": "定期复查",
        "createTime": "2024-01-15 10:30:00",
        "updateTime": "2024-01-15 10:30:00"
      }
    ]
  },
  "timestamp": 1628756438000
}
```

## 8. 短信服务管理

### 8.1 发送普通短信

- **接口URL**: `/sms/send`
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
| - | Boolean | 发送结果 |

#### 请求示例

```http
POST /sms/send
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
  "message": "短信发送成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 8.2 批量发送短信

- **接口URL**: `/sms/send/batch`
- **请求方式**: POST
- **接口描述**: 批量发送短信
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| phones | String | 是 | 手机号列表（逗号分隔） |
| content | String | 是 | 短信内容 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 发送结果 |

#### 请求示例

```http
POST /sms/send/batch
Content-Type: application/json

{
  "phones": "13800138000,13900139000,13700137000",
  "content": "系统维护通知：系统将于今晚22:00-24:00进行维护，期间可能影响服务使用。"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "批量短信发送成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 8.3 发送模板短信

- **接口URL**: `/sms/send/template`
- **请求方式**: POST
- **接口描述**: 根据模板发送短信
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| phone | String | 是 | 手机号 |
| templateCode | String | 是 | 模板代码 |
| params | Object | 否 | 模板参数 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 发送结果 |

#### 请求示例

```http
POST /sms/send/template
Content-Type: application/json

{
  "phone": "13800138000",
  "templateCode": "LOGIN_SECURITY",
  "params": {
    "username": "张三",
    "loginTime": "2024-01-15 10:30:00",
    "location": "北京市朝阳区"
  }
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "模板短信发送成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 8.4 批量发送模板短信

- **接口URL**: `/sms/send/template/batch`
- **请求方式**: POST
- **接口描述**: 批量发送模板短信
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| phones | String | 是 | 手机号列表（逗号分隔） |
| templateCode | String | 是 | 模板代码 |
| params | Object | 否 | 模板参数 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 发送结果 |

#### 请求示例

```http
POST /sms/send/template/batch
Content-Type: application/json

{
  "phones": "13800138000,13900139000",
  "templateCode": "APPOINTMENT_REMINDER",
  "params": {
    "elderName": "张老先生",
    "appointmentTime": "2024-01-20 09:00",
    "hospitalName": "北京协和医院",
    "department": "心内科"
  }
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "批量模板短信发送成功",
  "data": true,
  "timestamp": 1628756438000
}
```

### 8.5 查询账户余额

- **接口URL**: `/sms/balance`
- **请求方式**: GET
- **接口描述**: 查询短信账户余额
- **权限要求**: 管理员角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 账户余额 |

#### 请求示例

```http
GET /sms/balance
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "查询成功",
  "data": "1000",
  "timestamp": 1628756438000
}
```

### 8.6 获取短信发送记录

- **接口URL**: `/sms/records`
- **请求方式**: GET
- **接口描述**: 分页获取短信发送记录
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| phone | String | 否 | 手机号 |
| status | String | 否 | 发送状态 |
| type | String | 否 | 短信类型 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页大小 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Object | 分页数据 |

#### 请求示例

```http
GET /sms/records?page=1&size=10&status=success
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
        "recordId": 1,
        "phone": "13800138000",
        "content": "测试短信内容",
        "type": "normal",
        "status": "success",
        "sendTime": "2024-01-15 10:30:00"
      }
    ]
  },
  "timestamp": 1628756438000
}
```

### 8.7 获取短信发送统计

- **接口URL**: `/sms/stats`
- **请求方式**: GET
- **接口描述**: 获取今日短信发送统计信息
- **权限要求**: 管理员角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Object | 统计信息 |

#### 请求示例

```http
GET /sms/stats
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "todayTotal": 50,
    "todaySuccess": 48,
    "todayFailed": 2,
    "successRate": "96%"
  },
  "timestamp": 1628756438000
}
```

### 8.8 获取短信模板列表

- **接口URL**: `/sms/templates`
- **请求方式**: GET
- **接口描述**: 获取所有可用的短信模板
- **权限要求**: 管理员或医生角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 模板列表 |

#### 请求示例

```http
GET /sms/templates
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "code": "REGISTER_WELCOME",
      "name": "注册欢迎",
      "content": "【云护CloudCare平台】感谢您的注册，欢迎来到云护CloudCare智慧系统。",
      "params": []
    },
    {
      "code": "LOGIN_SECURITY",
      "name": "登录安全提醒",
      "content": "【云护CloudCare】用户{username}于{loginTime}在{location}登录系统，如非本人操作请及时联系管理员。",
      "params": ["username", "loginTime", "location"]
    }
  ],VITE_APP_BASE_API=''
  "timestamp": 1628756438000
}
```

### 8.2 根据ID查询医疗预约详情

- **接口URL**: `/api/medical/appointment/{appointmentId}`
- **请求方式**: GET
- **接口描述**: 根据预约ID查询医疗预约详情
- **权限要求**: 管理员、医生、护士或相关老人

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| appointmentId | Long | 是 | 预约ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Object | 预约详情（结构同分页查询） |

#### 请求示例

```http
GET /api/medical/appointment/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "appointmentId": 1,
    "elderId": 3,
    "elderName": "张老先生",
    "doctorId": 1,
    "doctorName": "李医生",
    "appointmentTime": "2024-01-20T09:00:00+08:00",
    "appointmentType": 2,
    "appointmentTypeText": "门诊",
    "status": 1,
    "statusText": "待确认",
    "remark": "定期复查",
    "createTime": "2024-01-15 10:30:00",
    "updateTime": "2024-01-15 10:30:00"
  },
  "timestamp": 1628756438000
}
```

### 8.3 创建医疗预约

- **接口URL**: `/api/medical/appointment`
- **请求方式**: POST
- **接口描述**: 创建新的医疗预约
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderId | Long | 是 | 老人ID |
| doctorId | Long | 是 | 医生ID |
| appointmentTime | String | 是 | 预约时间（格式：yyyy-MM-ddTHH:mm:ss+08:00） |
| appointmentType | Integer | 是 | 预约类型（1-体检 2-门诊 3-专科 4-急诊） |
| status | Integer | 否 | 预约状态（默认为1-待确认） |
| remark | String | 否 | 备注 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 操作结果消息 |

#### 请求示例

```http
POST /api/medical/appointment
Content-Type: application/json

{
  "elderId": 3,
  "doctorId": 1,
  "appointmentTime": "2024-01-25T14:00:00+08:00",
  "appointmentType": 2,
  "remark": "定期复查血压"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": "创建预约成功",
  "timestamp": 1628756438000
}
```

### 8.4 更新医疗预约

- **接口URL**: `/api/medical/appointment/{appointmentId}`
- **请求方式**: PUT
- **接口描述**: 更新指定的医疗预约信息
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| appointmentId | Long | 是 | 预约ID（路径参数） |
| elderId | Long | 是 | 老人ID |
| doctorId | Long | 是 | 医生ID |
| appointmentTime | String | 是 | 预约时间（格式：yyyy-MM-ddTHH:mm:ss+08:00） |
| appointmentType | Integer | 是 | 预约类型（1-体检 2-门诊 3-专科 4-急诊） |
| status | Integer | 否 | 预约状态 |
| remark | String | 否 | 备注 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 操作结果消息 |

#### 请求示例

```http
PUT /api/medical/appointment/1
Content-Type: application/json

{
  "elderId": 3,
  "doctorId": 1,
  "appointmentTime": "2024-01-25T15:00:00+08:00",
  "appointmentType": 2,
  "status": 2,
  "remark": "已确认预约时间"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": "更新预约成功",
  "timestamp": 1628756438000
}
```

### 8.5 删除医疗预约

- **接口URL**: `/api/medical/appointment/{appointmentId}`
- **请求方式**: DELETE
- **接口描述**: 删除指定的医疗预约
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| appointmentId | Long | 是 | 预约ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 操作结果消息 |

#### 请求示例

```http
DELETE /api/medical/appointment/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": "删除预约成功",
  "timestamp": 1628756438000
}
```

### 8.6 更新预约状态

- **接口URL**: `/api/medical/appointment/{appointmentId}/status`
- **请求方式**: PATCH
- **接口描述**: 更新指定预约的状态
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| appointmentId | Long | 是 | 预约ID（路径参数） |
| status | Integer | 是 | 预约状态（1-待确认 2-已确认 3-已完成 4-已取消） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 操作结果消息 |

#### 请求示例

```http
PATCH /api/medical/appointment/1/status?status=2
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": "更新预约状态成功",
  "timestamp": 1628756438000
}
```

---

**医疗预约管理模块说明**：

1. **预约类型说明**：
   - 1：体检 - 定期健康体检
   - 2：门诊 - 普通门诊就诊
   - 3：专科 - 专科医生诊疗
   - 4：急诊 - 紧急医疗服务

2. **预约状态说明**：
   - 1：待确认 - 预约已提交，等待确认
   - 2：已确认 - 预约已确认，等待就诊
   - 3：已完成 - 预约已完成就诊
   - 4：已取消 - 预约已取消

3. **时间格式说明**：
   - 预约时间使用ISO 8601格式：`yyyy-MM-ddTHH:mm:ss+08:00`
   - 查询时间参数使用简化格式：`yyyy-MM-dd HH:mm:ss`

4. **权限控制**：
   - 管理员：拥有所有操作权限
   - 医生：可以查看、创建、更新和删除预约
   - 护士：可以查看、创建和更新预约状态
   - 老人：只能查看自己的预约信息

5. **业务规则**：
   - 同一老人在同一时间段只能有一个预约
   - 同一医生在同一时间段只能接受一个预约
   - 预约时间不能早于当前时间
   - 已完成或已取消的预约不能再次修改

**更新日志**：

- 2024-01-15：新增医疗预约管理模块
- 2024-01-15：完善预约状态管理和权限控制
- 2024-01-15：优化时间格式处理和业务规则验证

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

## 6. GPS定位管理

### 6.1 接收GPS数据推送

- **接口URL**: `/gps/push`
- **请求方式**: POST
- **接口描述**: 接收GPS设备推送的定位数据
- **权限要求**: 无（设备接口）

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| method | String | 是 | 数据类型标识（固定值：status） |
| serialNumber | String | 是 | 推送序号（毫秒时间戳） |
| data | String | 是 | GPS设备数据数组（JSON格式） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | String | 返回推送序号 |

#### 请求示例

```http
POST /api/gps/push
Content-Type: application/x-www-form-urlencoded

method=status&serialNumber=1628756438000&data=[{"macid":"TEST123456789","gpsTime":1628756438000,"lat":39.9042,"lon":116.4074,"speed":0.0,"dir":0.0}]
```

#### 响应示例

```
1628756438000
```

### 6.2 获取老人轨迹数据

- **接口URL**: `/gps/track/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 获取指定老人在指定时间范围内的GPS轨迹数据
- **权限要求**: 管理员、医生或相关老人

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Integer | 是 | 老人ID（路径参数） |
| startTime | String | 是 | 开始时间（格式：yyyy-MM-dd HH:mm:ss） |
| endTime | String | 是 | 结束时间（格式：yyyy-MM-dd HH:mm:ss） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| code | Integer | 状态码 |
| data | Array | GPS轨迹数据列表 |
| message | String | 响应消息 |

#### GPS轨迹数据结构

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| id | Long | 记录ID |
| macid | String | 设备编号 |
| elderlyId | Integer | 老人ID |
| gpsTime | Long | GPS时间戳（毫秒） |
| lat | Double | GPS纬度 |
| lon | Double | GPS经度 |
| mapLat | Double | 地图纬度 |
| mapLon | Double | 地图经度 |
| speed | Double | 速度（km/h） |
| dir | Double | 方向角度 |
| createTime | String | 创建时间 |

#### 请求示例

```http
GET /api/gps/track/1?startTime=2023-08-08 00:00:00&endTime=2023-08-08 23:59:59
```

#### 响应示例

```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "macid": "TEST123456789",
      "elderlyId": 1,
      "gpsTime": 1628756438000,
      "lat": 39.9042,
      "lon": 116.4074,
      "mapLat": 39.9042,
      "mapLon": 116.4074,
      "speed": 5.2,
      "dir": 90.0,
      "createTime": "2023-08-08 10:30:00"
    }
  ],
  "message": "查询成功"
}
```

### 6.3 获取老人最新位置

- **接口URL**: `/gps/latest/{elderlyId}`
- **请求方式**: GET
- **接口描述**: 获取指定老人的最新GPS位置信息
- **权限要求**: 管理员、医生或相关老人

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Integer | 是 | 老人ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| code | Integer | 状态码 |
| data | Object | 最新位置数据（结构同轨迹数据） |
| message | String | 响应消息 |

#### 请求示例

```http
GET /api/gps/latest/1
```

#### 响应示例

```json
{
  "code": 200,
  "data": {
    "id": 100,
    "macid": "TEST123456789",
    "elderlyId": 1,
    "gpsTime": 1628756438000,
    "lat": 39.9042,
    "lon": 116.4074,
    "mapLat": 39.9042,
    "mapLon": 116.4074,
    "speed": 0.0,
    "dir": 90.0,
    "createTime": "2023-08-08 14:30:00"
  },
  "message": "查询成功"
}
```

### 6.4 绑定GPS设备

- **接口URL**: `/gps/bind`
- **请求方式**: POST
- **接口描述**: 将GPS设备与老人建立绑定关系
- **权限要求**: 管理员或医生

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| macid | String | 是 | 设备MAC地址/IMEI |
| elderlyId | Integer | 是 | 老人ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 绑定结果 |
| message | String | 响应消息 |

#### 请求示例

```http
POST /api/gps/bind
Content-Type: application/json

{
  "macid": "TEST123456789",
  "elderlyId": 1
}
```

#### 响应示例

```json
{
  "success": true,
  "message": "设备绑定成功"
}
```

### 6.5 获取设备绑定列表

- **接口URL**: `/gps/bindings`
- **请求方式**: GET
- **接口描述**: 获取所有GPS设备与老人的绑定关系列表
- **权限要求**: 管理员或医生

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 查询结果 |
| data | Array | 绑定关系列表 |
| message | String | 响应消息 |

#### 绑定关系数据结构

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| macid | String | 设备编号 |
| elderlyId | Integer | 老人ID |
| elderlyName | String | 老人姓名 |
| createTime | String | 绑定时间 |
| updateTime | String | 最后更新时间 |

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
      "macid": "TEST123456789",
      "elderlyId": 1,
      "elderlyName": "张老先生",
      "createTime": "2023-08-01 10:00:00",
      "updateTime": "2023-08-08 14:30:00"
    }
  ],
  "message": "查询成功"
}
```

### 6.6 解绑GPS设备

- **接口URL**: `/gps/unbind/{macid}`
- **请求方式**: DELETE
- **接口描述**: 解除GPS设备与老人的绑定关系
- **权限要求**: 管理员或医生

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| macid | String | 是 | 设备MAC地址/IMEI（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 解绑结果 |
| message | String | 响应消息 |

#### 请求示例

```http
DELETE /api/gps/unbind/TEST123456789
```

#### 响应示例

```json
{
  "success": true,
  "message": "设备解绑成功"
}
```

## 7. 电子围栏管理

### 7.1 创建围栏

- **接口URL**: `/geo-fence/create`
- **请求方式**: POST
- **接口描述**: 创建新的电子围栏
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |
| fenceName | String | 是 | 围栏名称 |
| fenceType | String | 是 | 围栏类型（circle：圆形，polygon：多边形） |
| centerLat | Double | 是 | 中心点纬度 |
| centerLon | Double | 是 | 中心点经度 |
| radius | Double | 否 | 半径（米，圆形围栏必填） |
| coordinates | String | 否 | 坐标点（JSON，多边形围栏必填） |
| enterAlert | Boolean | 是 | 进入提醒 |
| exitAlert | Boolean | 是 | 离开提醒 |
| alertType | Integer | 是 | 提醒方式（1：短信，2：应用推送，3：两者都有） |
| emergencyContacts | String | 否 | 紧急联系人手机号（多个用逗号分隔） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| success | Boolean | 创建结果 |
| message | String | 响应消息 |

#### 请求示例

```http
POST /api/geo-fence/create
Content-Type: application/json

{
  "elderlyId": 1,
  "fenceName": "家庭围栏",
  "fenceType": "circle",
  "centerLat": 39.9042,
  "centerLon": 116.4074,
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
  "message": "围栏创建成功"
}
```

---

**注意事项**：

1. **时间格式**：所有时间参数统一使用 `yyyy-MM-dd HH:mm:ss` 格式（24小时制）
2. **GPS时间戳**：GPS数据中的 `gpsTime` 字段为毫秒级Unix时间戳
3. **坐标系统**：支持GPS原始坐标和地图坐标，系统会自动处理坐标转换
4. **权限控制**：不同角色用户只能访问相应权限范围内的数据
5. **数据分页**：列表查询接口支持分页参数，建议合理设置页面大小
6. **错误处理**：所有接口都会返回统一的错误格式，便于前端处理

**更新日志**：

- 2024-01-15：新增GPS轨迹查询接口
- 2024-01-15：修复时间显示格式，统一使用24小时制
- 2024-01-15：优化GPS时间戳处理，解决时区转换问题
- 2024-01-15：完善电子围栏管理接口文档

### 7.2 更新围栏

- **接口URL**: `/geo-fence/update/{id}`
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

### 7.3 删除围栏

- **接口URL**: `/geo-fence/delete/{fenceId}`
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

### 7.4 分页查询所有围栏

- **接口URL**: `/geo-fence/list`
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

### 7.5 根据老人ID查询围栏

- **接口URL**: `/geo-fence/list/{elderlyId}`
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

## 9. 设备管理

### 9.1 获取设备信息列表

- **接口URL**: `/device/list`
- **请求方式**: GET
- **接口描述**: 获取设备信息列表
- **权限要求**: 管理员或医生角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 设备信息列表 |

#### 请求示例

```http
GET /device-info/list
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "deviceId": 1,
      "deviceName": "GPS定位器",
      "deviceType": "GPS",
      "deviceModel": "GT06N",
      "status": 1,
      "createTime": "2024-01-15 10:30:00"
    }
  ],
  "timestamp": 1628756438000
}
```

### 9.2 根据ID获取设备信息

- **接口URL**: `/device/{deviceId}`
- **请求方式**: GET
- **接口描述**: 根据设备ID获取设备详细信息
- **权限要求**: 管理员或医生角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| deviceId | Long | 是 | 设备ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Object | 设备详细信息 |

#### 请求示例

```http
GET /device-info/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "deviceId": 1,
    "deviceName": "GPS定位器",
    "deviceType": "GPS",
    "deviceModel": "GT06N",
    "status": 1,
    "createTime": "2024-01-15 10:30:00",
    "updateTime": "2024-01-15 10:30:00"
  },
  "timestamp": 1628756438000
}
```

### 9.3 创建设备信息

- **接口URL**: `/device`
- **请求方式**: POST
- **接口描述**: 创建新的设备信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| deviceName | String | 是 | 设备名称 |
| deviceType | String | 是 | 设备类型 |
| deviceModel | String | 是 | 设备型号 |
| status | Integer | 否 | 设备状态 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 创建结果 |

#### 请求示例

```http
POST /device-info
Content-Type: application/json

{
  "deviceName": "新GPS定位器",
  "deviceType": "GPS",
  "deviceModel": "GT06N",
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

### 9.4 更新设备信息

- **接口URL**: `/device`
- **请求方式**: PUT
- **接口描述**: 更新设备信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| deviceId | Long | 是 | 设备ID（路径参数） |
| deviceName | String | 否 | 设备名称 |
| deviceType | String | 否 | 设备类型 |
| deviceModel | String | 否 | 设备型号 |
| status | Integer | 否 | 设备状态 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /device-info/1
Content-Type: application/json

{
  "deviceName": "更新后的GPS定位器",
  "status": 0
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

### 9.5 删除设备信息

- **接口URL**: `/device/{deviceIds}`
- **请求方式**: DELETE
- **接口描述**: 删除设备信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| deviceId | Long | 是 | 设备ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /device-info/1
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

## 10. 老人观察记录管理

### 10.1 获取观察记录列表

- **接口URL**: `/elderly-observations/list`
- **请求方式**: GET
- **接口描述**: 获取老人观察记录列表
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 否 | 老人ID |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页大小 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Array | 观察记录列表 |

#### 请求示例

```http
GET /elderly-observations/list?elderlyId=1&page=1&size=10
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "observationId": 1,
      "elderlyId": 1,
      "elderlyName": "张老先生",
      "observationType": "日常观察",
      "observationContent": "精神状态良好，食欲正常",
      "observationTime": "2024-01-15 10:30:00",
      "observerId": 2,
      "observerName": "李护士",
      "createTime": "2024-01-15 10:30:00"
    }
  ],
  "timestamp": 1628756438000
}
```

### 10.2 根据ID获取观察记录

- **接口URL**: `/elderly-observations/{observationId}`
- **请求方式**: GET
- **接口描述**: 根据观察记录ID获取详细信息
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| observationId | Long | 是 | 观察记录ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Object | 观察记录详细信息 |

#### 请求示例

```http
GET /elderly-observations/1
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "observationId": 1,
    "elderlyId": 1,
    "elderlyName": "张老先生",
    "observationType": "日常观察",
    "observationContent": "精神状态良好，食欲正常",
    "observationTime": "2024-01-15 10:30:00",
    "observerId": 2,
    "observerName": "李护士",
    "createTime": "2024-01-15 10:30:00",
    "updateTime": "2024-01-15 10:30:00"
  },
  "timestamp": 1628756438000
}
```

### 10.3 创建观察记录

- **接口URL**: `/elderly-observations`
- **请求方式**: POST
- **接口描述**: 创建新的观察记录
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| elderlyId | Long | 是 | 老人ID |
| observationType | String | 是 | 观察类型 |
| observationContent | String | 是 | 观察内容 |
| observationTime | String | 是 | 观察时间 |
| observerId | Long | 是 | 观察者ID |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 创建结果 |

#### 请求示例

```http
POST /elderly-observations
Content-Type: application/json

{
  "elderlyId": 1,
  "observationType": "日常观察",
  "observationContent": "精神状态良好，食欲正常",
  "observationTime": "2024-01-15 10:30:00",
  "observerId": 2
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

### 10.4 更新观察记录

- **接口URL**: `/elderly-observations/{observationId}`
- **请求方式**: PUT
- **接口描述**: 更新观察记录
- **权限要求**: 管理员、医生或护士角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| observationId | Long | 是 | 观察记录ID（路径参数） |
| observationType | String | 否 | 观察类型 |
| observationContent | String | 否 | 观察内容 |
| observationTime | String | 否 | 观察时间 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /elderly-observations/1
Content-Type: application/json

{
  "observationContent": "精神状态良好，食欲正常，血压稳定"
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

### 10.5 删除观察记录

- **接口URL**: `/elderly-observations/{observationId}`
- **请求方式**: DELETE
- **接口描述**: 删除观察记录
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| observationId | Long | 是 | 观察记录ID（路径参数） |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 删除结果 |

#### 请求示例

```http
DELETE /elderly-observations/1
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

## 11. 系统配置管理

### 11.1 获取系统配置

- **接口URL**: `/system/config`
- **请求方式**: GET
- **接口描述**: 获取系统配置信息
- **权限要求**: 管理员角色

#### 请求参数

无

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Object | 系统配置信息 |

#### 请求示例

```http
GET /system-config
```

#### 响应示例

```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "systemName": "云护CloudCare智慧医养系统",
    "systemVersion": "v1.0.0",
    "maxUserCount": 1000,
    "dataRetentionDays": 365,
    "enableSmsAlert": true,
    "enableEmailAlert": false,
    "updateTime": "2024-01-15 10:30:00"
  },
  "timestamp": 1628756438000
}
```

### 11.2 更新系统配置

- **接口URL**: `/system/config`
- **请求方式**: PUT
- **接口描述**: 更新系统配置信息
- **权限要求**: 管理员角色

#### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| ----- | --- | ------- | ---- |
| systemName | String | 否 | 系统名称 |
| maxUserCount | Integer | 否 | 最大用户数 |
| dataRetentionDays | Integer | 否 | 数据保留天数 |
| enableSmsAlert | Boolean | 否 | 启用短信告警 |
| enableEmailAlert | Boolean | 否 | 启用邮件告警 |

#### 响应参数

| 参数名 | 类型 | 描述 |
| ----- | --- | ---- |
| - | Boolean | 更新结果 |

#### 请求示例

```http
PUT /system-config
Content-Type: application/json

{
  "maxUserCount": 2000,
  "dataRetentionDays": 730,
  "enableEmailAlert": true
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

---

**注意事项**：

1. **接口路径统一**：所有接口路径已统一修正，去除了不必要的 `/api` 前缀
2. **响应格式统一**：所有接口都使用 `Result<T>` 格式返回数据
3. **权限控制**：不同角色用户只能访问相应权限范围内的数据
4. **时间格式**：统一使用 `yyyy-MM-dd HH:mm:ss` 格式
5. **错误处理**：所有接口都会返回统一的错误格式
6. **数据验证**：所有输入参数都会进行有效性验证

**更新日志**：

- 2024-01-20：修正所有接口路径，确保与后端实现一致
- 2024-01-20：补充设备管理、老人观察记录管理和系统配置管理模块
- 2024-01-20：统一响应格式为 `Result<T>`
- 2024-01-20：完善健康预警管理接口，修正为实际存在的接口

### 7.6 查询围栏详情

- **接口URL**: `/geo-fence/detail/{fenceId}`
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

### 7.7 查询围栏事件记录

- **接口URL**: `/geo-fence/events/{elderlyId}`
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

### 7.8 获取老人当前位置

- **接口URL**: `/geo-fence/location/{elderlyId}`
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

- **接口URL**: `/auth/login`
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

- **接口URL**: `/auth/register`
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

- **接口URL**: `/auth/logout`
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

- **接口URL**: `/user/info`
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

- **接口URL**: `/user/info`
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

- **接口URL**: `/user/password`
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

- **接口URL**: `/user/avatar`
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

- **接口URL**: `/user/page`
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

- **接口URL**: `/user/{userId}`
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

- **接口URL**: `/user`
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

- **接口URL**: `/user`
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

- **接口URL**: `/user/{userId}`
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

- **接口URL**: `/user/reset-password/{userId}`
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

- **接口URL**: `/user/status/{userId}/{status}`
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

- **接口URL**: `/dashboard/statistics`
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

- **接口URL**: `/dashboard/warning/trend`
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

- **接口URL**: `/dashboard/record/distribution`
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

- **接口URL**: `/dashboard/announcements`
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

- **接口URL**: `/dashboard/weather`
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

### 5.1 获取所有健康预警记录

- **接口URL**: `/health-alert/all`
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

### 5.2 根据老人ID获取预警记录

- **接口URL**: `/health-alert/elderly/{elderlyId}`
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

### 5.3 根据预警状态获取预警记录

- **接口URL**: `/health-alert/status/{status}`
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

### 5.4 根据预警级别获取预警记录

- **接口URL**: `/health-alert/level/{alertLevel}`
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

### 5.5 根据时间范围获取预警记录

- **接口URL**: `/health-alert/time-range`
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

### 5.6 处理预警（标记为已解决）

- **接口URL**: `/health-alert/resolve/{alertId}`
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

### 5.7 获取活跃预警数量

- **接口URL**: `/health-alert/active-count`
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

### 5.8 获取预警统计信息

- **接口URL**: `/health-alert/statistics`
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

### 5.9 根据预警类型获取预警记录

- **接口URL**: `/health-alert/type/{alertType}`
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