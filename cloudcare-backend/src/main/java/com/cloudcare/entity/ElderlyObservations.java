package com.cloudcare.entity;

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
    private Integer elderlyId;
    private String observationTime;
    private Double bodyTemperature;
    private Integer systolicBp;
    private Boolean cough;
    private Integer heartRate;
    private Double sleep_hours;
    private String observationLocation; // 新增属性，记录观察地点
    private Double height; // 身高(cm)
    private Double weight; // 体重(kg)

}
