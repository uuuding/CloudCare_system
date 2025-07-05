package com.cloudcare.service.impl;

import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GeoFenceEvent;
import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.service.ElderlyProfileService;
import com.cloudcare.mapper.GeoFenceEventMapper;
import com.cloudcare.mapper.GeoFenceMapper;
import com.cloudcare.service.GeoFenceEventService;
import com.cloudcare.service.SmsService;
import com.cloudcare.config.SystemConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 电子围栏事件服务实现类
 * 处理围栏事件的记录、查询、统计和提醒发送功能
 * 支持多种提醒方式（短信、邮件、推送），提供事件状态管理
 * 自动处理未发送提醒的重试机制，确保重要事件及时通知
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeoFenceEventServiceImpl implements GeoFenceEventService {

    private final GeoFenceEventMapper geoFenceEventMapper;
    private final GeoFenceMapper geoFenceMapper;
    private final SmsService smsService;
    private final ElderlyProfileService elderlyProfileService;
    private final SystemConfig systemConfig;

    @Override
    @Transactional
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
    @Transactional
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
    public void sendFenceEventAlert(GeoFenceEvent geoFenceEvent) {
        try {
            // 获取围栏信息
            GeoFence geoFence = geoFenceMapper.getFenceById(geoFenceEvent.getFenceId());
            if (geoFence == null) {
                log.warn("围栏不存在，无法发送提醒: fenceId={}", geoFenceEvent.getFenceId());
                // 围栏不存在，标记为已发送避免重复处理
                updateAlertSentStatus(geoFenceEvent.getId());
                return;
            }
            
            String alertType = geoFence.getAlertType();
            String emergencyContacts = geoFence.getEmergencyContacts();
            
            if (emergencyContacts == null || emergencyContacts.trim().isEmpty()) {
                log.warn("未配置紧急联系人，无法发送提醒: fenceId={}", geoFenceEvent.getFenceId());
                // 未配置联系人，标记为已发送避免重复处理
                updateAlertSentStatus(geoFenceEvent.getId());
                return;
            }
            
            boolean smsSuccess = true;
            
            // 发送短信提醒
            if ("sms".equals(alertType) || "both".equals(alertType)) {
                try {
                    // 如果是离开围栏事件，使用离开模板发送
                    if ("exit".equals(geoFenceEvent.getEventType())) {
                        sendFenceLeaveAlertWithTemplate(emergencyContacts, geoFenceEvent, geoFence);
                    } else if ("enter".equals(geoFenceEvent.getEventType())) {
                        // 如果是进入围栏事件，使用进入模板发送
                        sendFenceEnterAlertWithTemplate(emergencyContacts, geoFenceEvent, geoFence);
                    } else {
                        // 其他事件类型使用原有方式
                        sendSmsAlert(emergencyContacts, geoFenceEvent.getAlertContent());
                    }
                } catch (Exception smsException) {
                    log.error("发送短信失败: eventId={}, error={}", geoFenceEvent.getId(), smsException.getMessage());
                    smsSuccess = false;
                }
            }
            
            // 发送应用推送（暂时跳过，后续可扩展）
            if ("app".equals(alertType) || "both".equals(alertType)) {
                // TODO: 实现应用推送功能
                log.info("应用推送功能待实现");
            }
            
            // 只有在短信发送成功时才更新发送状态
            if (smsSuccess) {
                updateAlertSentStatus(geoFenceEvent.getId());
                log.info("围栏事件提醒发送成功: eventId={}, alertType={}", 
                        geoFenceEvent.getId(), alertType);
            } else {
                log.warn("围栏事件提醒发送失败，将在下次定时任务中重试: eventId={}", geoFenceEvent.getId());
            }
            
        } catch (Exception e) {
            log.error("发送围栏事件提醒失败: eventId={}, error={}", geoFenceEvent.getId(), e.getMessage(), e);
            // 发生异常时不更新发送状态，让定时任务重试
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
    
    @Override
    public List<GeoFenceEvent> getUnreadEvents(Integer limit) {
        return geoFenceEventMapper.getUnreadEvents(limit);
    }
    
    @Override
    public boolean markEventAsRead(Long eventId) {
        try {
            return geoFenceEventMapper.markEventAsRead(eventId) > 0;
        } catch (Exception e) {
            log.error("标记事件为已读失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public List<com.cloudcare.dto.GeoFenceEventDTO> getUnreadEventsWithElderlyName(Integer limit) {
        try {
            List<GeoFenceEvent> events = geoFenceEventMapper.getUnreadEvents(limit);
            return convertToDTO(events);
        } catch (Exception e) {
            log.error("查询未读事件失败: {}", e.getMessage(), e);
            return new java.util.ArrayList<>();
        }
    }
    
    @Override
    public List<com.cloudcare.dto.GeoFenceEventDTO> getRecentEventsWithElderlyName(Integer limit) {
        try {
            List<GeoFenceEvent> events = geoFenceEventMapper.getRecentEvents(limit);
            return convertToDTO(events);
        } catch (Exception e) {
            log.error("查询最近事件失败: {}", e.getMessage(), e);
            return new java.util.ArrayList<>();
        }
    }
    
    @Override
    public Map<String, Object> getAllEventsWithPagination(Integer page, Integer size, Integer elderlyId, 
                                                          String eventType, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            // 计算偏移量
            int offset = (page - 1) * size;
            
            List<GeoFenceEvent> events;
            Long total;
            
            // 根据筛选条件选择不同的查询方法
            if (elderlyId != null && eventType != null && startTime != null && endTime != null) {
                // 多条件筛选
                events = geoFenceEventMapper.getEventsByMultipleConditionsWithPagination(elderlyId, eventType, startTime, endTime, offset, size);
                total = geoFenceEventMapper.countEventsByMultipleConditions(elderlyId, eventType, startTime, endTime);
            } else if (elderlyId != null && startTime != null && endTime != null) {
                // 按老人ID和时间范围筛选
                events = geoFenceEventMapper.getEventsByElderlyIdAndTimeRangeWithPagination(elderlyId, startTime, endTime, offset, size);
                total = geoFenceEventMapper.countEventsByElderlyIdAndTimeRange(elderlyId, startTime, endTime);
            } else if (elderlyId != null && eventType != null) {
                // 按老人ID和事件类型筛选
                events = geoFenceEventMapper.getEventsByElderlyIdAndTypeWithPagination(elderlyId, eventType, offset, size);
                total = geoFenceEventMapper.countEventsByElderlyIdAndType(elderlyId, eventType);
            } else if (eventType != null && startTime != null && endTime != null) {
                // 按事件类型和时间范围筛选
                events = geoFenceEventMapper.getEventsByTypeAndTimeRangeWithPagination(eventType, startTime, endTime, offset, size);
                total = geoFenceEventMapper.countEventsByTypeAndTimeRange(eventType, startTime, endTime);
            } else if (elderlyId != null) {
                // 按老人ID筛选
                events = geoFenceEventMapper.getEventsByElderlyIdWithPagination(elderlyId, offset, size);
                total = geoFenceEventMapper.countEventsByElderlyId(elderlyId);
            } else if (eventType != null) {
                // 按事件类型筛选
                events = geoFenceEventMapper.getEventsByTypeWithPagination(eventType, offset, size);
                total = geoFenceEventMapper.countEventsByType(eventType);
            } else if (startTime != null && endTime != null) {
                // 按时间范围筛选
                events = geoFenceEventMapper.getEventsByTimeRangeWithPagination(startTime, endTime, offset, size);
                total = geoFenceEventMapper.countEventsByTimeRange2(startTime, endTime);
            } else {
                // 无筛选条件，查询所有
                events = geoFenceEventMapper.getAllEventsWithPagination(offset, size);
                total = geoFenceEventMapper.countAllEvents();
            }
            
            // 转换为DTO
            List<com.cloudcare.dto.GeoFenceEventDTO> eventDTOs = convertToDTO(events);
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", eventDTOs);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("pages", (total + size - 1) / size); // 总页数
            
            return result;
        } catch (Exception e) {
            log.error("分页查询所有围栏事件失败: {}", e.getMessage(), e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("list", new java.util.ArrayList<>());
            errorResult.put("total", 0L);
            errorResult.put("page", page);
            errorResult.put("size", size);
            errorResult.put("pages", 0L);
            return errorResult;
        }
    }
    
    /**
     * 将GeoFenceEvent列表转换为DTO列表
     */
    private List<com.cloudcare.dto.GeoFenceEventDTO> convertToDTO(List<GeoFenceEvent> events) {
        List<com.cloudcare.dto.GeoFenceEventDTO> dtoList = new java.util.ArrayList<>();
        for (GeoFenceEvent event : events) {
            String elderlyName = "未知";
            try {
                com.cloudcare.entity.ElderlyProfile elderly = elderlyProfileService.getElderlyProfileById(event.getElderlyId());
                if (elderly != null && elderly.getName() != null) {
                    elderlyName = elderly.getName();
                }
            } catch (Exception e) {
                log.warn("获取老人姓名失败: elderlyId={}, error={}", event.getElderlyId(), e.getMessage());
            }
            dtoList.add(com.cloudcare.dto.GeoFenceEventDTO.fromEntity(event, elderlyName));
        }
        return dtoList;
    }
    
    /**
     * 发送围栏离开提醒（使用模板）
     */
    private void sendFenceLeaveAlertWithTemplate(String emergencyContacts, GeoFenceEvent geoFenceEvent, GeoFence geoFence) {
        try {
            // 获取老人信息
            String elderlyName = "未知";
            try {
                ElderlyProfile elderly = elderlyProfileService.getElderlyProfileById(geoFenceEvent.getElderlyId());
                if (elderly != null && elderly.getName() != null) {
                    elderlyName = elderly.getName();
                }
            } catch (Exception e) {
                log.warn("获取老人姓名失败: elderlyId={}, error={}", geoFenceEvent.getElderlyId(), e.getMessage());
            }
            
            // 收集所有需要发送短信的电话号码
            Set<String> phoneSet = new HashSet<>();
            
            // 添加紧急联系人电话
            if (StringUtils.hasText(emergencyContacts)) {
                String[] phones = emergencyContacts.split(",");
                for (String phone : phones) {
                    phone = phone.trim();
                    if (!phone.isEmpty()) {
                        phoneSet.add(phone);
                    }
                }
            }
            
            // 添加管理员电话
            if (systemConfig.getAdmin() != null && StringUtils.hasText(systemConfig.getAdmin().getPhone())) {
                phoneSet.add(systemConfig.getAdmin().getPhone());
                log.info("添加管理员电话到围栏告警通知列表: {}", systemConfig.getAdmin().getPhone());
            }
            
            // 格式化事件时间
            String eventTime = geoFenceEvent.getEventTime() != null ? 
                geoFenceEvent.getEventTime().toString().replace("T", " ") : "未知";
            
            // 发送模板短信给所有电话号码
            for (String phone : phoneSet) {
                boolean sent = smsService.sendFenceLeaveAlert(
                    phone, 
                    elderlyName, 
                    geoFence.getFenceName(), 
                    String.valueOf(geoFenceEvent.getLat()), 
                    String.valueOf(geoFenceEvent.getLon()), 
                    eventTime
                );
                
                if (sent) {
                    log.info("围栏离开模板短信发送成功: phone={}, elderly={}, fence={}", 
                            phone, elderlyName, geoFence.getFenceName());
                } else {
                    log.warn("围栏离开模板短信发送失败: phone={}, elderly={}, fence={}", 
                            phone, elderlyName, geoFence.getFenceName());
                }
            }
            
            log.info("围栏离开模板短信发送完成，共发送{}条短信", phoneSet.size());
            
        } catch (Exception e) {
            log.error("发送围栏离开模板短信失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送围栏进入提醒（使用模板）
     */
    private void sendFenceEnterAlertWithTemplate(String emergencyContacts, GeoFenceEvent geoFenceEvent, GeoFence geoFence) {
        try {
            // 获取老人信息
            String elderlyName = "未知";
            try {
                ElderlyProfile elderly = elderlyProfileService.getElderlyProfileById(geoFenceEvent.getElderlyId());
                if (elderly != null && elderly.getName() != null) {
                    elderlyName = elderly.getName();
                }
            } catch (Exception e) {
                log.warn("获取老人姓名失败: elderlyId={}, error={}", geoFenceEvent.getElderlyId(), e.getMessage());
            }
            
            // 收集所有需要发送短信的电话号码
            Set<String> phoneSet = new HashSet<>();
            
            // 添加紧急联系人电话
            if (StringUtils.hasText(emergencyContacts)) {
                String[] phones = emergencyContacts.split(",");
                for (String phone : phones) {
                    phone = phone.trim();
                    if (!phone.isEmpty()) {
                        phoneSet.add(phone);
                    }
                }
            }
            
            // 添加管理员电话
            if (systemConfig.getAdmin() != null && StringUtils.hasText(systemConfig.getAdmin().getPhone())) {
                phoneSet.add(systemConfig.getAdmin().getPhone());
                log.info("添加管理员电话到围栏告警通知列表: {}", systemConfig.getAdmin().getPhone());
            }
            
            // 格式化事件时间
            String eventTime = geoFenceEvent.getEventTime() != null ? 
                geoFenceEvent.getEventTime().toString().replace("T", " ") : "未知";
            
            // 发送模板短信给所有电话号码
            for (String phone : phoneSet) {
                boolean sent = smsService.sendFenceEnterAlert(
                    phone, 
                    elderlyName, 
                    geoFence.getFenceName(), 
                    String.valueOf(geoFenceEvent.getLat()), 
                    String.valueOf(geoFenceEvent.getLon()), 
                    eventTime
                );
                
                if (sent) {
                    log.info("围栏进入模板短信发送成功: phone={}, elderly={}, fence={}", 
                            phone, elderlyName, geoFence.getFenceName());
                } else {
                    log.warn("围栏进入模板短信发送失败: phone={}, elderly={}, fence={}", 
                            phone, elderlyName, geoFence.getFenceName());
                }
            }
            
            log.info("围栏进入模板短信发送完成，共发送{}条短信", phoneSet.size());
            
        } catch (Exception e) {
            log.error("发送围栏进入模板短信失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送短信提醒
     * 同时发送给紧急联系人和管理员
     */
    private void sendSmsAlert(String emergencyContacts, String alertContent) {
        try {
            // 收集所有需要发送短信的电话号码
            Set<String> phoneSet = new HashSet<>();
            
            // 添加紧急联系人电话
            if (StringUtils.hasText(emergencyContacts)) {
                String[] phones = emergencyContacts.split(",");
                for (String phone : phones) {
                    phone = phone.trim();
                    if (!phone.isEmpty()) {
                        phoneSet.add(phone);
                    }
                }
            }
            
            // 添加管理员电话
            if (systemConfig.getAdmin() != null && StringUtils.hasText(systemConfig.getAdmin().getPhone())) {
                phoneSet.add(systemConfig.getAdmin().getPhone());
                log.info("添加管理员电话到围栏告警通知列表: {}", systemConfig.getAdmin().getPhone());
            }
            
            // 发送短信给所有电话号码
            for (String phone : phoneSet) {
                // 直接使用预生成的告警内容
                boolean sent = smsService.sendSms(phone, alertContent);
                if (sent) {
                    log.info("围栏事件短信提醒发送成功: phone={}", phone);
                } else {
                    log.warn("围栏事件短信提醒发送失败: phone={}", phone);
                }
            }
            
            log.info("围栏事件短信提醒发送完成，共发送{}条短信", phoneSet.size());
            
        } catch (Exception e) {
            log.error("发送围栏事件短信提醒失败: {}", e.getMessage(), e);
        }
    }
}