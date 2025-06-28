package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 健康预警记录实体类
 */
@Data
@TableName("health_alert")
public class HealthAlert {
    
    @TableId(type = IdType.AUTO)
    private Long alertId;
    
    /**
     * 老人ID
     */
    private int elderlyId;
    
    /**
     * 老人姓名
     */
    private String elderlyName;
    
    /**
     * 预警类型：TEMPERATURE(体温异常), BLOOD_PRESSURE(血压异常), HEART_RATE(心率异常), BMI(体重异常), SLEEP(睡眠异常)
     */
    private String alertType;
    
    /**
     * 预警级别：LOW(低风险), MEDIUM(中风险), HIGH(高风险), CRITICAL(危急)
     */
    private String alertLevel;
    
    /**
     * 预警标题
     */
    private String alertTitle;
    
    /**
     * 预警描述
     */
    private String alertDescription;
    
    /**
     * 触发值
     */
    private String triggerValue;
    
    /**
     * 正常范围
     */
    private String normalRange;
    
    /**
     * 预警状态：ACTIVE(活跃), RESOLVED(已解决), IGNORED(已忽略)
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 处理时间
     */
    private LocalDateTime resolvedAt;
    
    /**
     * 处理人员
     */
    private String resolvedBy;
    
    /**
     * 处理备注
     */
    private String resolvedNote;
    
    /**
     * 关联的观察记录ID
     */
    private int observationId;
}