package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.enums.SmsTemplateEnum;
import com.cloudcare.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 短信管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@Tag(name = "短信管理", description = "短信发送和管理相关接口")
public class SmsController {
    
    private final SmsService smsService;
    
    /**
     * 发送普通短信
     */
    @PostMapping("/send")
    @Operation(summary = "发送普通短信", description = "发送普通短信")
    public Result<Boolean> sendSms(@RequestBody Map<String, String> request) {
        try {
            String phone = request.get("phone");
            String content = request.get("content");
            
            if (phone == null || phone.trim().isEmpty()) {
                return Result.error("手机号不能为空");
            }
            if (content == null || content.trim().isEmpty()) {
                return Result.error("短信内容不能为空");
            }
            
            boolean result = smsService.sendSms(phone, content);
            return result ? Result.success(true, "短信发送成功") : Result.error("短信发送失败");
        } catch (Exception e) {
            log.error("发送短信异常", e);
            return Result.error("短信发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 批量发送短信
     */
    @PostMapping("/send/batch")
    @Operation(summary = "批量发送短信", description = "批量发送短信")
    public Result<Boolean> sendBatchSms(@RequestBody Map<String, String> request) {
        try {
            String phones = request.get("phones");
            String content = request.get("content");
            
            if (phones == null || phones.trim().isEmpty()) {
                return Result.error("手机号列表不能为空");
            }
            if (content == null || content.trim().isEmpty()) {
                return Result.error("短信内容不能为空");
            }
            
            boolean result = smsService.sendBatchSms(phones, content);
            return result ? Result.success(true, "批量短信发送成功") : Result.error("批量短信发送失败");
        } catch (Exception e) {
            log.error("批量发送短信异常", e);
            return Result.error("批量短信发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 根据模板发送短信
     */
    @PostMapping("/send/template")
    @Operation(summary = "根据模板发送短信", description = "根据模板发送短信")
    public Result<Boolean> sendTemplateSms(@RequestBody Map<String, Object> request) {
        try {
            String phone = (String) request.get("phone");
            String templateCode = (String) request.get("templateCode");
            @SuppressWarnings("unchecked")
            Map<String, Object> params = (Map<String, Object>) request.get("params");
            
            if (phone == null || phone.trim().isEmpty()) {
                return Result.error("手机号不能为空");
            }
            if (templateCode == null || templateCode.trim().isEmpty()) {
                return Result.error("模板代码不能为空");
            }
            
            SmsTemplateEnum template = SmsTemplateEnum.getByCode(templateCode);
            if (template == null) {
                return Result.error("模板不存在");
            }
            
            boolean result = smsService.sendTemplateSms(phone, template, params);
            return result ? Result.success(true, "模板短信发送成功") : Result.error("模板短信发送失败");
        } catch (Exception e) {
            log.error("发送模板短信异常", e);
            return Result.error("模板短信发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 批量发送模板短信
     */
    @PostMapping("/send/template/batch")
    @Operation(summary = "批量发送模板短信", description = "批量发送模板短信")
    public Result<Boolean> sendBatchTemplateSms(@RequestBody Map<String, Object> request) {
        try {
            String phones = (String) request.get("phones");
            String templateCode = (String) request.get("templateCode");
            @SuppressWarnings("unchecked")
            Map<String, Object> params = (Map<String, Object>) request.get("params");
            
            if (phones == null || phones.trim().isEmpty()) {
                return Result.error("手机号列表不能为空");
            }
            if (templateCode == null || templateCode.trim().isEmpty()) {
                return Result.error("模板代码不能为空");
            }
            
            SmsTemplateEnum template = SmsTemplateEnum.getByCode(templateCode);
            if (template == null) {
                return Result.error("模板不存在");
            }
            
            // 分割手机号并逐个发送
            String[] phoneArray = phones.split(",");
            boolean allSuccess = true;
            for (String phone : phoneArray) {
                phone = phone.trim();
                if (!phone.isEmpty()) {
                    boolean result = smsService.sendTemplateSms(phone, template, params);
                    if (!result) {
                        allSuccess = false;
                    }
                }
            }
            
            return allSuccess ? Result.success(true, "批量模板短信发送成功") : Result.error("部分模板短信发送失败");
        } catch (Exception e) {
            log.error("批量发送模板短信异常", e);
            return Result.error("批量模板短信发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 查询账户余额
     */
    @GetMapping("/balance")
    @Operation(summary = "查询账户余额", description = "查询短信宝账户余额")
    public Result<String> queryBalance() {
        try {
            String balance = smsService.queryBalance();
            return balance != null ? Result.success(balance, "查询成功") : Result.error("查询失败");
        } catch (Exception e) {
            log.error("查询余额异常", e);
            return Result.error("查询余额异常: " + e.getMessage());
        }
    }
    
    /**
     * 发送注册欢迎短信
     */
    @PostMapping("/send/register-welcome")
    @Operation(summary = "发送注册欢迎短信", description = "发送注册欢迎短信")
    public Result<Boolean> sendRegisterWelcome(
            @Parameter(description = "手机号") @RequestParam String phone) {
        try {
            boolean result = smsService.sendRegisterWelcome(phone);
            return result ? Result.success(true, "欢迎短信发送成功") : Result.error("欢迎短信发送失败");
        } catch (Exception e) {
            log.error("发送欢迎短信异常", e);
            return Result.error("欢迎短信发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 发送登录安全提醒
     */
    @PostMapping("/send/login-security")
    @Operation(summary = "发送登录安全提醒", description = "发送登录安全提醒")
    public Result<Boolean> sendLoginSecurity(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "登录时间") @RequestParam String loginTime,
            @Parameter(description = "登录地点") @RequestParam String location) {
        try {
            boolean result = smsService.sendLoginSecurity(phone, username, loginTime, location);
            return result ? Result.success(true, "安全提醒发送成功") : Result.error("安全提醒发送失败");
        } catch (Exception e) {
            log.error("发送安全提醒异常", e);
            return Result.error("安全提醒发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 发送服务通知
     */
    @PostMapping("/send/service-notification")
    @Operation(summary = "发送服务通知", description = "发送服务通知")
    public Result<Boolean> sendServiceNotification(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "服务名称") @RequestParam String serviceName,
            @Parameter(description = "安排时间") @RequestParam String scheduledTime,
            @Parameter(description = "老人姓名") @RequestParam String elderlyName,
            @Parameter(description = "服务人员") @RequestParam String serviceProvider,
            @Parameter(description = "联系电话") @RequestParam String contactPhone) {
        try {
            boolean result = smsService.sendServiceNotification(phone, serviceName, scheduledTime, 
                    elderlyName, serviceProvider, contactPhone);
            return result ? Result.success(true, "服务通知发送成功") : Result.error("服务通知发送失败");
        } catch (Exception e) {
            log.error("发送服务通知异常", e);
            return Result.error("服务通知发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 发送就诊提醒
     */
    @PostMapping("/send/appointment-reminder")
    @Operation(summary = "发送就诊提醒", description = "发送就诊提醒")
    public Result<Boolean> sendAppointmentReminder(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "老人姓名") @RequestParam String elderlyName,
            @Parameter(description = "预约时间") @RequestParam String appointmentTime,
            @Parameter(description = "医院名称") @RequestParam String hospitalName,
            @Parameter(description = "科室") @RequestParam String department) {
        try {
            boolean result = smsService.sendAppointmentReminder(phone, elderlyName, appointmentTime, 
                    hospitalName, department);
            return result ? Result.success(true, "就诊提醒发送成功") : Result.error("就诊提醒发送失败");
        } catch (Exception e) {
            log.error("发送就诊提醒异常", e);
            return Result.error("就诊提醒发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 获取短信发送记录
     */
    @GetMapping("/records")
    @Operation(summary = "获取短信发送记录", description = "分页获取短信发送记录")
    public Result<Map<String, Object>> getSendRecords(
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "发送状态") @RequestParam(required = false) String status,
            @Parameter(description = "短信类型") @RequestParam(required = false) String type,
            @Parameter(description = "开始时间") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) String endTime,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> result = smsService.getSendRecords(phone, status, type, startTime, endTime, page, size);
            return Result.success(result, "查询成功");
        } catch (Exception e) {
            log.error("查询发送记录异常", e);
            return Result.error("查询发送记录异常: " + e.getMessage());
        }
    }
    
    /**
     * 获取短信发送统计
     */
    @GetMapping("/stats")
    @Operation(summary = "获取短信发送统计", description = "获取今日短信发送统计信息")
    public Result<Map<String, Object>> getSendStats() {
        try {
            Map<String, Object> stats = smsService.getSendStats();
            return Result.success(stats, "查询成功");
        } catch (Exception e) {
            log.error("查询发送统计异常", e);
            return Result.error("查询发送统计异常: " + e.getMessage());
        }
    }
    
    /**
     * 发送设备提醒
     */
    @PostMapping("/send/device-alert")
    @Operation(summary = "发送设备提醒", description = "发送设备提醒")
    public Result<Boolean> sendDeviceAlert(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "老人姓名") @RequestParam String elderlyName,
            @Parameter(description = "设备名称") @RequestParam String deviceName,
            @Parameter(description = "设备编码") @RequestParam String deviceCode,
            @Parameter(description = "设备状态") @RequestParam String deviceStatus) {
        try {
            boolean result = smsService.sendDeviceAlert(phone, elderlyName, deviceName, deviceCode, deviceStatus);
            return result ? Result.success(true, "设备提醒发送成功") : Result.error("设备提醒发送失败");
        } catch (Exception e) {
            log.error("发送设备提醒异常", e);
            return Result.error("设备提醒发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 发送紧急提醒
     */
    @PostMapping("/send/emergency-alert")
    @Operation(summary = "发送紧急提醒", description = "发送紧急提醒")
    public Result<Boolean> sendEmergencyAlert(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "老人姓名") @RequestParam String elderlyName,
            @Parameter(description = "预警类型") @RequestParam String alertType,
            @Parameter(description = "预警描述") @RequestParam String alertDescription) {
        try {
            boolean result = smsService.sendEmergencyAlert(phone, elderlyName, alertType, alertDescription);
            return result ? Result.success(true, "紧急提醒发送成功") : Result.error("紧急提醒发送失败");
        } catch (Exception e) {
            log.error("发送紧急提醒异常", e);
            return Result.error("紧急提醒发送异常: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有短信模板
     */
    @GetMapping("/templates")
    @Operation(summary = "获取所有短信模板", description = "获取所有可用的短信模板")
    public Result<SmsTemplateEnum[]> getTemplates() {
        return Result.success(SmsTemplateEnum.values(), "获取成功");
    }
}