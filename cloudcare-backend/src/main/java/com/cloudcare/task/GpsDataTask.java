package com.cloudcare.task;

import com.cloudcare.service.GeoFenceEventService;
import com.cloudcare.service.GpsLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * GPS数据定时任务
 * 负责处理GPS数据清理和未发送告警
 * 
 * @author CloudCare Team
 * @since 2024-01-01
 */
@Slf4j
@Component
public class GpsDataTask {

    @Autowired
    private GpsLocationService gpsLocationService;
    
    @Autowired
    private GeoFenceEventService geoFenceEventService;
    
    @Value("${gps.data.retention-days:30}")
    private int retentionDays;
    
    /**
     * 清理过期的GPS定位数据
     * 每天凌晨2点执行
     */
    @Scheduled(cron = "${gps.schedule.data-cleanup-cron:0 0 2 * * ?}")
    public void cleanupOldGpsData() {
        log.info("开始清理{}天前的GPS定位数据", retentionDays);
        try {
            LocalDateTime beforeTime = LocalDateTime.now().minusDays(retentionDays);
            int deletedCount = gpsLocationService.cleanOldLocationData(beforeTime);
            log.info("GPS数据清理完成，删除了{}条记录", deletedCount);
        } catch (Exception e) {
            log.error("GPS数据清理失败", e);
        }
    }
    
    /**
     * 处理未发送的告警
     * 每5分钟执行一次
     */
    @Scheduled(fixedDelayString = "${gps.schedule.unsent-alert-interval:5}000", initialDelay = 60000)
    public void processUnsentAlerts() {
        log.debug("开始处理未发送的围栏告警");
        try {
            int processedCount = geoFenceEventService.processUnsentAlerts();
            if (processedCount > 0) {
                log.info("处理了{}条未发送的围栏告警", processedCount);
            }
        } catch (Exception e) {
            log.error("处理未发送告警失败", e);
        }
    }
    
    /**
     * 统计GPS数据和围栏事件
     * 每小时执行一次
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void statisticsGpsData() {
        log.debug("开始统计GPS数据和围栏事件");
        try {
            // 这里可以添加统计逻辑，比如：
            // 1. 统计当前在线设备数量
            // 2. 统计今日围栏事件数量
            // 3. 统计系统健康状态
            // 4. 发送统计报告等
            
            log.debug("GPS数据统计完成");
        } catch (Exception e) {
            log.error("GPS数据统计失败", e);
        }
    }
}