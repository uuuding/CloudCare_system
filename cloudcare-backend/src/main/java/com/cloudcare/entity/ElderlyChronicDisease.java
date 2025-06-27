package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("elderly_chronic_disease")
public class ElderlyChronicDisease extends Model<ElderlyChronicDisease> {

    @TableId
    private Integer id;
    private Integer elderlyId;
    private String diseaseName;
    private String diseaseCategory; // 'A', 'B', 'C'
    private String diagnosisDate;

}
