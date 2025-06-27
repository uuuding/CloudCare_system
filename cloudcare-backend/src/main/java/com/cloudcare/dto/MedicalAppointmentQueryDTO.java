package com.cloudcare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 医疗预约查询DTO
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Data
public class MedicalAppointmentQueryDTO {

    /**
     * 老人姓名（模糊查询）
     */
    private String elderName;

    /**
     * 医生姓名（模糊查询）
     */
    private String doctorName;

    /**
     * 预约类型（1-体检 2-门诊 3-专科 4-急诊）
     */
    private Integer appointmentType;

    /**
     * 预约状态（1-待确认 2-已确认 3-已完成 4-已取消）
     */
    private Integer status;

    /**
     * 预约开始时间
     */
    private OffsetDateTime startTime;

    /**
     * 预约结束时间
     */
    private OffsetDateTime endTime;

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;
}