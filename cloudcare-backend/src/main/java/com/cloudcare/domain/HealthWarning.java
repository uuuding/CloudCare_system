package com.cloudcare.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloudcare.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 健康预警对象 health_warning
 *
 * @author CloudCare
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_warning")
public class HealthWarning extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 预警ID */
    @TableId(value = "warning_id", type = IdType.AUTO)
    private Long warningId;

    /** 老人ID */
    private Long elderId;

    /** 健康记录ID */
    private Long recordId;

    /** 预警类型（1：体温异常，2：血压异常，3：血糖异常，4：心率异常，5：血氧异常，6：体重异常，7：其他） */
    private Integer warningType;

    /** 预警级别（1：轻度，2：中度，3：重度） */
    private Integer warningLevel;

    /** 预警时间 */
    private LocalDateTime warningTime;

    /** 预警内容 */
    private String warningContent;

    /** 处理状态（0：未处理，1：已处理） */
    private Integer processStatus;

    /** 处理时间 */
    private LocalDateTime processTime;

    /** 处理人ID */
    private Long processUserId;

    /** 处理结果 */
    private String processResult;
}