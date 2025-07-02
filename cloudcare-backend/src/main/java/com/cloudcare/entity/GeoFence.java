package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 电子围栏实体类
 */
@Data
@TableName("geo_fence")
public class GeoFence {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 老人ID
     */
    private Integer elderlyId;
    
    /**
     * 围栏名称
     */
    private String fenceName;
    
    /**
     * 围栏类型：circle-圆形，polygon-多边形
     */
    private String fenceType;
    
    /**
     * 围栏中心点纬度（圆形围栏使用）
     */
    private Double centerLat;
    
    /**
     * 围栏中心点经度（圆形围栏使用）
     */
    private Double centerLon;
    
    /**
     * 围栏半径（米，圆形围栏使用）
     */
    private Double radius;
    
    /**
     * 围栏坐标点（JSON格式，多边形围栏使用）
     * 格式：[{"lat":22.123,"lon":114.123},{"lat":22.124,"lon":114.124},...]
     */
    private String coordinates;
    
    /**
     * 围栏状态：1-启用，0-禁用
     */
    private Integer status;
    
    /**
     * 是否启用进入提醒：1-启用，0-禁用
     */
    private Integer enterAlert;
    
    /**
     * 是否启用离开提醒：1-启用，0-禁用
     */
    private Integer exitAlert;
    
    /**
     * 提醒方式：sms-短信，app-应用推送，both-两者都有
     */
    private String alertType;
    
    /**
     * 紧急联系人手机号（多个用逗号分隔）
     */
    private String emergencyContacts;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建者
     */
    private String createBy;
    
    /**
     * 更新者
     */
    private String updateBy;
}