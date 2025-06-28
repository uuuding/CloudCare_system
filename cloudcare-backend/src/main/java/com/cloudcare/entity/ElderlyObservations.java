package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("elderly_observations")
public class ElderlyObservations extends Model<ElderlyObservations> {

    @TableId
    private Integer id;
    
    @TableField("elderly_id")
    private Integer elderlyId;
    
    @TableField("observation_time")
    private String observationTime;
    
    @TableField("body_temperature")
    private Double bodyTemperature;
    
    @TableField("systolic_bp")
    private Integer systolicBp;
    
    @TableField("cough")
    private Boolean cough;
    
    @TableField("heart_rate")
    private Integer heartRate;
    
    @TableField("sleep_hours")
    private Double sleepHours;
    
    @TableField("observation_location")
    private String observationLocation; // 新增属性，记录观察地点
    
    @TableField("height")
    private Double height; // 身高(cm)
    
    @TableField("weight")
    private Double weight; // 体重(kg)

}
