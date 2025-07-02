package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 电子围栏事件记录实体类
 */
@Data
@TableName("geo_fence_event")
public class GeoFenceEvent {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 老人ID
     */
    private Integer elderlyId;
    
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
     * 创建时间
     */
    private LocalDateTime createTime;
}