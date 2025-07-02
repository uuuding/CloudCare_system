package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GPS设备实体类
 *
 * @author CloudCare
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gps_device")
public class GpsDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备IMEI号（唯一标识）
     */
    @TableField("macid")
    private String macid;

    /**
     * 设备名称
     */
    @TableField("device_name")
    private String deviceName;

    /**
     * 绑定的老人ID
     */
    @TableField("elderly_id")
    private Long elderlyId;

    /**
     * 设备状态：0-离线，1-在线
     */
    @TableField("status")
    private Integer status;

    /**
     * 最后一次GPS时间
     */
    @TableField("last_gps_time")
    private LocalDateTime lastGpsTime;

    /**
     * 最后一次心跳时间
     */
    @TableField("last_heart_time")
    private LocalDateTime lastHeartTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}