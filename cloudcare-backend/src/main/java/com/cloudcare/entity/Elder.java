package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 老人信息实体类
 *
 * @author cloudcare
 */
@Data
@TableName("elder_info")
public class Elder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 老人ID
     */
    @TableId(value = "elder_id", type = IdType.AUTO)
    private Long elderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 老人编号
     */
    private String elderCode;

    /**
     * 老人姓名
     */
    private String elderName;

    /**
     * 性别（0：未知，1：男，2：女）
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 紧急联系人
     */
    private String emergencyContact;

    /**
     * 紧急联系人电话
     */
    private String emergencyPhone;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 血型（A、B、AB、O）
     */
    private String bloodType;

    /**
     * 过敏史
     */
    private String allergies;

    /**
     * 慢性病史
     */
    private String chronicDiseases;

    /**
     * 健康状况（1：良好，2：一般，3：较差，4：危重）
     */
    private Integer healthStatus;

    /**
     * 自理能力（1：完全自理，2：部分自理，3：完全不能自理）
     */
    private Integer selfCareAbility;

    /**
     * 入住状态（0：未入住，1：已入住）
     */
    private Integer residenceStatus;

    /**
     * 入住时间
     */
    private LocalDateTime checkInTime;

    /**
     * 床位号
     */
    private String bedNumber;

    /**
     * 负责医生ID
     */
    private Long doctorId;

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