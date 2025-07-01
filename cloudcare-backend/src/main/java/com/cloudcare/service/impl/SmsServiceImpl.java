package com.cloudcare.service.impl;

import com.cloudcare.config.SmsConfig;
import com.cloudcare.entity.SmsRecord;
import com.cloudcare.enums.SmsTemplateEnum;
import com.cloudcare.mapper.SmsRecordMapper;
import com.cloudcare.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 短信服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    
    private final SmsConfig smsConfig;
    private final RestTemplate restTemplate;
    private final SmsRecordMapper smsRecordMapper;
    
    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    @Override
    public boolean sendSms(String phone, String content) {
        if (!smsConfig.isEnabled()) {
            log.warn("短信服务未启用");
            saveSmsRecord(phone, content, "normal", null, null, "failed", null, "短信服务未启用", null);
            return false;
        }
        
        if (!isValidPhone(phone)) {
            log.error("手机号格式不正确: {}", phone);
            saveSmsRecord(phone, content, "normal", null, null, "failed", null, "手机号格式不正确", null);
            return false;
        }
        
        try {
            // 只对用户名和密码进行URL编码，短信内容不编码
            String encodedUsername = URLEncoder.encode(smsConfig.getUsername(), "UTF-8");
            String encodedPassword = URLEncoder.encode(smsConfig.getPassword(), "UTF-8");
            
            String url = String.format("%s?u=%s&p=%s&m=%s&c=%s",
                    smsConfig.getApiUrl(),
                    encodedUsername,
                    encodedPassword,
                    phone,
                    content);
            
            log.debug("短信发送URL: {}", url);
            log.debug("短信内容: {}", content);
            
            String response = restTemplate.getForObject(url, String.class);
            log.info("发送短信到 {} 响应: {}", phone, response);
            
            boolean success = "0".equals(response);
            String status = success ? "success" : "failed";
            String errorMessage = success ? null : "发送失败，响应码: " + response;
            
            // 保存发送记录
            saveSmsRecord(phone, content, "normal", null, null, status, response, errorMessage, null);
            
            return success;
        } catch (UnsupportedEncodingException e) {
            log.error("短信内容编码失败", e);
            saveSmsRecord(phone, content, "normal", null, null, "failed", null, "短信内容编码失败: " + e.getMessage(), null);
            return false;
        } catch (Exception e) {
            log.error("发送短信失败", e);
            saveSmsRecord(phone, content, "normal", null, null, "failed", null, "发送短信失败: " + e.getMessage(), null);
            return false;
        }
    }
    
    @Override
    public boolean sendBatchSms(String phones, String content) {
        if (!smsConfig.isEnabled()) {
            log.warn("短信服务未启用");
            saveSmsRecord(phones, content, "batch", null, null, "failed", null, "短信服务未启用", null);
            return false;
        }
        
        // 验证手机号格式
        String[] phoneArray = phones.split(",");
        for (String phone : phoneArray) {
            if (!isValidPhone(phone.trim())) {
                log.error("手机号格式不正确: {}", phone);
                saveSmsRecord(phones, content, "batch", null, null, "failed", null, "手机号格式不正确: " + phone, null);
                return false;
            }
        }
        
        if (phoneArray.length > 99) {
            log.error("批量发送手机号数量不能超过99个");
            saveSmsRecord(phones, content, "batch", null, null, "failed", null, "批量发送手机号数量不能超过99个", null);
            return false;
        }
        
        try {
            // 对用户名和密码进行URL编码，短信内容不编码
            String encodedUsername = URLEncoder.encode(smsConfig.getUsername(), "UTF-8");
            String encodedPassword = URLEncoder.encode(smsConfig.getPassword(), "UTF-8");
            
            String url = String.format("%s?u=%s&p=%s&m=%s&c=%s",
                    smsConfig.getApiUrl(),
                    encodedUsername,
                    encodedPassword,
                    phones,
                    content);
            
            String response = restTemplate.getForObject(url, String.class);
            log.info("批量发送短信到 {} 响应: {}", phones, response);
            
            boolean success = "0".equals(response);
            String status = success ? "success" : "failed";
            String errorMessage = success ? null : "发送失败，响应码: " + response;
            
            // 保存发送记录
            saveSmsRecord(phones, content, "batch", null, null, status, response, errorMessage, null);
            
            return success;
        } catch (UnsupportedEncodingException e) {
            log.error("短信内容编码失败", e);
            saveSmsRecord(phones, content, "batch", null, null, "failed", null, "短信内容编码失败: " + e.getMessage(), null);
            return false;
        } catch (Exception e) {
            log.error("批量发送短信失败", e);
            saveSmsRecord(phones, content, "batch", null, null, "failed", null, "批量发送短信失败: " + e.getMessage(), null);
            return false;
        }
    }
    
    @Override
    public boolean sendTemplateSms(String phone, SmsTemplateEnum template, Map<String, Object> params) {
        if (!smsConfig.isEnabled()) {
            log.warn("短信服务未启用");
            String content = buildTemplateContent(template, params);
            String paramsJson = null;
             try {
                 if (params != null && !params.isEmpty()) {
                     com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                     paramsJson = mapper.writeValueAsString(params);
                 }
             } catch (Exception ex) {
                 log.warn("序列化模板参数失败", ex);
             }
            saveSmsRecord(phone, content, "template", template.getCode(), getTemplateDescription(template), 
                         "failed", null, "短信服务未启用", paramsJson);
            return false;
        }
        
        if (!isValidPhone(phone)) {
            log.error("手机号格式不正确: {}", phone);
            String content = buildTemplateContent(template, params);
            String paramsJson = null;
             try {
                 if (params != null && !params.isEmpty()) {
                     com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                     paramsJson = mapper.writeValueAsString(params);
                 }
             } catch (Exception ex) {
                 log.warn("序列化模板参数失败", ex);
             }
            saveSmsRecord(phone, content, "template", template.getCode(), getTemplateDescription(template), 
                         "failed", null, "手机号格式不正确", paramsJson);
            return false;
        }
        
        String content = buildTemplateContent(template, params);
        String paramsJson = null;
        try {
            if (params != null && !params.isEmpty()) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                paramsJson = mapper.writeValueAsString(params);
            }
        } catch (Exception e) {
            log.warn("序列化模板参数失败", e);
        }
        
        try {
            // 只对用户名和密码进行URL编码，短信内容不编码
            String encodedUsername = URLEncoder.encode(smsConfig.getUsername(), "UTF-8");
            String encodedPassword = URLEncoder.encode(smsConfig.getPassword(), "UTF-8");
            
            String url = String.format("%s?u=%s&p=%s&m=%s&c=%s",
                    smsConfig.getApiUrl(),
                    encodedUsername,
                    encodedPassword,
                    phone,
                    content);
            
            log.debug("模板短信发送URL: {}", url);
            log.debug("模板短信内容: {}", content);
            
            String response = restTemplate.getForObject(url, String.class);
            log.info("发送模板短信到 {} 响应: {}", phone, response);
            
            boolean success = "0".equals(response);
            String status = success ? "success" : "failed";
            String errorMessage = success ? null : "发送失败，响应码: " + response;
            
            // 保存发送记录
            saveSmsRecord(phone, content, "template", template.getCode(), getTemplateDescription(template), 
                         status, response, errorMessage, paramsJson);
            
            return success;
        } catch (UnsupportedEncodingException e) {
            log.error("短信内容编码失败", e);
            saveSmsRecord(phone, content, "template", template.getCode(), getTemplateDescription(template), 
                         "failed", null, "短信内容编码失败: " + e.getMessage(), paramsJson);
            return false;
        } catch (Exception e) {
            log.error("发送模板短信失败", e);
            saveSmsRecord(phone, content, "template", template.getCode(), getTemplateDescription(template), 
                         "failed", null, "发送模板短信失败: " + e.getMessage(), paramsJson);
            return false;
        }
    }
    
    @Override
    public boolean sendBatchTemplateSms(String phones, SmsTemplateEnum template, Map<String, Object> params) {
        String content = buildTemplateContent(template, params);
        String paramsJson = null;
        try {
            if (params != null && !params.isEmpty()) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                paramsJson = mapper.writeValueAsString(params);
            }
        } catch (Exception e) {
            log.warn("序列化模板参数失败", e);
        }
        
        if (!smsConfig.isEnabled()) {
            log.warn("短信服务未启用");
            saveSmsRecord(phones, content, "batch", template.getCode(), getTemplateDescription(template), 
                         "failed", null, "短信服务未启用", paramsJson);
            return false;
        }
        
        // 验证手机号格式
        String[] phoneArray = phones.split(",");
        for (String phone : phoneArray) {
            if (!isValidPhone(phone.trim())) {
                log.error("手机号格式不正确: {}", phone);
                saveSmsRecord(phones, content, "batch", template.getCode(), getTemplateDescription(template), 
                             "failed", null, "手机号格式不正确: " + phone, paramsJson);
                return false;
            }
        }
        
        if (phoneArray.length > 99) {
            log.error("批量发送手机号数量不能超过99个");
            saveSmsRecord(phones, content, "batch", template.getCode(), getTemplateDescription(template), 
                         "failed", null, "批量发送手机号数量不能超过99个", paramsJson);
            return false;
        }
        
        try {
            // 对用户名和密码进行URL编码，短信内容不编码
            String encodedUsername = URLEncoder.encode(smsConfig.getUsername(), "UTF-8");
            String encodedPassword = URLEncoder.encode(smsConfig.getPassword(), "UTF-8");
            
            String url = String.format("%s?u=%s&p=%s&m=%s&c=%s",
                    smsConfig.getApiUrl(),
                    encodedUsername,
                    encodedPassword,
                    phones,
                    content);
            
            String response = restTemplate.getForObject(url, String.class);
            log.info("批量发送模板短信到 {} 响应: {}", phones, response);
            
            boolean success = "0".equals(response);
            String status = success ? "success" : "failed";
            String errorMessage = success ? null : "发送失败，响应码: " + response;
            
            // 保存发送记录
            saveSmsRecord(phones, content, "batch", template.getCode(), getTemplateDescription(template), 
                         status, response, errorMessage, paramsJson);
            
            return success;
        } catch (UnsupportedEncodingException e) {
            log.error("短信内容编码失败", e);
            saveSmsRecord(phones, content, "batch", template.getCode(), getTemplateDescription(template), 
                         "failed", null, "短信内容编码失败: " + e.getMessage(), paramsJson);
            return false;
        } catch (Exception e) {
            log.error("批量发送模板短信失败", e);
            saveSmsRecord(phones, content, "batch", template.getCode(), getTemplateDescription(template), 
                         "failed", null, "批量发送模板短信失败: " + e.getMessage(), paramsJson);
            return false;
        }
    }
    
    @Override
    public String queryBalance() {
        if (!smsConfig.isEnabled()) {
            log.warn("短信服务未启用");
            return null;
        }
        
        try {
            String url = String.format("%s?u=%s&p=%s",
                    smsConfig.getQueryUrl(),
                    smsConfig.getUsername(),
                    smsConfig.getPassword());
            
            String response = restTemplate.getForObject(url, String.class);
            log.info("查询余额响应: {}", response);
            
            if (response != null && response.startsWith("0\n")) {
                return response.substring(2); // 返回余额信息
            }
            
            return null;
        } catch (Exception e) {
            log.error("查询余额失败", e);
            return null;
        }
    }
    
    @Override
    public boolean sendRegisterWelcome(String phone) {
        return sendTemplateSms(phone, SmsTemplateEnum.REGISTER_WELCOME, new HashMap<>());
    }
    
    @Override
    public boolean sendLoginSecurity(String phone, String username, String loginTime, String location) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("loginTime", loginTime);
        params.put("location", location);
        return sendTemplateSms(phone, SmsTemplateEnum.LOGIN_SECURITY, params);
    }
    
    @Override
    public boolean sendMessageNotification(String phone, String senderName, String messageContent) {
        Map<String, Object> params = new HashMap<>();
        params.put("senderName", senderName);
        params.put("messageContent", messageContent);
        return sendTemplateSms(phone, SmsTemplateEnum.MESSAGE_NOTIFICATION, params);
    }
    
    @Override
    public boolean sendServiceNotification(String phone, String serviceName, String scheduledTime,
                                         String elderlyName, String serviceProvider, String contactPhone) {
        Map<String, Object> params = new HashMap<>();
        params.put("serviceName", serviceName);
        params.put("scheduledTime", scheduledTime);
        params.put("elderlyName", elderlyName);
        params.put("serviceProvider", serviceProvider);
        params.put("contactPhone", contactPhone);
        return sendTemplateSms(phone, SmsTemplateEnum.SERVICE_NOTIFICATION, params);
    }
    
    @Override
    public boolean sendAppointmentReminder(String phone, String elderlyName, String appointmentTime,
                                         String hospitalName, String department) {
        Map<String, Object> params = new HashMap<>();
        params.put("elderlyName", elderlyName);
        params.put("appointmentTime", appointmentTime);
        params.put("hospitalName", hospitalName);
        params.put("department", department);
        return sendTemplateSms(phone, SmsTemplateEnum.APPOINTMENT_REMINDER, params);
    }
    
    @Override
    public boolean sendDeviceAlert(String phone, String elderlyName, String deviceName,
                                 String deviceCode, String deviceStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("elderlyName", elderlyName);
        params.put("deviceName", deviceName);
        params.put("deviceCode", deviceCode);
        params.put("deviceStatus", deviceStatus);
        return sendTemplateSms(phone, SmsTemplateEnum.DEVICE_ALERT, params);
    }
    
    @Override
    public boolean sendEmergencyAlert(String phone, String elderlyName, String alertType, String alertDescription) {
        Map<String, Object> params = new HashMap<>();
        params.put("elderlyName", elderlyName);
        params.put("alertType", alertType);
        params.put("alertDescription", alertDescription);
        return sendTemplateSms(phone, SmsTemplateEnum.EMERGENCY_ALERT, params);
    }
    
    /**
     * 构建模板内容
     */
    private String buildTemplateContent(SmsTemplateEnum template, Map<String, Object> params) {
        String content = template.getTemplate();
        
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                content = content.replace(placeholder, value);
            }
        }
        
        return content;
    }
    
    /**
     * 验证手机号格式
     */
    private boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 保存短信发送记录
     */
    private void saveSmsRecord(String phone, String content, String type, String templateCode, 
                              String templateName, String status, String responseCode, 
                              String errorMessage, String params) {
        try {
            SmsRecord record = new SmsRecord();
            record.setPhone(phone);
            record.setContent(content);
            record.setType(type);
            record.setTemplateCode(templateCode);
            record.setTemplateName(templateName);
            record.setStatus(status);
            record.setSendTime(LocalDateTime.now());
            record.setResponseCode(responseCode);
            record.setErrorMessage(errorMessage);
            record.setParams(params);
            
            smsRecordMapper.insert(record);
            log.debug("短信发送记录已保存: {}", record.getId());
        } catch (Exception e) {
            log.error("保存短信发送记录失败", e);
        }
    }
    
    /**
     * 获取模板描述
     */
    private String getTemplateDescription(SmsTemplateEnum template) {
        switch (template) {
            case REGISTER_WELCOME:
                return "注册欢迎";
            case LOGIN_SECURITY:
                return "登录安全提醒";
            case MESSAGE_NOTIFICATION:
                return "消息提醒";
            case SERVICE_NOTIFICATION:
                return "服务通知";
            case APPOINTMENT_REMINDER:
                return "就诊提醒";
            case DEVICE_ALERT:
                return "设备提醒";
            case EMERGENCY_ALERT:
                return "紧急提醒";
            default:
                return template.getCode();
        }
    }
    
    @Override
    public Map<String, Object> getSendRecords(String phone, String status, String type, 
                                            String startTime, String endTime, int page, int size) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 构建查询条件
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SmsRecord> queryWrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            
            // 添加查询条件
            if (phone != null && !phone.trim().isEmpty()) {
                queryWrapper.like("phone", phone.trim());
            }
            
            if (status != null && !status.trim().isEmpty()) {
                queryWrapper.eq("status", status.trim());
            }
            
            if (type != null && !type.trim().isEmpty()) {
                queryWrapper.eq("type", type.trim());
            }
            
            if (startTime != null && !startTime.trim().isEmpty()) {
                queryWrapper.ge("send_time", startTime.trim());
            }
            
            if (endTime != null && !endTime.trim().isEmpty()) {
                queryWrapper.le("send_time", endTime.trim());
            }
            
            // 按发送时间倒序排列
            queryWrapper.orderByDesc("send_time");
            
            // 分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<SmsRecord> pageParam = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
            
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<SmsRecord> pageResult = 
                smsRecordMapper.selectPage(pageParam, queryWrapper);
            
            // 转换为前端需要的格式
            java.util.List<Map<String, Object>> records = new java.util.ArrayList<>();
            for (SmsRecord record : pageResult.getRecords()) {
                Map<String, Object> recordMap = new HashMap<>();
                recordMap.put("id", record.getId());
                recordMap.put("phone", record.getPhone());
                recordMap.put("content", record.getContent());
                recordMap.put("type", record.getType());
                recordMap.put("template", record.getTemplateCode());
                recordMap.put("templateName", record.getTemplateName());
                recordMap.put("status", record.getStatus());
                recordMap.put("sendTime", record.getSendTime() != null ? 
                    record.getSendTime().toString().replace("T", " ") : null);
                recordMap.put("responseCode", record.getResponseCode());
                recordMap.put("errorMessage", record.getErrorMessage());
                records.add(recordMap);
            }
            
            result.put("records", records);
            result.put("total", pageResult.getTotal());
            result.put("page", pageResult.getCurrent());
            result.put("size", pageResult.getSize());
            result.put("pages", pageResult.getPages());
            
        } catch (Exception e) {
            log.error("查询短信发送记录失败", e);
            // 发生异常时返回空结果
            result.put("records", new java.util.ArrayList<>());
            result.put("total", 0);
            result.put("page", page);
            result.put("size", size);
            result.put("pages", 0);
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getSendStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 获取今日统计数据
            Map<String, Object> todayStats = smsRecordMapper.getTodayStats();
            
            if (todayStats != null) {
                Long todaySent = (Long) todayStats.get("todaySent");
                Long todaySuccess = (Long) todayStats.get("todaySuccess");
                Long todayFailed = (Long) todayStats.get("todayFailed");
                
                stats.put("todaySent", todaySent != null ? todaySent.intValue() : 0);
                stats.put("todaySuccess", todaySuccess != null ? todaySuccess.intValue() : 0);
                stats.put("todayFailed", todayFailed != null ? todayFailed.intValue() : 0);
                
                // 计算成功率
                if (todaySent != null && todaySent > 0 && todaySuccess != null) {
                    BigDecimal successRate = BigDecimal.valueOf(todaySuccess)
                            .divide(BigDecimal.valueOf(todaySent), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));
                    stats.put("successRate", successRate.setScale(2, RoundingMode.HALF_UP) + "%");
                } else {
                    stats.put("successRate", "0.00%");
                }
            } else {
                // 如果没有数据，返回默认值
                stats.put("todaySent", 0);
                stats.put("todaySuccess", 0);
                stats.put("todayFailed", 0);
                stats.put("successRate", "0.00%");
            }
        } catch (Exception e) {
            log.error("获取短信统计数据失败", e);
            // 发生异常时返回默认值
            stats.put("todaySent", 0);
            stats.put("todaySuccess", 0);
            stats.put("todayFailed", 0);
            stats.put("successRate", "0.00%");
        }
        
        return stats;
    }
}