package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家属成员实体类
 */
@Data
@TableName("family_member")
public class FamilyMember {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 老人ID
     */
    @TableField("elder_id")
    private Long elderId;

    /**
     * 家属用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 与老人的关系
     */
    @TableField("relation")
    private String relation;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @TableField("contact_email")
    private String contactEmail;

    /**
     * 是否为紧急联系人
     */
    @TableField("emergency_contact")
    private Boolean emergencyContact;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 家属姓名（非数据库字段）
     */
    @TableField(exist = false)
    private String name;
}