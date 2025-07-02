package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GPS位置记录实体类
 *
 * @author CloudCare
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gps_location")
public class GpsLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备IMEI号
     */
    @TableField("macid")
    private String macid;

    /**
     * 老人ID
     */
    @TableField("elderly_id")
    private Long elderlyId;

    /**
     * GPS时间（UTC毫秒时间戳）
     */
    @TableField("gps_time")
    private Long gpsTime;

    /**
     * 心跳时间（UTC毫秒时间戳）
     */
    @TableField("heart_time")
    private Long heartTime;

    /**
     * 更新时间（UTC毫秒时间戳）
     */
    @TableField("upd_time")
    private Long updTime;

    /**
     * 纬度
     */
    @TableField("lat")
    private Double lat;

    /**
     * 经度
     */
    @TableField("lon")
    private Double lon;

    /**
     * 地图纬度（百度坐标系）
     */
    @TableField("map_lat")
    private Double mapLat;

    /**
     * 地图经度（百度坐标系）
     */
    @TableField("map_lon")
    private Double mapLon;

    /**
     * 速度
     */
    @TableField("speed")
    private Double speed;

    /**
     * 方向
     */
    @TableField("direction")
    private Double direction;

    /**
     * 状态信息（逗号分隔）
     */
    @TableField("stats")
    private String stats;

    /**
     * 数值状态（逗号分隔）
     */
    @TableField("value")
    private String value;

    /**
     * 定位类型：0-GPS，16-LBS基站
     */
    @TableField("signal_type")
    private Integer signalType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}