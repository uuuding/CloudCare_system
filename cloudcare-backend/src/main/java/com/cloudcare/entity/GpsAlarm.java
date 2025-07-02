package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GPS报警记录实体类
 *
 * @author CloudCare
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gps_alarm")
public class GpsAlarm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报警ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报警编号（来自GPS系统）
     */
    @TableField("alarm_id")
    private String alarmId;

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
     * 设备名称
     */
    @TableField("device_name")
    private String deviceName;

    /**
     * 报警时间（UTC毫秒时间戳）
     */
    @TableField("alarm_time")
    private Long alarmTime;

    /**
     * 添加时间（UTC毫秒时间戳）
     */
    @TableField("add_time")
    private Long addTime;

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
     * 地图纬度
     */
    @TableField("map_lat")
    private Double mapLat;

    /**
     * 地图经度
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
    private Integer direction;

    /**
     * 报警类型
     */
    @TableField("classify")
    private Integer classify;

    /**
     * 报警描述
     */
    @TableField("describe_info")
    private String describeInfo;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 处理状态：0-未处理，1-已处理
     */
    @TableField("handle_status")
    private Integer handleStatus;

    /**
     * 处理人
     */
    @TableField("handle_by")
    private String handleBy;

    /**
     * 处理时间
     */
    @TableField("handle_time")
    private LocalDateTime handleTime;

    /**
     * 处理备注
     */
    @TableField("handle_remark")
    private String handleRemark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}