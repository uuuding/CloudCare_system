package com.cloudcare.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloudcare.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 健康记录对象 health_record
 *
 * @author CloudCare
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_record")
public class HealthRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    /** 老人ID */
    private Long elderlyId;

    /** 记录类型（1体温 2血压 3血糖 4心率 5血氧 6体重 7其他） */
    private Integer recordType;

    /** 记录值 */
    private String recordValue;

    /** 记录等级（0正常 1轻度异常 2中度异常 3重度异常） */
    private Integer recordLevel;

    /** 记录时间 */
    private LocalDateTime recordTime;

    /** 记录人ID */
    private Long recordBy;

    /** 备注 */
    private String remark;
}