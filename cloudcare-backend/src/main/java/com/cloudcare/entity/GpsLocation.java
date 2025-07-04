package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * GPS定位数据实体类
 * 存储GPS设备上报的定位数据，包括原始GPS坐标和转换后的地图坐标
 * 支持多种坐标系统转换，提供位置信息的完整记录
 * 用于轨迹回放、围栏检测、位置查询等功能
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@TableName("gps_location")
public class GpsLocation {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 设备编号（IMEI）
     */
    private String macid;
    
    /**
     * 老人ID
     */
    private Integer elderlyId;
    
    /**
     * GPS时间戳（毫秒）
     */
    private Long gpsTime;
    
    /**
     * 信号时间
     */
    private Long heartTime;
    
    /**
     * 最后更新位置时间
     */
    private Long updTime;
    
    /**
     * GPS纬度
     */
    private Double lat;
    
    /**
     * GPS经度
     */
    private Double lon;
    
    /**
     * 地图纬度
     */
    private Double mapLat;
    
    /**
     * 地图经度
     */
    private Double mapLon;
    
    /**
     * 速度（单位：km/h）
     */
    private Double speed;
    
    /**
     * 方向角度
     */
    private Double dir;
    
    /**
     * 状态字段，逗号分隔
     */
    private String stats;
    
    /**
     * 状态值字段，逗号分隔
     */
    private String value;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}