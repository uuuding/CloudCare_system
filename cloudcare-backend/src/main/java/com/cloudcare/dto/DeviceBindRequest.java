package com.cloudcare.dto;

import lombok.Data;

/**
 * 设备绑定请求DTO
 */
@Data
public class DeviceBindRequest {
    /**
     * 设备MAC地址
     */
    private String macid;
    
    /**
     * 老人ID
     */
    private Integer elderlyId;
}