package com.cloudcare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 设备信息DTO
 *
 * @author cloudcare
 */
@Data
@Schema(description = "设备信息DTO")
public class DeviceInfoDTO {

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @NotBlank(message = "设备名称不能为空")
    @Schema(description = "设备名称")
    private String deviceName;

    @NotNull(message = "设备类型不能为空")
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

    @Schema(description = "备注")
    private String remark;
}