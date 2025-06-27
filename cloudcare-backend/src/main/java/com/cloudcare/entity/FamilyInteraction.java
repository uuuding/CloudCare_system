package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家属互动实体类
 */
@Data
@TableName("family_interaction")
public class FamilyInteraction {

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
     * 家属ID
     */
    @TableField("family_member_id")
    private Long familyMemberId;

    /**
     * 互动类型：phone-电话沟通，video-视频通话，sms-短信交流，wechat-微信聊天，email-邮件往来，face_to_face-面对面交流，other-其他
     */
    @TableField("interaction_type")
    private String interactionType;

    /**
     * 互动内容
     */
    @TableField("content")
    private String content;

    /**
     * 互动时间
     */
    @TableField("interaction_time")
    private LocalDateTime interactionTime;

    /**
     * 回复内容
     */
    @TableField("reply_content")
    private String replyContent;

    /**
     * 回复时间
     */
    @TableField("reply_time")
    private LocalDateTime replyTime;

    /**
     * 状态：pending-待回复，replied-已回复，closed-已关闭
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

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
     * 老人姓名（非数据库字段）
     */
    @TableField(exist = false)
    private String elderName;

    /**
     * 家属姓名（非数据库字段）
     */
    @TableField(exist = false)
    private String familyName;

    /**
     * 家属关系（非数据库字段）
     */
    @TableField(exist = false)
    private String familyRelation;
}