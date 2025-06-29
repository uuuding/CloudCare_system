package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 干预方案实体类
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("intervention_plan")
public class InterventionPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 干预方案ID
     */
    @TableId(value = "plan_id", type = IdType.AUTO)
    private Long planId;

    /**
     * 老人ID
     */
    @TableField("elderly_id")
    private Integer elderlyId;

    /**
     * 老人姓名
     */
    @TableField("elderly_name")
    private String elderlyName;

    /**
     * 关联的健康预警ID
     */
    @TableField("alert_id")
    private Long alertId;

    /**
     * 干预方案标题
     */
    @TableField("plan_title")
    private String planTitle;

    /**
     * 干预类型：MEDICATION(用药), EXERCISE(运动), DIET(饮食), LIFESTYLE(生活方式), MONITORING(监测), MEDICAL(医疗)
     */
    @TableField("plan_type")
    private String planType;

    /**
     * 优先级：LOW(低), MEDIUM(中), HIGH(高), URGENT(紧急)
     */
    @TableField("priority_level")
    private String priorityLevel;

    /**
     * 目标指标（JSON格式）
     */
    @TableField("target_indicators")
    private String targetIndicators;

    /**
     * 干预内容详情
     */
    @TableField("intervention_content")
    private String interventionContent;

    /**
     * 实施方法
     */
    @TableField("implementation_method")
    private String implementationMethod;

    /**
     * 执行频率
     */
    @TableField("frequency")
    @JsonProperty("executionFrequency")
    private String frequency;

    /**
     * 持续天数
     */
    @TableField("duration_days")
    private Integer durationDays;

    /**
     * 负责人
     */
    @TableField("responsible_person")
    private String responsiblePerson;

    /**
     * 联系方式
     */
    @TableField("contact_info")
    private String contactInfo;

    /**
     * 开始日期
     */
    @TableField("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 状态：PENDING(待执行), ACTIVE(执行中), COMPLETED(已完成), PAUSED(暂停), CANCELLED(已取消)
     */
    @TableField("status")
    private String status;

    /**
     * 完成进度百分比
     */
    @TableField("progress_rate")
    @JsonProperty("progress")
    private BigDecimal progressRate;

    /**
     * 效果评分(1-10)
     */
    @TableField("effectiveness_score")
    private BigDecimal effectivenessScore;

    /**
     * 备注
     */
    @TableField("notes")
    private String notes;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    // 干预类型枚举
    public enum PlanType {
        MEDICATION("用药"),
        EXERCISE("运动"),
        DIET("饮食"),
        LIFESTYLE("生活方式"),
        MONITORING("监测"),
        MEDICAL("医疗");

        private final String description;

        PlanType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 优先级枚举
    public enum PriorityLevel {
        LOW("低"),
        MEDIUM("中"),
        HIGH("高"),
        URGENT("紧急");

        private final String description;

        PriorityLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 状态枚举
    public enum Status {
        PENDING("待执行"),
        ACTIVE("执行中"),
        COMPLETED("已完成"),
        PAUSED("暂停"),
        CANCELLED("已取消");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}