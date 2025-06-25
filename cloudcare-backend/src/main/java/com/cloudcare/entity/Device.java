package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备信息实体类
 *
 * @author cloudcare
 */
@Data
@TableName("device_info")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @TableId(value = "device_id", type = IdType.AUTO)
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型（1：体温计，2：血压计，3：血糖仪，4：心率监测仪，5：血氧仪，6：体重秤，7：其他）
     */
    private Integer deviceType;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 设备厂商
     */
    private String manufacturer;

    /**
     * 购买日期
     */
    private LocalDateTime purchaseDate;

    /**
     * 保修期限（月）
     */
    private Integer warrantyPeriod;

    /**
     * 设备状态（0：停用，1：正常，2：维修中，3：已报废）
     */
    private Integer deviceStatus;

    /**
     * 使用位置
     */
    private String location;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 最后检修时间
     */
    private LocalDateTime lastMaintenanceTime;

    /**
     * 下次检修时间
     */
    private LocalDateTime nextMaintenanceTime;

    /**
     * 设备IP地址
     */
    private String ipAddress;

    /**
     * MAC地址
     */
    private String macAddress;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0：未删除，1：已删除）
     */
    private Integer deleted;

}