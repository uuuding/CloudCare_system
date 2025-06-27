package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 医疗预约实体类
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("medical_appointment")
public class MedicalAppointment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @TableId(value = "appointment_id", type = IdType.AUTO)
    private Long appointmentId;

    /**
     * 老人ID
     */
    @TableField("elder_id")
    private Long elderId;

    /**
     * 医生ID
     */
    @TableField("doctor_id")
    private Long doctorId;

    /**
     * 预约时间
     */
    @TableField("appointment_time")
    private OffsetDateTime appointmentTime;

    /**
     * 预约类型（1-体检 2-门诊 3-专科 4-急诊）
     */
    @TableField("appointment_type")
    private Integer appointmentType;

    /**
     * 预约状态（1-待确认 2-已确认 3-已完成 4-已取消）
     */
    @TableField("status")
    private Integer status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // 非数据库字段，用于前端显示
    @TableField(exist = false)
    private String elderName;

    @TableField(exist = false)
    private String doctorName;

    @TableField(exist = false)
    private String appointmentTypeText;

    @TableField(exist = false)
    private String statusText;
}