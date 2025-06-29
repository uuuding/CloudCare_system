package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 干预模板实体类
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("intervention_template")
public class InterventionTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @TableId(value = "template_id", type = IdType.AUTO)
    private Long templateId;

    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 模板分类
     */
    @TableField("template_category")
    private String templateCategory;

    /**
     * 适用的预警类型
     */
    @TableField("alert_type")
    private String alertType;

    /**
     * 建议优先级
     */
    @TableField("priority_level")
    private String priorityLevel;

    /**
     * 模板内容
     */
    @TableField("template_content")
    private String templateContent;

    /**
     * 实施指南
     */
    @TableField("implementation_guide")
    private String implementationGuide;

    /**
     * 注意事项
     */
    @TableField("precautions")
    private String precautions;

    /**
     * 预期效果
     */
    @TableField("expected_outcomes")
    private String expectedOutcomes;

    /**
     * 是否启用
     */
    @TableField("is_active")
    private Boolean isActive;

    /**
     * 使用次数
     */
    @TableField("usage_count")
    private Integer usageCount;

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
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}