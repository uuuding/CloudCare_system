package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 电子围栏实体类
 *
 * @author CloudCare
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("geo_fence")
public class GeoFence implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 围栏ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 围栏名称
     */
    @TableField("fence_name")
    private String fenceName;

    /**
     * 老人ID
     */
    @TableField("elderly_id")
    private Long elderlyId;

    /**
     * 围栏类型：1-圆形，2-多边形
     */
    @TableField("fence_type")
    private Integer fenceType;

    /**
     * 中心点纬度（圆形围栏）
     */
    @TableField("center_lat")
    private Double centerLat;

    /**
     * 中心点经度（圆形围栏）
     */
    @TableField("center_lon")
    private Double centerLon;

    /**
     * 半径（米，圆形围栏）
     */
    @TableField("radius")
    private Integer radius;

    /**
     * 多边形坐标点（JSON格式，多边形围栏）
     */
    @TableField("polygon_points")
    private String polygonPoints;

    /**
     * 围栏状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 报警类型：1-进入报警，2-离开报警，3-进入和离开都报警
     */
    @TableField("alert_type")
    private Integer alertType;

    /**
     * 生效时间段（JSON格式）
     */
    @TableField("effective_time")
    private String effectiveTime;

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
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}