package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 干预执行记录实体类
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("intervention_execution")
public class InterventionExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 执行记录ID
     */
    @TableId(value = "execution_id", type = IdType.AUTO)
    private Long executionId;

    /**
     * 干预方案ID
     */
    @TableField("plan_id")
    private Long planId;

    /**
     * 执行日期
     */
    @TableField("execution_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate executionDate;

    /**
     * 执行时间
     */
    @TableField("execution_time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime executionTime;

    /**
     * 执行状态：COMPLETED(已完成), PARTIAL(部分完成), MISSED(未执行)
     */
    @TableField("execution_status")
    private String executionStatus;

    /**
     * 完成率
     */
    @TableField("completion_rate")
    private BigDecimal completionRate;

    /**
     * 执行详情
     */
    @TableField("execution_details")
    private String executionDetails;

    /**
     * 副作用或不良反应
     */
    @TableField("side_effects")
    private String sideEffects;

    /**
     * 患者反馈
     */
    @TableField("patient_feedback")
    private String patientFeedback;

    /**
     * 执行人姓名
     */
    @TableField("executor_name")
    private String executorName;

    /**
     * 执行地点
     */
    @TableField("execution_location")
    private String executionLocation;

    /**
     * 执行前生命体征（JSON格式）
     */
    @TableField("vital_signs_before")
    private String vitalSignsBefore;

    /**
     * 执行后生命体征（JSON格式）
     */
    @TableField("vital_signs_after")
    private String vitalSignsAfter;

    /**
     * 效果评价(1-5)
     */
    @TableField("effectiveness_rating")
    private Integer effectivenessRating;

    /**
     * 备注
     */
    @TableField("notes")
    private String notes;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    // 执行状态枚举
    public enum ExecutionStatus {
        COMPLETED("已完成"),
        PARTIAL("部分完成"),
        MISSED("未执行");

        private final String description;

        ExecutionStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}