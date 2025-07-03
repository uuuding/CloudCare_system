package com.cloudcare.service;

import com.cloudcare.entity.GeoFenceEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 电子围栏事件服务接口
 */
public interface GeoFenceEventService {

    /**
     * 保存围栏事件记录
     * @param geoFenceEvent 围栏事件
     * @return 保存结果
     */
    boolean saveGeoFenceEvent(GeoFenceEvent geoFenceEvent);

    /**
     * 根据老人ID查询围栏事件记录
     * @param elderlyId 老人ID
     * @return 事件记录列表
     */
    List<GeoFenceEvent> getEventsByElderlyId(Integer elderlyId);

    /**
     * 根据老人ID和时间范围查询围栏事件记录
     * @param elderlyId 老人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 事件记录列表
     */
    List<GeoFenceEvent> getEventsByElderlyIdAndTimeRange(Integer elderlyId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据围栏ID查询事件记录
     * @param fenceId 围栏ID
     * @return 事件记录列表
     */
    List<GeoFenceEvent> getEventsByFenceId(Long fenceId);

    /**
     * 查询未发送提醒的事件记录
     * @return 事件记录列表
     */
    List<GeoFenceEvent> getUnsentAlertEvents();

    /**
     * 更新事件提醒发送状态
     * @param eventId 事件ID
     * @return 更新结果
     */
    boolean updateAlertSentStatus(Long eventId);

    /**
     * 查询最近的围栏事件（用于仪表盘显示）
     * @param limit 限制数量
     * @return 事件记录列表
     */
    List<GeoFenceEvent> getRecentEvents(Integer limit);

    /**
     * 统计指定时间范围内的事件数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 事件数量
     */
    Long countEventsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据事件类型统计数量
     * @param eventType 事件类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 事件数量
     */
    Long countEventsByTypeAndTimeRange(String eventType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 发送围栏事件提醒
     * @param geoFenceEvent 围栏事件
     */
    void sendFenceEventAlert(GeoFenceEvent geoFenceEvent);

    /**
     * 批量处理未发送的提醒
     * @return 处理的告警数量
     */
    int processUnsentAlerts();

    /**
     * 查询未读的围栏事件
     * @param limit 限制数量
     * @return 未读事件记录列表
     */
    List<GeoFenceEvent> getUnreadEvents(Integer limit);

    /**
     * 标记围栏事件为已读
     * @param eventId 事件ID
     * @return 标记结果
     */
    boolean markEventAsRead(Long eventId);
    
    /**
     * 查询未读的围栏事件（包含老人姓名）
     * @param limit 限制数量
     * @return 未读事件记录列表
     */
    List<com.cloudcare.dto.GeoFenceEventDTO> getUnreadEventsWithElderlyName(Integer limit);
    
    /**
     * 查询最近的围栏事件（包含老人姓名）
     * @param limit 限制数量
     * @return 事件记录列表
     */
    List<com.cloudcare.dto.GeoFenceEventDTO> getRecentEventsWithElderlyName(Integer limit);
    
    /**
     * 分页查询所有围栏事件（支持筛选）
     * @param page 页码
     * @param size 每页大小
     * @param elderlyId 老人ID（可选）
     * @param eventType 事件类型（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 分页结果
     */
    Map<String, Object> getAllEventsWithPagination(Integer page, Integer size, Integer elderlyId, 
                                                   String eventType, LocalDateTime startTime, LocalDateTime endTime);
}