package com.cloudcare.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家属互动DTO
 */
@Data
public class FamilyInteractionDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 家属ID
     */
    private Long familyMemberId;

    /**
     * 互动类型
     */
    private String interactionType;

    /**
     * 互动内容
     */
    private String content;

    /**
     * 互动时间
     */
    private LocalDateTime interactionTime;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复时间
     */
    private LocalDateTime replyTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 老人姓名
     */
    private String elderName;

    /**
     * 家属姓名
     */
    private String familyName;

    /**
     * 家属关系
     */
    private String familyRelation;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}