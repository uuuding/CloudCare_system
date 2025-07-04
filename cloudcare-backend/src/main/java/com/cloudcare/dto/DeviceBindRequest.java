package com.cloudcare.dto;

import lombok.Data;

/**
 * 设备绑定请求DTO
 * 用于GPS设备与老人绑定关系的建立，确保设备数据能正确关联到对应老人
 * 支持设备重新绑定和绑定关系验证
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class DeviceBindRequest {
    /**
     * 设备MAC地址
     * 设备的唯一标识符，通常为IMEI号或其他硬件标识
     * 用于在系统中唯一识别GPS设备
     */
    private String macid;
    
    /**
     * 老人ID
     * 系统中老人的唯一标识，用于建立设备与老人的绑定关系
     * 绑定后该设备的所有GPS数据将关联到此老人
     */
    private Integer elderlyId;
}