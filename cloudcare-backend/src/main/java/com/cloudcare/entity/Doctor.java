package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医生信息实体类
 *
 * @author cloudcare
 */
@Data
@TableName("doctor_info")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医生ID
     */
    @TableId(value = "doctor_id", type = IdType.AUTO)
    private Long doctorId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 医生编号
     */
    private String doctorCode;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 性别（0：未知，1：男，2：女）
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 职称（1：主任医师，2：副主任医师，3：主治医师，4：住院医师）
     */
    private Integer title;

    /**
     * 科室
     */
    private String department;

    /**
     * 专业领域
     */
    private String specialization;

    /**
     * 执业证号
     */
    private String licenseNumber;

    /**
     * 工作年限
     */
    private Integer workYears;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 状态（0：禁用，1：正常）
     */
    private Integer status;

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

    /**
     * 删除标志（0：未删除，1：已删除）
     */
    private Integer deleted;

}