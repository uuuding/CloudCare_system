# CloudCare云护智慧医养大数据公共服务平台

## 项目介绍

CloudCare云护智慧医养大数据公共服务平台是一个面向医养结合领域的综合管理系统，旨在通过大数据技术提升老年人健康管理和医疗服务质量。

## 系统功能

### 核心功能模块
- **用户管理**：支持管理员、医生、老人三种角色的用户管理
- **健康档案**：老人基本信息、健康状况记录管理
- **健康预警**：智能健康监测与预警系统
- **电子围栏**：GPS定位监控、围栏区域设置、进出告警系统
- **设备管理**：GPS设备绑定、位置追踪、历史轨迹查询
- **短信通知**：围栏告警、健康预警等多场景短信提醒
- **数据统计**：健康数据、位置数据可视化分析
- **系统管理**：用户权限、系统配置等管理功能

### 技术特色
- **响应式设计**：适配PC端和移动端
- **实时监控**：GPS实时定位、围栏事件实时通知
- **智能告警**：多种告警方式（短信、应用推送）
- **数据可视化**：ECharts图表展示、地图位置展示
- **权限控制**：基于角色的访问控制
- **安全防护**：JWT认证、密码加密
- **定时任务**：数据清理、告警处理等自动化任务

## 技术栈

### 后端
- SpringBoot 2.7.x
- MyBatis-Plus 3.5.x
- MySQL 8.0
- Redis
- Spring Security

### 前端
- Vue 3
- Element Plus
- Axios
- Vue Router
- Pinia

## 项目结构

```
CloudCare_system/
├── cloudcare-backend/          # 后端服务
│   ├── src/main/java/
│   │   └── com/cloudcare/
│   │       ├── controller/     # 控制器层
│   │       │   ├── GeoFenceController.java    # 围栏管理接口
│   │       │   ├── GpsController.java         # GPS数据接收接口
│   │       │   └── ...
│   │       ├── service/        # 服务层
│   │       │   ├── GeoFenceService.java       # 围栏服务
│   │       │   ├── GpsLocationService.java    # GPS定位服务
│   │       │   ├── GeoFenceEventService.java  # 围栏事件服务
│   │       │   ├── SmsService.java            # 短信服务
│   │       │   └── ...
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       │   ├── GeoFence.java              # 围栏实体
│   │       │   ├── GpsLocation.java           # GPS定位实体
│   │       │   ├── GeoFenceEvent.java         # 围栏事件实体
│   │       │   └── ...
│   │       ├── dto/            # 数据传输对象
│   │       ├── config/         # 配置类
│   │       ├── task/           # 定时任务
│   │       ├── utils/          # 工具类
│   │       └── CloudCareApplication.java # 启动类
│   └── src/main/resources/     # 配置文件
│       ├── mapper/             # MyBatis XML映射文件
│       ├── application.yml     # 主配置文件
│       ├── application-dev.yml # 开发环境配置
│       └── application-gps.yml # GPS系统配置
├── cloudcare-frontend/         # 前端应用
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   │   └── elderly-service/
│   │   │       └── geo-fence/  # 围栏管理页面
│   │   ├── components/         # 公共组件
│   │   │   └── FenceEventNotification.vue  # 围栏事件通知组件
│   │   ├── api/                # API接口
│   │   │   ├── geoFence.js     # 围栏相关API
│   │   │   └── ...
│   │   ├── router/             # 路由配置
│   │   ├── store/              # 状态管理
│   │   ├── assets/             # 资源文件
│   │   ├── App.vue             # 根组件
│   │   └── main.js             # 入口文件
│   ├── public/                 # 静态资源
│   ├── .env.development        # 开发环境变量
│   ├── .env.production         # 生产环境变量
│   ├── package.json            # NPM配置
│   └── vite.config.js          # Vite配置
├── GPS_FENCE_SYSTEM_README.md  # GPS围栏系统详细文档
├── SMS_MODULE_DOCUMENTATION.md # 短信模块文档
├── geo_fence_system.sql        # 数据库表结构
├── gps_test.html              # GPS系统测试页面
└── README.md                   # 项目说明
```

## 快速开始

### 后端启动

1. 确保已安装JDK 11+和Maven
2. 创建MySQL数据库，名称为`cloudcare`
3. 修改`application-dev.yml`中的数据库连接信息
4. 在`cloudcare-backend`目录下执行：

```bash
mvn spring-boot:run
```

### 前端启动

1. 确保已安装Node.js 16+
2. 在`cloudcare-frontend`目录下执行：

```bash
npm install
npm run dev
```

## 默认账号

- 管理员：admin / admin123
- 医生：doctor / doctor123
- 老人：elder / elder123

## 数据库设计

### 主要数据表

#### 用户管理相关
1. **用户表 (users)**
   - 用户基本信息、角色权限

2. **老人档案表 (elderly_profile)**
   - 老人基本信息、健康状况

#### 健康管理相关
3. **健康预警表 (health_alert)**
   - 健康预警记录、处理状态

#### GPS围栏系统相关
4. **GPS定位数据表 (gps_location)**
   - GPS设备定位数据、时间戳、设备信息
   - 支持原始GPS坐标和地图坐标

