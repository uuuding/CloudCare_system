package com.cloudcare.service;

import com.cloudcare.entity.*;

/**
 * 短信通知服务接口
 * 用于在业务流程中自动触发短信通知
 */
public interface SmsNotificationService {
    
    /**
     * 用户注册后发送欢迎短信
     * 
     * @param user 用户信息
     */
    void sendWelcomeNotification(User user);
    
    /**
     * 用户登录后发送安全提醒
     * 
     * @param user 用户信息
     * @param loginTime 登录时间
     * @param location 登录地点
     */
    void sendLoginSecurityNotification(User user, String loginTime, String location);
    
    /**
     * 服务调度创建后发送通知
     * 
     * @param serviceSchedule 服务调度信息
     */
    void sendServiceScheduleNotification(ServiceSchedule serviceSchedule);
    
    /**
     * 医疗预约创建后发送提醒
     * 
     * @param appointment 预约信息
     */
    void sendAppointmentNotification(MedicalAppointment appointment);
    
    /**
     * 设备故障时发送提醒
     * 
     * @param deviceInfo 设备信息
     * @param elderlyProfile 老人信息
     */
    void sendDeviceAlertNotification(DeviceInfo deviceInfo, ElderlyProfile elderlyProfile);
    
    /**
     * 健康预警时发送紧急提醒
     * 
     * @param healthAlert 健康预警信息
     */
    void sendHealthAlertNotification(HealthAlert healthAlert);
    
    /**
     * 家庭互动消息通知
     * 
     * @param interaction 互动信息
     */
    void sendFamilyInteractionNotification(FamilyInteraction interaction);
    
    /**
     * 批量发送服务提醒（定时任务使用）
     * 
     * @param hours 提前小时数
     */
    void sendBatchServiceReminders(int hours);
    
    /**
     * 批量发送预约提醒（定时任务使用）
     * 
     * @param hours 提前小时数
     */
    void sendBatchAppointmentReminders(int hours);
}