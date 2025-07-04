package com.cloudcare.enums;

/**
 * 短信模板枚举
 */
public enum SmsTemplateEnum {
    
    /**
     * 注册欢迎短信
     */
    REGISTER_WELCOME("REGISTER_WELCOME", "【云护CloudCare平台】感谢您的注册，欢迎来到云护CloudCare智慧系统。"),
    
    /**
     * 登录安全提醒
     */
    LOGIN_SECURITY("LOGIN_SECURITY", "【云护CloudCare平台】安全提醒：您的账户{username}在{loginTime}有新的登录记录，登录地点：{location}。如非本人操作请及时修改密码。"),
    
    /**
     * 消息提醒
     */
    MESSAGE_NOTIFICATION("MESSAGE_NOTIFICATION", "【云护CloudCare平台】消息提醒：您有来自{senderName}的新消息，内容：{messageContent}。请及时回复。"),
    
    /**
     * 服务通知
     */
    SERVICE_NOTIFICATION("SERVICE_NOTIFICATION", "【云护CloudCare平台】服务通知：{serviceName}已安排在{scheduledTime}为{elderlyName}提供服务，服务人员：{serviceProvider}，联系电话：{contactPhone}。"),
    
    /**
     * 就诊提醒
     */
    APPOINTMENT_REMINDER("APPOINTMENT_REMINDER", "【云护CloudCare平台】就诊提醒：{elderlyName}明天{appointmentTime}在{hospitalName}{department}有预约，请提前30分钟到达。如需改期请及时联系。"),
    
    /**
     * 设备提醒
     */
    DEVICE_ALERT("DEVICE_ALERT", "【云护CloudCare平台】设备提醒：老人{elderlyName}的{deviceName}({deviceCode})出现故障，请及时检查或联系维修。设备状态：{deviceStatus}。"),
    
    /**
     * 紧急提醒
     */
    EMERGENCY_ALERT("EMERGENCY_ALERT", "【云护CloudCare平台】紧急提醒：您关注的老人{elderlyName}出现{alertType}异常，{alertDescription}。请及时关注并联系医护人员。详情请登录系统查看。"),
    
    /**
     * 围栏离开提醒
     */
    FENCE_LEAVE_ALERT("FENCE_LEAVE_ALERT", "【云护CloudCare】老人{elder_name}已离开电子围栏\"{fence_name}\"，当前位置：纬度{latitude}，经度{longitude}，事件时间：{event_time}，请及时关注。"),
    
    /**
     * 围栏进入提醒
     */
    FENCE_ENTER_ALERT("FENCE_ENTER_ALERT", "【云护CloudCare】老人{elder_name}已进入电子围栏\"{fence_name}\"，当前位置：纬度{latitude}，经度{longitude}，事件时间：{event_time}，请及时关注。");
    
    private final String code;
    private final String template;
    
    SmsTemplateEnum(String code, String template) {
        this.code = code;
        this.template = template;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getTemplate() {
        return template;
    }
    
    /**
     * 根据代码获取模板
     */
    public static SmsTemplateEnum getByCode(String code) {
        for (SmsTemplateEnum template : values()) {
            if (template.getCode().equals(code)) {
                return template;
            }
        }
        return null;
    }
}