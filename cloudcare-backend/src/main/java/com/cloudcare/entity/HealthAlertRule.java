package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 健康预警规则实体类
 */
@Data
@TableName("health_alert_rule")
public class HealthAlertRule {
    
    @TableId(type = IdType.AUTO)
    private Long ruleId;
    
    /**
     * 规则名称
     */
    private String ruleName;
    
    /**
     * 预警类型：TEMPERATURE, BLOOD_PRESSURE, HEART_RATE, BMI, SLEEP
     */
    private String alertType;
    
    /**
     * 预警级别：LOW, MEDIUM, HIGH, CRITICAL
     */
    private String alertLevel;
    
    /**
     * 最小阈值
     */
    private Double minThreshold;
    
    /**
     * 最大阈值
     */
    private Double maxThreshold;
    
    /**
     * 预警消息模板
     */
    private String messageTemplate;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 适用年龄段最小值
     */
    private Integer minAge;
    
    /**
     * 适用年龄段最大值
     */
    private Integer maxAge;
    
    /**
     * 适用性别：M(男), F(女), ALL(全部)
     */
    private String gender;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 备注
     */
    private String remark;
}