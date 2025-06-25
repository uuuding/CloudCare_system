package com.cloudcare.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloudcare.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 老人信息对象 elderly
 *
 * @author CloudCare
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("elderly")
public class Elderly extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 老人ID */
    @TableId(value = "elderly_id", type = IdType.AUTO)
    private Long elderlyId;

    /** 老人姓名 */
    private String elderlyName;

    /** 性别（0男 1女 2未知） */
    private String gender;

    /** 年龄 */
    private Integer age;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 身份证号 */
    private String idCard;

    /** 联系电话 */
    private String phone;

    /** 紧急联系人 */
    private String emergencyContact;

    /** 紧急联系人电话 */
    private String emergencyPhone;

    /** 家庭住址 */
    private String address;

    /** 健康状况 */
    private String healthCondition;

    /** 护理等级（0自理 1轻度 2中度 3重度） */
    private Integer careLevel;

    /** 入住时间 */
    private LocalDate checkInDate;

    /** 床位号 */
    private String bedNumber;

    /** 状态（0在院 1出院 2请假） */
    private Integer status;

    /** 备注 */
    private String remark;
}