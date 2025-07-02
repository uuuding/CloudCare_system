# 🛡️ 老人防丢电子围栏系统

基于GPS定位器的老人防丢电子围栏系统，实现实时位置监控、围栏区域设置、进出告警等功能。

## 📋 系统概述

本系统利用车辆防盗GPS定位器改造为老人防丢电子围栏，通过接收GPS设备推送的定位数据，结合电子围栏配置，实现老人位置的实时监控和安全告警。

### 🎯 主要功能

- **实时定位监控**：接收GPS设备推送的位置数据
- **电子围栏管理**：支持圆形和多边形围栏设置
- **进出告警**：老人进入或离开围栏时自动发送短信告警
- **历史轨迹**：记录老人的位置历史和围栏事件
- **设备绑定**：支持GPS设备与老人信息的绑定管理
- **数据统计**：提供围栏事件统计和分析

## 🏗️ 系统架构

```
老人防丢电子围栏系统
├── GPS设备数据接收层
│   ├── GpsController - GPS数据接收接口
│   └── GpsDataDTO - GPS数据传输对象
├── 业务逻辑层
│   ├── GpsLocationService - GPS定位数据服务
│   ├── GeoFenceService - 电子围栏服务
│   └── GeoFenceEventService - 围栏事件服务
├── 数据存储层
│   ├── gps_location - GPS定位数据表
│   ├── geo_fence - 电子围栏配置表
│   └── geo_fence_event - 围栏事件记录表
└── 告警通知层
    ├── SmsService - 短信告警服务
    └── 定时任务 - 数据清理和告警处理
```

## 📦 核心组件

### 1. 数据接收接口

**接口地址**：`POST /api/gps/push`

**请求参数**：
- `method`: 数据类型标识（status）
- `serialNumber`: 推送序号（毫秒时间戳）
- `data`: GPS设备数据数组（JSON格式）

**响应格式**：返回请求中的 `serialNumber` 值

### 2. 核心实体类

#### GpsLocation - GPS定位数据
```java
@Data
@TableName("gps_location")
public class GpsLocation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String macid;        // 设备编号（IMEI）
    private Integer elderlyId;   // 老人ID
    private Long gpsTime;        // GPS时间戳
    private Double lat;          // 纬度
    private Double lon;          // 经度
    private Double speed;        // 速度
    private Double dir;          // 方向角度
    // ... 其他字段
}
```

#### GeoFence - 电子围栏配置
```java
@Data
@TableName("geo_fence")
public class GeoFence {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer elderlyId;      // 老人ID
    private String fenceName;       // 围栏名称
    private String fenceType;       // 围栏类型：circle/polygon
    private Double centerLat;       // 中心点纬度（圆形）
    private Double centerLon;       // 中心点经度（圆形）
    private Double radius;          // 半径（米，圆形）
    private String coordinates;     // 坐标点（JSON，多边形）
    private String emergencyContacts; // 紧急联系人
    // ... 其他字段
}
```

#### GeoFenceEvent - 围栏事件记录
```java
@Data
@TableName("geo_fence_event")
public class GeoFenceEvent {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer elderlyId;   // 老人ID
    private Long fenceId;        // 围栏ID
    private String eventType;    // 事件类型：enter/exit
    private Double lat;          // 事件发生位置
    private Double lon;
    private LocalDateTime eventTime; // 事件时间
    private Boolean alertSent;   // 是否已发送告警
    // ... 其他字段
}
```

### 3. 核心服务接口

#### GpsLocationService - GPS定位服务
```java
public interface GpsLocationService {
    // 处理GPS数据推送
    String processGpsDataPush(GpsDataDTO gpsData);
    
    // 保存GPS记录
    void saveGpsLocation(GpsLocation location);
    
    // 查询最新位置
    GpsLocation getLatestLocationByElderlyId(Integer elderlyId);
    
    // 绑定设备到老人
    void bindDeviceToElderly(String macid, Integer elderlyId);
    
    // 清理旧数据
    int cleanOldLocationData(int days);
}
```

#### GeoFenceService - 电子围栏服务
```java
public interface GeoFenceService {
    // 创建围栏
    void createFence(GeoFence fence);
    
    // 检查位置是否在围栏内
    boolean isLocationInFence(Double lat, Double lon, GeoFence fence);
    
    // 检查围栏事件
    void checkFenceEvents(Integer elderlyId, Double lat, Double lon, Long locationId, String macid);
    
    // 计算距离
    double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2);
}
```

## 🚀 部署指南

