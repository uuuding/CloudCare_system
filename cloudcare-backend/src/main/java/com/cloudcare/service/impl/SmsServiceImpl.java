package com.cloudcare.service.impl;

import com.cloudcare.config.SmsConfig;
import com.cloudcare.enums.SmsTemplateEnum;
import com.cloudcare.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    
    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    @Override
    public boolean sendSms(String phone, String content) {
        if (!smsConfig.isEnabled()) {
            log.warn("短信服务未启用");
            return false;
        }
        
        if (!isValidPhone(phone)) {
            log.error("手机号格式不正确: {}", phone);
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
            
            return "0".equals(response);
        } catch (UnsupportedEncodingException e) {
            log.error("短信内容编码失败", e);
            return false;
        } catch (Exception e) {
            log.error("发送短信失败", e);
            return false;
        }
    }
    
    @Override
    public boolean sendBatchSms(String phones, String content) {
        if (!smsConfig.isEnabled()) {
            log.warn("短信服务未启用");
            return false;
        }
        
        // 验证手机号格式
        String[] phoneArray = phones.split(",");
        for (String phone : phoneArray) {
            if (!isValidPhone(phone.trim())) {
                log.error("手机号格式不正确: {}", phone);
                return false;
            }
        }
        
        if (phoneArray.length > 99) {
            log.error("批量发送手机号数量不能超过99个");
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
            
            return "0".equals(response);
        } catch (UnsupportedEncodingException e) {
            log.error("短信内容编码失败", e);
            return false;
        } catch (Exception e) {
            log.error("批量发送短信失败", e);
            return false;
        }
    }
    
    @Override
    public boolean sendTemplateSms(String phone, SmsTemplateEnum template, Map<String, Object> params) {
        String content = buildTemplateContent(template, params);
        return sendSms(phone, content);
    }
    
    @Override
    public boolean sendBatchTemplateSms(String phones, SmsTemplateEnum template, Map<String, Object> params) {
        String content = buildTemplateContent(template, params);
        return sendBatchSms(phones, content);
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
}