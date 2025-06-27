package com.cloudcare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * 医疗预约DTO
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Data
public class MedicalAppointmentDTO {

    /**
     * 预约ID（编辑时需要）
     */
    private Long appointmentId;

    /**
     * 老人ID
     */
    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    /**
     * 医生ID
     */
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    /**
     * 预约时间
     */
    @NotNull(message = "预约时间不能为空")
    private OffsetDateTime appointmentTime;

    /**
     * 预约类型（1-体检 2-门诊 3-专科 4-急诊）
     */
    @NotNull(message = "预约类型不能为空")
    private Integer appointmentType;

    /**
     * 预约状态（1-待确认 2-已确认 3-已完成 4-已取消）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}