### 1. 环境要求

- Java 8+
- MySQL 5.7+
- Redis（可选，用于缓存）
- Spring Boot 2.7.x

### 2. 数据库初始化

执行 `geo_fence_system.sql` 脚本创建相关表：

```sql
-- 创建GPS定位数据表
CREATE TABLE gps_location (...)

-- 创建电子围栏配置表
CREATE TABLE geo_fence (...)

-- 创建围栏事件记录表
CREATE TABLE geo_fence_event (...)
```

### 3. 配置文件

在 `application.yml` 中添加GPS系统配置：

```yaml
# 引入GPS配置
spring:
  profiles:
    include: gps

# GPS系统配置
gps:
  data:
    retention-days: 30
  fence:
    accuracy-threshold: 10
  alert:
    sms-enabled: true
    repeat-interval: 30
```

### 4. 启动应用

```bash
# 编译项目
mvn clean package

# 启动应用
java -jar cloudcare-backend.jar
```

### 5. GPS设备配置

将GPS设备的数据推送地址配置为：
```
http://8.137.152.246:8080/api/gps/push
```

## 📱 API接口文档

### GPS数据接收接口

**接口**：`POST /api/gps/push`

**请求示例**：
```
method=status&serialNumber=1506399055758&data=[{"Macid":"123456","Lat":"22.69937","Lon":"114.12503","GpsTime":1506399055758,"Speed":0,"Dir":0}]
```

**响应示例**：
```
1506399055758
```

### 设备绑定接口

**接口**：`POST /api/gps/bind`

**请求参数**：
```json
{
  "macid": "123456",
  "elderlyId": 1
}
```

### 围栏管理接口

**创建围栏**：`POST /api/geo-fence`

**请求参数**：
```json
{
  "elderlyId": 1,
  "fenceName": "家庭安全区域",
  "fenceType": "circle",
  "centerLat": 22.69937,
  "centerLon": 114.12503,
  "radius": 500,
  "emergencyContacts": "13800138000,13900139000"
}
```

**查询围栏**：`GET /api/geo-fence/elderly/{elderlyId}`

**围栏事件**：`GET /api/geo-fence/events/{elderlyId}`

## 🔧 配置说明

### GPS数据配置

```yaml
gps:
  data:
    retention-days: 30        # 数据保留天数
    batch-size: 100          # 批量处理大小
    enable-compression: true  # 启用数据压缩
```

### 围栏检测配置

```yaml
gps:
  fence:
    accuracy-threshold: 10    # 检测精度（米）
    min-check-interval: 30   # 最小检测间隔（秒）
    cache-duration: 5        # 缓存时间（分钟）
```

### 告警配置

```yaml
gps:
  alert:
    sms-enabled: true        # 启用短信告警
    app-push-enabled: true   # 启用应用推送
    repeat-interval: 30      # 重复发送间隔（分钟）
    max-retry-count: 3       # 最大重试次数
```

## 📊 监控和维护

### 1. 定时任务

- **数据清理**：每天凌晨2点清理过期GPS数据
- **告警处理**：每5分钟处理未发送的告警
- **统计报告**：每小时生成系统统计数据

### 2. 日志监控

关键日志位置：
- GPS数据接收：`com.cloudcare.controller.GpsController`
- 围栏检测：`com.cloudcare.service.impl.GeoFenceServiceImpl`
- 告警发送：`com.cloudcare.service.impl.SmsServiceImpl`

### 3. 性能优化

- 使用Redis缓存设备绑定关系
- 定期清理历史GPS数据
- 优化围栏检测算法
- 批量处理告警发送

## 🔍 故障排查

### 常见问题

1. **GPS数据接收失败**
   - 检查接口地址配置
   - 验证请求参数格式
   - 查看应用日志

2. **围栏检测不准确**
   - 调整检测精度阈值
   - 检查GPS数据质量
   - 验证围栏配置

3. **告警发送失败**
   - 检查短信服务配置
   - 验证手机号格式
   - 查看短信发送记录

### 调试命令

```bash
# 查看GPS数据接收日志
tail -f logs/cloudcare.log | grep "GpsController"

# 查看围栏事件日志
tail -f logs/cloudcare.log | grep "GeoFenceService"

# 查看短信发送日志
tail -f logs/cloudcare.log | grep "SmsService"
```

## 📞 技术支持

如有问题，请联系技术支持团队或查看详细的API文档。

---

**版本**：v1.0.0  
**更新时间**：2024-01-01  
**维护团队**：CloudCare开发团队