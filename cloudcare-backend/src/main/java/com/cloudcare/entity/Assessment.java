package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评估报告实体类
 *
 * @author cloudcare
 */
@Data
@TableName("assessment_report")
public class Assessment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报告ID
     */
    @TableId(value = "report_id", type = IdType.AUTO)
    private Long reportId;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 报告编号
     */
    private String reportCode;

    /**
     * 报告类型（1：入院评估，2：定期评估，3：专项评估）
     */
    private Integer reportType;

    /**
     * 评估时间
     */
    private LocalDateTime assessmentTime;

    /**
     * 评估人ID
     */
    private Long assessorId;

    /**
     * 评估人姓名
     */
    private String assessorName;

    /**
     * 身体状况评分（0-100）
     */
    private Integer physicalScore;

    /**
     * 身体状况描述
     */
    private String physicalDesc;

    /**
     * 心理状况评分（0-100）
     */
    private Integer mentalScore;

    /**
     * 心理状况描述
     */
    private String mentalDesc;

    /**
     * 认知能力评分（0-100）
     */
    private Integer cognitiveScore;

    /**
     * 认知能力描述
     */
    private String cognitiveDesc;

    /**
     * 日常生活能力评分（0-100）
     */
    private Integer dailyLifeScore;

    /**
     * 日常生活能力描述
     */
    private String dailyLifeDesc;

    /**
     * 社会参与评分（0-100）
     */
    private Integer socialScore;

    /**
     * 社会参与描述
     */
    private String socialDesc;

    /**
     * 总评分（0-100）
     */
    private Integer totalScore;

    /**
     * 评估结论
     */
    private String conclusion;

    /**
     * 建议
     */
    private String suggestion;

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