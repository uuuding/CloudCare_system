package com.cloudcare.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("elderly_profile")
public class ElderlyProfile extends Model<ElderlyProfile> {

    @TableId
    private Integer id;
    private String name;
    private Integer age;
    private String gender;  // '男', '女', '其他'
    private String createTime;
    private String updateTime;

}