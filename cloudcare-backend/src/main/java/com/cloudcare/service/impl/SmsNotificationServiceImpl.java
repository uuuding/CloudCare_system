package com.cloudcare.service.impl;

import com.cloudcare.entity.*;
import com.cloudcare.mapper.*;
import com.cloudcare.service.SmsNotificationService;
import com.cloudcare.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 短信通知服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsNotificationServiceImpl implements SmsNotificationService {
    
    private final SmsService smsService;
    private final UserMapper userMapper;
    private final ElderlyProfileMapper elderlyProfileMapper;
    private final FamilyMemberMapper familyMemberMapper;
    private final ServiceScheduleMapper serviceScheduleMapper;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    @Override
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
    
    @Override
    @Async
    public void sendLoginSecurityNotification(User user, String loginTime, String location) {
        try {
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                smsService.sendLoginSecurity(user.getPhone(), user.getUsername(), loginTime, location);
                log.info("已发送登录安全提醒到: {}", user.getPhone());
            }
        } catch (Exception e) {
            log.error("发送登录安全提醒失败", e);
        }
    }
    
    @Override
    @Async
    public void sendServiceScheduleNotification(ServiceSchedule serviceSchedule) {
        try {
            // 获取老人信息
            ElderlyProfile elderly = elderlyProfileMapper.selectById(serviceSchedule.getElderId());
            if (elderly == null) {
                log.warn("未找到老人信息: {}", serviceSchedule.getElderId());
                return;
            }
            
            // 获取服务人员信息
            User staff = userMapper.selectById(serviceSchedule.getStaffId());
            String serviceProvider = staff != null ? staff.getRealName() : "未指定";
            String contactPhone = staff != null ? staff.getPhone() : "";
            
            // 获取家属联系方式
            List<String> familyPhones = getFamilyPhones(serviceSchedule.getElderId());
            
            String scheduledTime = serviceSchedule.getScheduledStartTime().format(DATE_TIME_FORMATTER);
            
            for (String phone : familyPhones) {
                smsService.sendServiceNotification(phone, serviceSchedule.getServiceName(), 
                        scheduledTime, elderly.getName(), serviceProvider, contactPhone);
            }
            
            log.info("已发送服务通知，服务: {}, 老人: {}", serviceSchedule.getServiceName(), elderly.getName());
        } catch (Exception e) {
            log.error("发送服务通知失败", e);
        }
    }
    
    @Override
    @Async
    public void sendAppointmentNotification(MedicalAppointment appointment) {
        try {
            // 获取老人信息
            ElderlyProfile elderly = elderlyProfileMapper.selectById(appointment.getElderId());
            if (elderly == null) {
                log.warn("未找到老人信息: {}", appointment.getElderId());
                return;
            }
            
            // 获取家属联系方式
            List<String> familyPhones = getFamilyPhones(appointment.getElderId());
            
            String appointmentTime = appointment.getAppointmentTime().format(DATE_TIME_FORMATTER);
            
            for (String phone : familyPhones) {
                smsService.sendAppointmentReminder(phone, elderly.getName(), appointmentTime, 
                        "医院", "科室");
            }
            
            log.info("已发送预约提醒，老人: {}, 预约时间: {}", elderly.getName(), appointmentTime);
        } catch (Exception e) {
            log.error("发送预约提醒失败", e);
        }
    }
    
    @Override
    @Async
    public void sendDeviceAlertNotification(DeviceInfo deviceInfo, ElderlyProfile elderlyProfile) {
        try {
            // 获取家属联系方式
            List<String> familyPhones = getFamilyPhones((long)elderlyProfile.getId());
            
            String deviceStatus = getDeviceStatusText(deviceInfo.getDeviceStatus());
            
            for (String phone : familyPhones) {
                smsService.sendDeviceAlert(phone, elderlyProfile.getName(), 
                        deviceInfo.getDeviceName(), deviceInfo.getDeviceCode(), deviceStatus);
            }
            
            log.info("已发送设备提醒，设备: {}, 老人: {}", deviceInfo.getDeviceName(), elderlyProfile.getName());
        } catch (Exception e) {
            log.error("发送设备提醒失败", e);
        }
    }
    
    @Override
    @Async
    public void sendHealthAlertNotification(HealthAlert healthAlert) {
        try {
            // 获取家属联系方式
            List<String> familyPhones = getFamilyPhones((long)healthAlert.getElderlyId());
            
            for (String phone : familyPhones) {
                smsService.sendEmergencyAlert(phone, healthAlert.getElderlyName(), 
                        healthAlert.getAlertType(), healthAlert.getAlertDescription());
            }
            
            log.info("已发送健康预警通知，老人: {}, 预警类型: {}", 
                    healthAlert.getElderlyName(), healthAlert.getAlertType());
        } catch (Exception e) {
            log.error("发送健康预警通知失败", e);
        }
    }
    
    @Override
    @Async
    public void sendFamilyInteractionNotification(FamilyInteraction interaction) {
        try {
            // 获取发送者信息
            User sender = userMapper.selectById(interaction.getFamilyMemberId());
            String senderName = sender != null ? sender.getRealName() : "系统";
            
            // 获取接收者信息
            User receiver = userMapper.selectById(interaction.getElderId());
            if (receiver != null && receiver.getPhone() != null && !receiver.getPhone().isEmpty()) {
                smsService.sendMessageNotification(receiver.getPhone(), senderName, interaction.getContent());
                log.info("已发送消息通知到: {}", receiver.getPhone());
            }
        } catch (Exception e) {
            log.error("发送消息通知失败", e);
        }
    }
    
    @Override
    @Async
    public void sendBatchServiceReminders(int hours) {
        try {
            LocalDateTime startTime = LocalDateTime.now().plusHours(hours);
            LocalDateTime endTime = startTime.plusHours(1);
            
            // 查询即将开始的服务
            List<ServiceSchedule> upcomingServices = serviceScheduleMapper.findUpcomingServices(startTime, endTime);
            
            for (ServiceSchedule service : upcomingServices) {
                sendServiceScheduleNotification(service);
            }
            
            log.info("批量发送服务提醒完成，共 {} 条", upcomingServices.size());
        } catch (Exception e) {
            log.error("批量发送服务提醒失败", e);
        }
    }
    
    @Override
    @Async
    public void sendBatchAppointmentReminders(int hours) {
        try {
            LocalDateTime startTime = LocalDateTime.now().plusHours(hours);
            LocalDateTime endTime = startTime.plusHours(1);
            
            // 查询即将开始的预约
            List<MedicalAppointment> upcomingAppointments = medicalAppointmentMapper.findUpcomingAppointments(startTime, endTime);
            
            for (MedicalAppointment appointment : upcomingAppointments) {
                sendAppointmentNotification(appointment);
            }
            
            log.info("批量发送预约提醒完成，共 {} 条", upcomingAppointments.size());
        } catch (Exception e) {
            log.error("批量发送预约提醒失败", e);
        }
    }
    
    /**
     * 获取老人的家属联系方式
     */
    private List<String> getFamilyPhones(Long elderId) {
        // 这里需要根据实际的数据库结构来实现
        // 假设有一个查询家属联系方式的方法
        return familyMemberMapper.getFamilyPhonesByElderId(elderId);
    }
    
    /**
     * 获取设备状态文本
     */
    private String getDeviceStatusText(Integer status) {
        switch (status) {
            case 0:
                return "离线";
            case 1:
                return "正常";
            case 2:
                return "故障";
            case 3:
                return "维修中";
            default:
                return "未知";
        }
    }
}