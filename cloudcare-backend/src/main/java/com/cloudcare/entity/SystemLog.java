package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统日志实体类
 *
 * @author cloudcare
 */
@Data
@TableName("system_log")
public class SystemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日志级别：DEBUG-调试，INFO-信息，WARN-警告，ERROR-错误
     */
    @TableField("level")
    private String level;

    /**
     * 模块名称：USER-用户，ELDERLY-老人档案，HEALTH-健康管理，MEDICAL-医疗服务，DEVICE-设备，SYSTEM-系统等
     */
    @TableField("module")
    private String module;

    /**
     * 操作用户名
     */
    @TableField("username")
    private String username;

    /**
     * 操作用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 操作类型/名称
     */
    @TableField("operation")
    private String operation;

    /**
     * 日志内容详情
     */
    @TableField("content")
    private String content;

    /**
     * 请求URL
     */
    @TableField("request_url")
    private String requestUrl;

    /**
     * 请求方法：GET,POST,PUT,DELETE等
     */
    @TableField("request_method")
    private String requestMethod;

    /**
     * 请求参数（JSON格式）
     */
    @TableField("request_params")
    private String requestParams;

    /**
     * 响应状态码
     */
    @TableField("response_status")
    private Integer responseStatus;

    /**
     * 响应时间（毫秒）
     */
    @TableField("response_time")
    private Long responseTime;

    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 用户代理信息
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 异常堆栈信息（错误日志专用）
     */
    @TableField("stack_trace")
    private String stackTrace;

    /**
     * 业务ID（如老人ID、订单ID等）
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;

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