# CloudCare GPS定位系统

## 系统概述

CloudCare GPS定位系统是一个完整的老人GPS定位监护解决方案，包含设备管理、位置监控、电子围栏、报警处理等核心功能。

## 功能模块

### 1. GPS设备管理
- **设备注册与管理**：支持GPS设备的添加、编辑、删除
- **设备绑定**：将GPS设备绑定到特定老人
- **设备状态监控**：实时监控设备在线状态、电量、信号等
- **设备定位**：查看设备当前位置和历史轨迹

### 2. 电子围栏
- **围栏创建**：支持圆形和多边形围栏
- **围栏管理**：围栏的启用、禁用、编辑、删除
- **报警设置**：进入报警或离开报警
- **时间控制**：设置围栏生效时间段

### 3. GPS报警监控
- **实时报警**：SOS紧急求助、低电量、围栏报警等
- **报警处理**：报警记录的查看、处理、备注
- **报警统计**：未处理报警数量、今日报警统计
- **报警历史**：历史报警记录查询和分析

### 4. 位置服务
- **实时定位**：获取设备当前位置
- **轨迹回放**：查看历史移动轨迹
- **位置记录**：存储所有位置数据
- **地图展示**：在地图上显示位置和轨迹

## 技术架构

### 后端技术栈
- **框架**：Spring Boot + MyBatis Plus
- **数据库**：MySQL 8.0
- **API**：RESTful API设计
- **数据接收**：支持GPS设备数据推送

### 前端技术栈
- **框架**：Vue 3 + Element Plus
- **地图**：百度地图API
- **状态管理**：Vue 3 Composition API
- **HTTP客户端**：Axios

## 数据库设计

### 核心表结构

#### 1. GPS设备表 (gps_device)
```sql
- id: 设备ID
- device_name: 设备名称
- imei: 设备IMEI号
- elderly_id: 绑定老人ID
- device_status: 设备状态(0-离线,1-在线,2-故障)
- last_gps_time: 最后GPS时间
- last_heartbeat_time: 最后心跳时间
```

#### 2. GPS位置记录表 (gps_location)
```sql
- id: 记录ID
- device_imei: 设备IMEI
- latitude/longitude: 经纬度
- speed: 速度
- direction: 方向
- location_time: 定位时间
```

#### 3. 电子围栏表 (geo_fence)
```sql
- id: 围栏ID
- fence_name: 围栏名称
- elderly_id: 关联老人ID
- fence_type: 围栏类型(1-圆形,2-多边形)
- alarm_type: 报警类型(1-进入,2-离开)
- center_latitude/longitude: 中心点坐标
- radius: 半径
```

#### 4. GPS报警记录表 (gps_alarm)
```sql
- id: 报警ID
- device_imei: 设备IMEI
- alarm_time: 报警时间
- alarm_type: 报警类型(1-SOS,2-低电量,3-围栏,4-震动,5-移动)
- process_status: 处理状态(0-未处理,1-已处理)
```

## API接口

### 设备管理接口
- `GET /api/gps/device/list` - 获取设备列表
- `POST /api/gps/device` - 添加设备
- `PUT /api/gps/device/{id}` - 更新设备
- `DELETE /api/gps/device/{id}` - 删除设备
- `POST /api/gps/device/bind` - 绑定设备

### 围栏管理接口
- `GET /api/gps/fence/list` - 获取围栏列表
- `POST /api/gps/fence` - 创建围栏
- `PUT /api/gps/fence/{id}` - 更新围栏
- `DELETE /api/gps/fence/{id}` - 删除围栏

### 报警管理接口
- `GET /api/gps/alarm/list` - 获取报警列表
- `POST /api/gps/alarm/process` - 处理报警
- `GET /api/gps/alarm/statistics` - 获取报警统计

### 数据推送接口
- `POST /api/gps/push/alarm` - 报警数据推送
- `POST /api/gps/push/location` - 位置数据推送
- `POST /api/gps/push/heartbeat` - 心跳数据推送

## 部署说明

### 1. 数据库初始化
```bash
# 执行SQL脚本创建表结构和示例数据
mysql -u root -p < src/main/resources/sql/gps_tables.sql
```

### 2. 后端启动
```bash
# 配置数据库连接
# 启动Spring Boot应用
mvn spring-boot:run
```

### 3. 前端启动
```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

## 使用指南

### 1. 设备管理
1. 进入"GPS设备管理"页面
2. 点击"添加设备"按钮
3. 填写设备信息（设备名称、IMEI号等）
4. 选择绑定的老人
5. 保存设备信息

### 2. 围栏设置
1. 进入"电子围栏"页面
2. 点击"添加围栏"按钮
3. 选择围栏类型（圆形或多边形）
4. 在地图上设置围栏范围
5. 配置报警类型和生效时间
6. 保存围栏设置

### 3. 报警监控
1. 进入"GPS报警监控"页面
2. 查看实时报警信息
3. 点击"处理"按钮处理报警
4. 填写处理结果和说明
5. 确认处理完成

### 4. 位置查看
1. 在设备列表中点击"定位"按钮
2. 查看设备当前位置
3. 查看历史轨迹
4. 分析移动模式

## 测试工具

系统提供了GPS数据推送测试工具，用于模拟GPS设备数据推送：

1. 进入"测试工具" -> "GPS推送测试"页面
2. 选择推送类型（报警、位置、心跳）
3. 填写测试数据或使用随机生成
4. 点击发送测试数据
5. 查看推送结果和响应日志

## 注意事项

1. **IMEI号唯一性**：每个GPS设备的IMEI号必须唯一
2. **坐标系统**：系统支持WGS84和百度坐标系转换
3. **时间格式**：所有时间数据使用标准datetime格式
4. **围栏精度**：围栏判断基于经纬度计算，精度约为10米
5. **数据安全**：敏感数据传输建议使用HTTPS

## 扩展功能

- **多设备支持**：一个老人可绑定多个GPS设备
- **家属通知**：报警时自动通知家属
- **数据分析**：位置数据的统计分析
- **设备管理**：远程设备配置和控制
- **API集成**：与第三方系统集成

## 技术支持

如有技术问题或功能建议，请联系开发团队。