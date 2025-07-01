package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短信发送记录实体类
 *
 * @author cloudcare
 */
@Data
@TableName("sms_record")
public class SmsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 短信内容
     */
    @TableField("content")
    private String content;

    /**
     * 短信类型：normal-普通短信，template-模板短信，batch-批量短信
     */
    @TableField("type")
    private String type;

    /**
     * 模板代码
     */
    @TableField("template_code")
    private String templateCode;

    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 发送状态：success-成功，failed-失败，sending-发送中
     */
    @TableField("status")
    private String status;

    /**
     * 发送时间
     */
    @TableField("send_time")
    private LocalDateTime sendTime;

    /**
     * 响应代码
     */
    @TableField("response_code")
    private String responseCode;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 模板参数（JSON格式）
     */
    @TableField("params")
    private String params;

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
}