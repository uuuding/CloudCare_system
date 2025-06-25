package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 健康记录实体类
 *
 * @author cloudcare
 */
@Data
@TableName("health_record")
public class HealthRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 记录类型（1：体温，2：血压，3：血糖，4：心率，5：血氧，6：体重，7：其他）
     */
    private Integer recordType;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;

    /**
     * 体温值（℃）
     */
    private Float temperature;

    /**
     * 收缩压（mmHg）
     */
    private Integer systolicPressure;

    /**
     * 舒张压（mmHg）
     */
    private Integer diastolicPressure;

    /**
     * 血糖值（mmol/L）
     */
    private Float bloodSugar;

    /**
     * 心率（次/分）
     */
    private Integer heartRate;

    /**
     * 血氧饱和度（%）
     */
    private Integer bloodOxygen;

    /**
     * 体重（kg）
     */
    private Float weight;

    /**
     * 其他指标值
     */
    private String otherValue;

    /**
     * 测量设备ID
     */
    private Long deviceId;

    /**
     * 记录来源（1：设备自动记录，2：人工录入）
     */
    private Integer recordSource;

    /**
     * 是否异常（0：正常，1：异常）
     */
    private Integer isAbnormal;

    /**
     * 异常描述
     */
    private String abnormalDesc;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

}