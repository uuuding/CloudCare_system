package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 服务调度实体类
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_schedule")
public class ServiceSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调度ID
     */
    @TableId(value = "schedule_id", type = IdType.AUTO)
    private Long scheduleId;

    /**
     * 服务名称
     */
    @TableField("service_name")
    private String serviceName;

    /**
     * 服务类型（1：日常护理，2：医疗服务，3：康复训练，4：心理疏导，5：营养配餐，6：清洁卫生）
     */
    @TableField("service_type")
    private Integer serviceType;

    /**
     * 老人ID
     */
    @TableField("elder_id")
    private Long elderId;

    /**
     * 服务人员ID
     */
    @TableField("staff_id")
    private Long staffId;

    /**
     * 计划开始时间
     */
    @TableField("scheduled_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledStartTime;

    /**
     * 计划结束时间
     */
    @TableField("scheduled_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledEndTime;

    /**
     * 实际开始时间
     */
    @TableField("actual_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    @TableField("actual_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndTime;

    /**
     * 服务状态（1：待执行，2：进行中，3：已完成，4：已取消）
     */
    @TableField("status")
    private Integer status;

    /**
     * 优先级（1：低，2：中，3：高，4：紧急）
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 服务描述
     */
    @TableField("description")
    private String description;

    /**
     * 服务备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}