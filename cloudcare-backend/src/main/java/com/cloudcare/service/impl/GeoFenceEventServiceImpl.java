package com.cloudcare.service.impl;

import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GeoFenceEvent;
import com.cloudcare.mapper.GeoFenceEventMapper;
import com.cloudcare.mapper.GeoFenceMapper;
import com.cloudcare.service.GeoFenceEventService;
import com.cloudcare.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 电子围栏事件服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeoFenceEventServiceImpl implements GeoFenceEventService {

    private final GeoFenceEventMapper geoFenceEventMapper;
    private final GeoFenceMapper geoFenceMapper;
    private final SmsService smsService;

    @Override
    public boolean saveGeoFenceEvent(GeoFenceEvent geoFenceEvent) {
        try {
            geoFenceEvent.setCreateTime(LocalDateTime.now());
            return geoFenceEventMapper.insert(geoFenceEvent) > 0;
        } catch (Exception e) {
            log.error("保存围栏事件记录失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<GeoFenceEvent> getEventsByElderlyId(Integer elderlyId) {
        return geoFenceEventMapper.getEventsByElderlyId(elderlyId);
    }

    @Override
    public List<GeoFenceEvent> getEventsByElderlyIdAndTimeRange(Integer elderlyId, LocalDateTime startTime, LocalDateTime endTime) {
        return geoFenceEventMapper.getEventsByElderlyIdAndTimeRange(elderlyId, startTime, endTime);
    }

    @Override
    public List<GeoFenceEvent> getEventsByFenceId(Long fenceId) {
        return geoFenceEventMapper.getEventsByFenceId(fenceId);
    }

    @Override
    public List<GeoFenceEvent> getUnsentAlertEvents() {
        return geoFenceEventMapper.getUnsentAlertEvents();
    }

    @Override
    public boolean updateAlertSentStatus(Long eventId) {
        try {
            return geoFenceEventMapper.updateAlertSentStatus(eventId) > 0;
        } catch (Exception e) {
            log.error("更新事件提醒发送状态失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<GeoFenceEvent> getRecentEvents(Integer limit) {
        return geoFenceEventMapper.getRecentEvents(limit);
    }

    @Override
    public Long countEventsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return geoFenceEventMapper.countEventsByTimeRange(startTime, endTime);
    }

    @Override
    public Long countEventsByTypeAndTimeRange(String eventType, LocalDateTime startTime, LocalDateTime endTime) {
        return geoFenceEventMapper.countEventsByTypeAndTimeRange(eventType, startTime, endTime);
    }

    @Override
    @Transactional
    public void sendFenceEventAlert(GeoFenceEvent geoFenceEvent) {
        try {
            // 获取围栏信息
            GeoFence geoFence = geoFenceMapper.getFenceById(geoFenceEvent.getFenceId());
            if (geoFence == null) {
                log.warn("围栏不存在，无法发送提醒: fenceId={}", geoFenceEvent.getFenceId());
                return;
            }
            
            String alertType = geoFence.getAlertType();
            String emergencyContacts = geoFence.getEmergencyContacts();
            
            if (emergencyContacts == null || emergencyContacts.trim().isEmpty()) {
                log.warn("未配置紧急联系人，无法发送提醒: fenceId={}", geoFenceEvent.getFenceId());
                return;
            }
            
            // 发送短信提醒
            if ("sms".equals(alertType) || "both".equals(alertType)) {
                sendSmsAlert(emergencyContacts, geoFenceEvent.getAlertContent());
            }
            
            // 发送应用推送（暂时跳过，后续可扩展）
            if ("app".equals(alertType) || "both".equals(alertType)) {
                // TODO: 实现应用推送功能
                log.info("应用推送功能待实现");
            }
            
            // 更新发送状态
            updateAlertSentStatus(geoFenceEvent.getId());
            
            log.info("围栏事件提醒发送成功: eventId={}, alertType={}", 
                    geoFenceEvent.getId(), alertType);
            
        } catch (Exception e) {
            log.error("发送围栏事件提醒失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public int processUnsentAlerts() {
        try {
            List<GeoFenceEvent> unsentEvents = getUnsentAlertEvents();
            int processedCount = unsentEvents.size();
            log.info("开始处理未发送的围栏事件提醒，数量: {}", processedCount);
            
            for (GeoFenceEvent event : unsentEvents) {
                sendFenceEventAlert(event);
            }
            
            log.info("未发送的围栏事件提醒处理完成");
            return processedCount;
        } catch (Exception e) {
            log.error("处理未发送的围栏事件提醒失败: {}", e.getMessage(), e);
            return 0;
        }
    }
    
    /**
     * 发送短信提醒
     */
    private void sendSmsAlert(String emergencyContacts, String alertContent) {
        try {
            String[] phones = emergencyContacts.split(",");
            for (String phone : phones) {
                phone = phone.trim();
                if (!phone.isEmpty()) {
                    boolean sent = smsService.sendSms(phone, alertContent);
                    if (sent) {
                        log.info("围栏事件短信提醒发送成功: phone={}", phone);
                    } else {
                        log.warn("围栏事件短信提醒发送失败: phone={}", phone);
                    }
                }
            }
        } catch (Exception e) {
            log.error("发送围栏事件短信提醒失败: {}", e.getMessage(), e);
        }
    }
}