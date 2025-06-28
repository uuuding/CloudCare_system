package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备信息实体类
 *
 * @author cloudcare
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_info")
@Schema(description = "设备信息")
public class DeviceInfo {

    @TableId(value = "device_id", type = IdType.AUTO)
    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "设备类型（1：体温计，2：血压计，3：血糖仪，4：心率监测仪，5：血氧仪，6：体重秤，7：其他）")
    private Integer deviceType;

    @Schema(description = "设备型号")
    private String deviceModel;

    @Schema(description = "设备厂商")
    private String manufacturer;

    @Schema(description = "购买日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    @Schema(description = "保修期限（月）")
    private Integer warrantyPeriod;

    @Schema(description = "设备状态（0：停用，1：正常，2：维修中，3：已报废）")
    private Integer deviceStatus;

    @Schema(description = "使用位置")
    private String location;

    @Schema(description = "负责人ID")
    private Long managerId;

    @Schema(description = "负责人姓名")
    private String managerName;

    @Schema(description = "最后检修时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMaintenanceTime;

    @Schema(description = "下次检修时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextMaintenanceTime;

    @Schema(description = "设备IP地址")
    private String ipAddress;

    @Schema(description = "MAC地址")
    private String macAddress;

    @Schema(description = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "删除标志（0：未删除，1：已删除）")
    @TableLogic
    private Integer deleted;

    /**
     * 获取设备类型名称
     */
    public String getDeviceTypeName() {
        if (deviceType == null) {
            return "未知";
        }
        switch (deviceType) {
            case 1: return "体温计";
            case 2: return "血压计";
            case 3: return "血糖仪";
            case 4: return "心率监测仪";
            case 5: return "血氧仪";
            case 6: return "体重秤";
            case 7: return "其他";
            default: return "未知";
        }
    }

    /**
     * 获取设备状态名称
     */
    public String getDeviceStatusName() {
        if (deviceStatus == null) {
            return "未知";
        }
        switch (deviceStatus) {
            case 0: return "停用";
            case 1: return "正常";
            case 2: return "维修中";
            case 3: return "已报废";
            default: return "未知";
        }
    }
}