5. **电子围栏配置表 (geo_fence)**
   - 围栏名称、类型（圆形/多边形）
   - 围栏坐标、半径、告警方式
   - 关联老人信息、状态管理

6. **围栏事件记录表 (geo_fence_event)**
   - 进入/离开事件记录
   - 事件时间、位置坐标、告警状态
   - 读取状态管理

7. **设备绑定表 (device_binding)**
   - GPS设备与老人的绑定关系
   - 设备MAC地址、绑定时间

#### 通知系统相关
8. **短信记录表 (sms_record)**
   - 短信发送记录、模板管理
   - 发送状态、失败重试机制

#### 系统管理相关
9. **系统配置表 (system_config)**
   - 系统参数配置

详细的数据库设计文档请参考项目文档。

## API 接口文档

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户退出
- `POST /api/auth/register` - 用户注册

### 用户管理
- `GET /api/users` - 获取用户列表
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

### 健康预警
- `GET /api/health-alerts` - 获取预警列表
- `POST /api/health-alerts` - 创建预警
- `PUT /api/health-alerts/{id}` - 更新预警
- `DELETE /api/health-alerts/{id}` - 删除预警

### GPS围栏系统
#### GPS数据接收
- `POST /api/gps/push` - 接收GPS设备推送数据
- `POST /api/gps/bind` - 绑定GPS设备与老人
- `GET /api/gps/bindings` - 获取设备绑定列表
- `DELETE /api/gps/unbind/{macid}` - 解绑GPS设备

#### 围栏管理
- `GET /api/geo-fence/list` - 获取围栏列表（分页）
- `POST /api/geo-fence/create` - 创建电子围栏
- `PUT /api/geo-fence/update/{id}` - 更新围栏信息
- `DELETE /api/geo-fence/delete/{id}` - 删除围栏
- `GET /api/geo-fence/{id}` - 获取围栏详情
- `GET /api/geo-fence/elderly/{elderlyId}` - 获取老人的围栏列表

#### 围栏事件
- `GET /api/geo-fence/events/all` - 获取所有围栏事件（支持筛选）
- `GET /api/geo-fence/events/{elderlyId}` - 获取老人的围栏事件
- `PUT /api/geo-fence/events/{eventId}/read` - 标记事件为已读
- `GET /api/geo-fence/events/recent` - 获取最近的围栏事件

#### 位置查询
- `GET /api/geo-fence/location/{elderlyId}` - 获取老人最新位置
- `GET /api/geo-fence/stats` - 获取围栏统计信息

更多接口详情请参考：
- [完整API文档](./api-docs.md)
- [GPS围栏系统文档](./GPS_FENCE_SYSTEM_README.md)
- [短信模块文档](./SMS_MODULE_DOCUMENTATION.md)

## 快速启动

### 后端服务

```bash
# 进入后端目录
cd cloudcare-backend

# 编译打包
mvn clean package

# 运行服务
mvn spring-boot:run
```

### 前端应用

```bash
# 进入前端目录
cd cloudcare-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### GPS围栏系统测试

1. 确保后端服务已启动
2. 打开 `gps_test.html` 文件进行GPS数据推送测试
3. 在前端应用中访问电子围栏管理页面

## 系统截图

### 电子围栏管理

![电子围栏管理](./screenshots/geo-fence.png)

### 围栏事件记录

![围栏事件记录](./screenshots/fence-events.png)

### 设备绑定管理

![设备绑定管理](./screenshots/device-binding.png)

## 系统特色

### 🎯 实时监控
- GPS设备实时位置追踪
- WebSocket实时事件推送
- 围栏状态实时更新

### 🚨 智能告警
- 多种告警方式（短信、系统通知）
- 可配置的告警规则
- 事件状态管理（已读/未读）

### 📱 设备管理
- GPS设备绑定/解绑
- 设备状态监控
- 批量设备管理

### 🗺️ 地图集成
- 高德地图API集成
- 圆形和多边形围栏支持
- 可视化围栏编辑
- **地图选点功能**：支持在地图上直观选择围栏中心位置
- **地点搜索**：支持地点名称搜索快速定位
- **多种坐标输入方式**：地图选点、GPS定位、手动输入

### 📊 数据统计
- 围栏事件统计
- 设备活跃度分析
- 历史轨迹查询

## 注意事项

1. **数据库配置**：确保MySQL数据库已正确配置并导入相关表结构
2. **短信服务**：需要配置短信服务商的API密钥
3. **地图API**：需要申请高德地图API密钥
4. **GPS设备**：确保GPS设备能够正常推送数据到系统接口
5. **网络环境**：系统需要稳定的网络环境以保证实时性

## 技术支持

如有问题，请参考以下文档：
- [GPS围栏系统详细文档](./GPS_FENCE_SYSTEM_README.md)
- [短信模块使用说明](./SMS_MODULE_DOCUMENTATION.md)
- [地图选点功能指南](./MAP_PICKER_GUIDE.md)
- [API接口文档](./api-docs.md)
- [数据库设计文档](./geo_fence_system.sql)

---

**CloudCare智慧养老系统** - 基于Spring Boot + Vue.js的现代化养老管理平台

© 2024 CloudCare Team. All rights reserved.