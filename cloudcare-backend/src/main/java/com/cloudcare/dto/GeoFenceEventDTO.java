package com.cloudcare.dto;

import com.cloudcare.entity.GeoFenceEvent;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 围栏事件DTO，包含老人姓名信息
 * 用于前端展示围栏事件列表，包含事件详情和关联的老人信息
 * 提供事件状态管理和提醒发送状态跟踪功能
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class GeoFenceEventDTO {
    
    private Long id;
    
    /**
     * 老人ID
     * 触发围栏事件的老人标识
     */
    private Integer elderlyId;
    
    /**
     * 老人姓名
     * 用于前端展示，避免额外查询老人信息
     */
    private String elderlyName;
    
    /**
     * 围栏ID
     */
    private Long fenceId;
    
    /**
     * 围栏名称
     */
    private String fenceName;
    
    /**
     * 事件类型：enter-进入，exit-离开
     */
    private String eventType;
    
    /**
     * 事件发生时的纬度
     */
    private Double lat;
    
    /**
     * 事件发生时的经度
     */
    private Double lon;
    
    /**
     * GPS定位记录ID
     */
    private Long locationId;
    
    /**
     * 设备编号（IMEI）
     */
    private String macid;
    
    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;
    
    /**
     * 是否已发送提醒：1-已发送，0-未发送
     */
    private Integer alertSent;
    
    /**
     * 提醒发送时间
     */
    private LocalDateTime alertSentTime;
    
    /**
     * 提醒方式
     */
    private String alertType;
    
    /**
     * 提醒内容
     */
    private String alertContent;
    
    /**
     * 是否已读：1-已读，0-未读
     */
    private Integer isRead;
    
    /**
     * 已读时间
     */
    private LocalDateTime readTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 从GeoFenceEvent转换为DTO
     */
    public static GeoFenceEventDTO fromEntity(GeoFenceEvent event, String elderlyName) {
        GeoFenceEventDTO dto = new GeoFenceEventDTO();
        dto.setId(event.getId());
        dto.setElderlyId(event.getElderlyId());
        dto.setElderlyName(elderlyName);
        dto.setFenceId(event.getFenceId());
        dto.setFenceName(event.getFenceName());
        dto.setEventType(event.getEventType());
        dto.setLat(event.getLat());
        dto.setLon(event.getLon());
        dto.setLocationId(event.getLocationId());
        dto.setMacid(event.getMacid());
        dto.setEventTime(event.getEventTime());
        dto.setAlertSent(event.getAlertSent());
        dto.setAlertSentTime(event.getAlertSentTime());
        dto.setAlertType(event.getAlertType());
        dto.setAlertContent(event.getAlertContent());
        dto.setIsRead(event.getIsRead());
        dto.setReadTime(event.getReadTime());
        dto.setCreateTime(event.getCreateTime());
        return dto;
    }
}