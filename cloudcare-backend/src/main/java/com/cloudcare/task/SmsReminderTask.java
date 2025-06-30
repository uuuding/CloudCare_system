package com.cloudcare.task;

import com.cloudcare.service.SmsNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 短信提醒定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsReminderTask {
    
    private final SmsNotificationService smsNotificationService;
    
    /**
     * 每小时执行一次，发送1小时后的服务提醒
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void sendServiceReminders() {
        try {
            log.info("开始执行服务提醒定时任务");
            smsNotificationService.sendBatchServiceReminders(1);
            log.info("服务提醒定时任务执行完成");
        } catch (Exception e) {
            log.error("服务提醒定时任务执行失败", e);
        }
    }
    
    /**
     * 每天早上8点执行，发送当天的预约提醒
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendTodayAppointmentReminders() {
        try {
            log.info("开始执行今日预约提醒定时任务");
            smsNotificationService.sendBatchAppointmentReminders(0);
            log.info("今日预约提醒定时任务执行完成");
        } catch (Exception e) {
            log.error("今日预约提醒定时任务执行失败", e);
        }
    }
    
    /**
     * 每天晚上8点执行，发送明天的预约提醒
     */
    @Scheduled(cron = "0 0 20 * * ?")
    public void sendTomorrowAppointmentReminders() {
        try {
            log.info("开始执行明日预约提醒定时任务");
            smsNotificationService.sendBatchAppointmentReminders(24);
            log.info("明日预约提醒定时任务执行完成");
        } catch (Exception e) {
            log.error("明日预约提醒定时任务执行失败", e);
        }
    }
    
    /**
     * 每30分钟执行一次，发送30分钟后的服务提醒
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void sendUrgentServiceReminders() {
        try {
            log.info("开始执行紧急服务提醒定时任务");
            // 发送30分钟后的服务提醒
            smsNotificationService.sendBatchServiceReminders(0);
            log.info("紧急服务提醒定时任务执行完成");
        } catch (Exception e) {
            log.error("紧急服务提醒定时任务执行失败", e);
        }
    }
}