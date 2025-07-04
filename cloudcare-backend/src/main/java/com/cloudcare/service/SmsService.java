package com.cloudcare.service;

import com.cloudcare.enums.SmsTemplateEnum;

import java.util.Map;

/**
 * 短信服务接口
 */
public interface SmsService {
    
    /**
     * 发送短信
     * 
     * @param phone 手机号
     * @param content 短信内容
     * @return 发送结果
     */
    boolean sendSms(String phone, String content);
    
    /**
     * 批量发送短信
     * 
     * @param phones 手机号列表（逗号分隔）
     * @param content 短信内容
     * @return 发送结果
     */
    boolean sendBatchSms(String phones, String content);
    
    /**
     * 根据模板发送短信
     * 
     * @param phone 手机号
     * @param template 短信模板
     * @param params 模板参数
     * @return 发送结果
     */
    boolean sendTemplateSms(String phone, SmsTemplateEnum template, Map<String, Object> params);
    
    /**
     * 批量根据模板发送短信
     * 
     * @param phones 手机号列表（逗号分隔）
     * @param template 短信模板
     * @param params 模板参数
     * @return 发送结果
     */
    boolean sendBatchTemplateSms(String phones, SmsTemplateEnum template, Map<String, Object> params);
    
    /**
     * 查询账户余额
     * 
     * @return 余额信息（格式：发送条数,剩余条数）
     */
    String queryBalance();
    
    /**
     * 发送注册欢迎短信
     * 
     * @param phone 手机号
     * @return 发送结果
     */
    boolean sendRegisterWelcome(String phone);
    
    /**
     * 发送登录安全提醒
     * 
     * @param phone 手机号
     * @param username 用户名
     * @param loginTime 登录时间
     * @param location 登录地点
     * @return 发送结果
     */
    boolean sendLoginSecurity(String phone, String username, String loginTime, String location);
    
    /**
     * 发送消息提醒
     * 
     * @param phone 手机号
     * @param senderName 发送者姓名
     * @param messageContent 消息内容
     * @return 发送结果
     */
    boolean sendMessageNotification(String phone, String senderName, String messageContent);
    
    /**
     * 发送服务通知
     * 
     * @param phone 手机号
     * @param serviceName 服务名称
     * @param scheduledTime 安排时间
     * @param elderlyName 老人姓名
     * @param serviceProvider 服务人员
     * @param contactPhone 联系电话
     * @return 发送结果
     */
    boolean sendServiceNotification(String phone, String serviceName, String scheduledTime, 
                                  String elderlyName, String serviceProvider, String contactPhone);
    
    /**
     * 发送就诊提醒
     * 
     * @param phone 手机号
     * @param elderlyName 老人姓名
     * @param appointmentTime 预约时间
     * @param hospitalName 医院名称
     * @param department 科室
     * @return 发送结果
     */
    boolean sendAppointmentReminder(String phone, String elderlyName, String appointmentTime, 
                                  String hospitalName, String department);
    
    /**
     * 发送设备提醒
     * 
     * @param phone 手机号
     * @param elderlyName 老人姓名
     * @param deviceName 设备名称
     * @param deviceCode 设备编码
     * @param deviceStatus 设备状态
     * @return 发送结果
     */
    boolean sendDeviceAlert(String phone, String elderlyName, String deviceName, 
                          String deviceCode, String deviceStatus);
    
    /**
     * 发送紧急提醒
     * 
     * @param phone 手机号
     * @param elderlyName 老人姓名
     * @param alertType 预警类型
     * @param alertDescription 预警描述
     * @return 发送结果
     */
    boolean sendEmergencyAlert(String phone, String elderlyName, String alertType, String alertDescription);
    
    /**
     * 获取短信发送记录
     * 
     * @param phone 手机号（可选）
     * @param status 发送状态（可选）
     * @param type 短信类型（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    Map<String, Object> getSendRecords(String phone, String status, String type, 
                                      String startTime, String endTime, int page, int size);
    
    /**
     * 获取短信发送统计
     * 
     * @return 统计信息
     */
    Map<String, Object> getSendStats();
    
    /**
     * 发送围栏告警短信
     * 
     * @param phone 手机号
     * @param elderlyName 老人姓名
     * @param fenceName 围栏名称
     * @param eventType 事件类型（enter/exit）
     * @param eventTime 事件时间
     * @param location 位置信息
     * @return 发送结果
     */
    boolean sendFenceAlert(String phone, String elderlyName, String fenceName, 
                          String eventType, String eventTime, String location);
    
    /**
     * 发送围栏离开提醒（使用模板）
     * 
     * @param phone 手机号
     * @param elderName 老人姓名
     * @param fenceName 围栏名称
     * @param latitude 纬度
     * @param longitude 经度
     * @param eventTime 事件时间
     * @return 发送结果
     */
    boolean sendFenceLeaveAlert(String phone, String elderName, String fenceName, 
                               String latitude, String longitude, String eventTime);
    
    /**
     * 发送围栏进入提醒（使用模板）
     * 
     * @param phone 手机号
     * @param elderName 老人姓名
     * @param fenceName 围栏名称
     * @param latitude 纬度
     * @param longitude 经度
     * @param eventTime 事件时间
     * @return 发送结果
     */
    boolean sendFenceEnterAlert(String phone, String elderName, String fenceName, 
                               String latitude, String longitude, String eventTime);
}