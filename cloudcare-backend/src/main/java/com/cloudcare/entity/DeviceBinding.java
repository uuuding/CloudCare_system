package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备绑定实体类
 * 管理GPS设备与老人的绑定关系，确保设备唯一性和数据关联
 * 支持设备绑定、解绑操作，记录绑定状态和时间信息
 * 用于GPS数据归属识别、设备管理和权限控制
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@TableName("device_binding")
public class DeviceBinding {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 设备编号（IMEI）
     */
    private String macid;
    
    /**
     * 老人ID
     */
    private Integer elderlyId;
    
    /**
     * 绑定时间
     */
    private LocalDateTime bindTime;
    
    /**
     * 解绑时间
     */
    private LocalDateTime unbindTime;
    
    /**
     * 绑定状态：1-已绑定，0-已解绑
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建者
     */
    private String createBy;
    
    /**
     * 更新者
     */
    private String updateBy;
}