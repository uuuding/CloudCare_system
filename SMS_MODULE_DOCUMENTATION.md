# 云护CloudCare短信模块文档

## 📋 目录

- [模块概述](#模块概述)
- [架构设计](#架构设计)
- [配置说明](#配置说明)
- [核心接口](#核心接口)
- [短信模板](#短信模板)
- [使用示例](#使用示例)
- [异步处理](#异步处理)
- [错误处理](#错误处理)
- [最佳实践](#最佳实践)
- [故障排查](#故障排查)

## 🎯 模块概述

云护CloudCare短信模块是一个基于Spring Boot的短信服务组件，提供了完整的短信发送、模板管理、异步通知等功能。该模块集成了短信宝API，支持单条和批量短信发送，并为业务流程提供自动化的短信通知服务。

### 主要功能

- ✅ **基础短信发送**：支持单条和批量短信发送
- ✅ **模板化短信**：预定义短信模板，支持参数替换
- ✅ **异步通知**：业务事件触发的自动短信通知
- ✅ **余额查询**：实时查询短信账户余额
- ✅ **参数验证**：手机号格式验证和参数校验
- ✅ **错误处理**：完善的异常处理和日志记录
- ✅ **配置管理**：灵活的配置管理和开关控制

## 🏗️ 架构设计

### 核心组件

```
短信模块架构
├── SmsService (短信服务接口)
│   └── SmsServiceImpl (短信服务实现)
├── SmsNotificationService (短信通知服务接口)
│   └── SmsNotificationServiceImpl (短信通知服务实现)
├── SmsConfig (短信配置类)
├── SmsTemplateEnum (短信模板枚举)
└── RestTemplateConfig (HTTP客户端配置)
```

### 分层设计

1. **接口层 (Interface Layer)**
   - `SmsService`: 基础短信发送服务
   - `SmsNotificationService`: 业务通知服务

2. **实现层 (Implementation Layer)**
   - `SmsServiceImpl`: 短信发送核心逻辑
   - `SmsNotificationServiceImpl`: 业务通知处理逻辑

3. **配置层 (Configuration Layer)**
   - `SmsConfig`: 短信服务配置
   - `RestTemplateConfig`: HTTP客户端配置

4. **模板层 (Template Layer)**
   - `SmsTemplateEnum`: 短信模板定义

## ⚙️ 配置说明

### application.yml 配置

```yaml
# 短信配置
sms:
  username: your_username          # 短信宝用户名
  password: your_md5_password      # 短信宝密码(MD5加密)
  api-url: http://api.smsbao.com/sms    # 发送短信API地址
  query-url: http://api.smsbao.com/query # 查询余额API地址
  enabled: true                    # 是否启用短信服务
  signature: "【云护CloudCare平台】"   # 短信签名

# 日志配置
logging:
  level:
    com.cloudcare: debug           # 开启调试日志
```

### 配置类说明

```java
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
    private String username;        // 短信宝用户名
    private String password;        // 短信宝密码（MD5加密）
    private String apiUrl;          // 短信发送API地址
    private String queryUrl;        // 余额查询API地址
    private boolean enabled = true; // 服务开关
    private String signature;       // 短信签名
}
```

## 🔌 核心接口

### SmsService - 基础短信服务

#### 基础发送方法

```java
// 发送单条短信
boolean sendSms(String phone, String content);

// 批量发送短信
boolean sendBatchSms(String phones, String content);

// 模板短信发送
boolean sendTemplateSms(String phone, SmsTemplateEnum template, Map<String, Object> params);

// 批量模板短信发送
boolean sendBatchTemplateSms(String phones, SmsTemplateEnum template, Map<String, Object> params);
```

#### 业务专用方法

```java
// 注册欢迎短信
boolean sendRegisterWelcome(String phone);

// 登录安全提醒
boolean sendLoginSecurity(String phone, String username, String loginTime, String location);

// 消息通知
boolean sendMessageNotification(String phone, String senderName, String messageContent);

// 服务通知
boolean sendServiceNotification(String phone, String serviceName, String scheduledTime, 
                               String elderlyName, String serviceProvider, String contactPhone);

// 就诊提醒
boolean sendAppointmentReminder(String phone, String elderlyName, String appointmentTime, 
                               String hospitalName, String department);

// 设备提醒
boolean sendDeviceAlert(String phone, String elderlyName, String deviceName, 
                       String deviceCode, String deviceStatus);

// 紧急提醒
boolean sendEmergencyAlert(String phone, String elderlyName, String alertType, String alertDescription);

// 余额查询
String queryBalance();
```

### SmsNotificationService - 业务通知服务

```java
// 用户注册通知
void sendWelcomeNotification(User user);

// 登录安全通知
void sendLoginSecurityNotification(User user, String loginTime, String location);

// 服务调度通知
void sendServiceScheduleNotification(ServiceSchedule serviceSchedule);

// 医疗预约通知
void sendAppointmentNotification(MedicalAppointment appointment);

// 设备故障通知
void sendDeviceAlertNotification(DeviceInfo deviceInfo, ElderlyProfile elderlyProfile);

// 健康预警通知
void sendHealthAlertNotification(HealthAlert healthAlert);

// 家庭互动通知
void sendFamilyInteractionNotification(FamilyInteraction interaction);

// 批量服务提醒（定时任务）
void sendBatchServiceReminders(int hours);

// 批量预约提醒（定时任务）
void sendBatchAppointmentReminders(int hours);
```

## 📝 短信模板

### 模板定义

```java
public enum SmsTemplateEnum {
    // 注册欢迎
    REGISTER_WELCOME("REGISTER_WELCOME", 
        "【云护CloudCare平台】感谢您的注册，欢迎来到云护CloudCare智慧系统。"),
    
    // 登录安全提醒
    LOGIN_SECURITY("LOGIN_SECURITY", 
        "【云护CloudCare平台】安全提醒：您的账户{username}在{loginTime}有新的登录记录，登录地点：{location}。如非本人操作请及时修改密码。"),
    
    // 消息提醒
    MESSAGE_NOTIFICATION("MESSAGE_NOTIFICATION", 
        "【云护CloudCare平台】消息提醒：您有来自{senderName}的新消息，内容：{messageContent}。请及时回复。"),
    
    // 服务通知
    SERVICE_NOTIFICATION("SERVICE_NOTIFICATION", 
        "【云护CloudCare平台】服务通知：{serviceName}已安排在{scheduledTime}为{elderlyName}提供服务，服务人员：{serviceProvider}，联系电话：{contactPhone}。"),
    
    // 就诊提醒
    APPOINTMENT_REMINDER("APPOINTMENT_REMINDER", 
        "【云护CloudCare平台】就诊提醒：{elderlyName}明天{appointmentTime}在{hospitalName}{department}有预约，请提前30分钟到达。如需改期请及时联系。"),
    
    // 设备提醒
    DEVICE_ALERT("DEVICE_ALERT", 
        "【云护CloudCare平台】设备提醒：老人{elderlyName}的{deviceName}({deviceCode})出现故障，请及时检查或联系维修。设备状态：{deviceStatus}。"),
    
    // 紧急提醒
    EMERGENCY_ALERT("EMERGENCY_ALERT", 
        "【云护CloudCare平台】紧急提醒：您关注的老人{elderlyName}出现{alertType}异常，{alertDescription}。请及时关注并联系医护人员。详情请登录系统查看。");
}
```

### 模板参数说明

| 模板类型 | 参数 | 说明 |
|---------|------|------|
| LOGIN_SECURITY | username, loginTime, location | 用户名、登录时间、登录地点 |
| MESSAGE_NOTIFICATION | senderName, messageContent | 发送者姓名、消息内容 |
| SERVICE_NOTIFICATION | serviceName, scheduledTime, elderlyName, serviceProvider, contactPhone | 服务名称、安排时间、老人姓名、服务人员、联系电话 |
| APPOINTMENT_REMINDER | elderlyName, appointmentTime, hospitalName, department | 老人姓名、预约时间、医院名称、科室 |
| DEVICE_ALERT | elderlyName, deviceName, deviceCode, deviceStatus | 老人姓名、设备名称、设备编码、设备状态 |
| EMERGENCY_ALERT | elderlyName, alertType, alertDescription | 老人姓名、预警类型、预警描述 |

## 💡 使用示例

### 基础短信发送

```java
@Autowired
private SmsService smsService;

// 发送单条短信
boolean result = smsService.sendSms("13800138000", "您的验证码是：123456");

// 批量发送短信
boolean result = smsService.sendBatchSms("13800138000,13800138001", "系统维护通知");
```

### 模板短信发送

```java
// 发送登录安全提醒
Map<String, Object> params = new HashMap<>();
params.put("username", "张三");
params.put("loginTime", "2024-01-15 10:30");
params.put("location", "北京市朝阳区");

boolean result = smsService.sendTemplateSms(
    "13800138000", 
    SmsTemplateEnum.LOGIN_SECURITY, 
    params
);
```

### 业务通知发送

```java
@Autowired
private SmsNotificationService smsNotificationService;

// 用户注册后自动发送欢迎短信
User user = new User();
user.setPhone("13800138000");
smsNotificationService.sendWelcomeNotification(user);

// 服务调度通知
ServiceSchedule schedule = new ServiceSchedule();
// ... 设置调度信息
smsNotificationService.sendServiceScheduleNotification(schedule);
```

### 余额查询

```java
// 查询账户余额
String balance = smsService.queryBalance();
if (balance != null) {
    System.out.println("账户余额：" + balance);
} else {
    System.out.println("余额查询失败");
}
```

## 🔄 异步处理

### 异步配置

短信通知服务使用 `@Async` 注解实现异步处理，避免阻塞主业务流程：

```java
@Service
public class SmsNotificationServiceImpl implements SmsNotificationService {
    
    @Override
    @Async
    public void sendWelcomeNotification(User user) {
        // 异步发送欢迎短信
    }
}
```

### 线程池配置

```java
@Configuration
@EnableAsync
public class ScheduleConfig {
    
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("sms-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
```

## ❌ 错误处理

### 异常类型

1. **配置异常**：短信服务未启用或配置错误
2. **参数异常**：手机号格式错误或参数缺失
3. **网络异常**：API调用失败或超时
4. **编码异常**：字符编码处理失败
5. **业务异常**：短信宝API返回错误码

### 错误处理策略

```java
public boolean sendSms(String phone, String content) {
    // 1. 服务开关检查
    if (!smsConfig.isEnabled()) {
        log.warn("短信服务未启用");
        return false;
    }
    
    // 2. 参数验证
    if (!isValidPhone(phone)) {
        log.error("手机号格式不正确: {}", phone);
        return false;
    }
    
    try {
        // 3. 发送短信
        String response = restTemplate.getForObject(url, String.class);
        log.info("发送短信到 {} 响应: {}", phone, response);
        
        // 4. 结果判断
        return "0".equals(response);
    } catch (Exception e) {
        log.error("发送短信失败", e);
        return false;
    }
}
```

### 返回值检查

```java
@Async
public void sendWelcomeNotification(User user) {
    try {
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            boolean success = smsService.sendRegisterWelcome(user.getPhone());
            if (success) {
                log.info("已成功发送注册欢迎短信到: {}", user.getPhone());
            } else {
                log.error("发送注册欢迎短信失败到: {}", user.getPhone());
            }
        }
    } catch (Exception e) {
        log.error("发送注册欢迎短信异常", e);
    }
}
```

## 🎯 最佳实践

### 1. 配置管理

- ✅ 使用环境变量或配置中心管理敏感信息
- ✅ 为不同环境配置不同的短信服务开关
- ✅ 定期检查短信账户余额

### 2. 参数验证

- ✅ 严格验证手机号格式
- ✅ 限制短信内容长度
- ✅ 验证模板参数完整性

### 3. 性能优化

- ✅ 使用异步处理避免阻塞
- ✅ 合理配置线程池大小
- ✅ 批量发送时控制并发数

### 4. 安全考虑

- ✅ 密码使用MD5加密存储
- ✅ 实施发送频率限制
- ✅ 记录详细的操作日志

### 5. 监控告警

- ✅ 监控短信发送成功率
- ✅ 设置余额不足告警
- ✅ 监控异常发送频率

## 🔧 故障排查

### 常见问题

#### 1. 短信发送失败

**症状**：方法返回 false，日志显示发送失败

**排查步骤**：
1. 检查短信服务是否启用：`sms.enabled=true`
2. 验证短信宝账户配置是否正确
3. 检查网络连接是否正常
4. 查看短信宝API响应码

#### 2. 收到编码后的短信内容

**症状**：手机收到类似 `%E3%80%90%E4%BA%91%E6%8A%A4...` 的内容

**解决方案**：
- ✅ 确保短信内容不进行URL编码
- ✅ 只对用户名和密码进行URL编码

#### 3. 异步短信不发送

**症状**：业务流程正常，但短信未发送

**排查步骤**：
1. 检查 `@EnableAsync` 是否配置
2. 验证线程池配置是否正确
3. 查看异步方法是否被正确调用

#### 4. 手机号验证失败

**症状**：日志显示"手机号格式不正确"

**解决方案**：
- 检查手机号是否符合正则表达式：`^1[3-9]\d{9}$`
- 确保手机号不包含空格或特殊字符

### 调试技巧

#### 1. 开启调试日志

```yaml
logging:
  level:
    com.cloudcare.service.impl.SmsServiceImpl: debug
    com.cloudcare.service.impl.SmsNotificationServiceImpl: debug
```

#### 2. 查看关键日志

```
# 短信发送URL
DEBUG - 短信发送URL: http://api.smsbao.com/sms?u=username&p=password&m=13800138000&c=短信内容

# 短信内容
DEBUG - 短信内容: 【云护CloudCare平台】感谢您的注册...

# API响应
INFO  - 发送短信到 13800138000 响应: 0

# 发送结果
INFO  - 已成功发送注册欢迎短信到: 13800138000
```

#### 3. 测试工具

```java
@RestController
@RequestMapping("/test")
public class SmsTestController {
    
    @Autowired
    private SmsService smsService;
    
    @GetMapping("/sms")
    public String testSms(@RequestParam String phone, @RequestParam String content) {
        boolean result = smsService.sendSms(phone, content);
        return result ? "发送成功" : "发送失败";
    }
    
    @GetMapping("/balance")
    public String testBalance() {
        return smsService.queryBalance();
    }
}
```

---

## 📞 技术支持

如有问题，请联系开发团队或查看相关日志文件进行排查。

**文档版本**：v1.0  
**最后更新**：2024-01-15  
**维护团队**：云护CloudCare开发团队