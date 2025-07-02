package com.cloudcare.dto;

import lombok.Data;

import java.util.List;

/**
 * GPS数据推送DTO
 */
@Data
public class GpsDataDTO {
    
    /**
     * 数据类型标识
     */
    private String method;
    
    /**
     * 推送序号，毫秒时间戳字符串
     */
    private String serialNumber;
    
    /**
     * 设备数据数组
     */
    private List<GpsLocationData> data;
    
    /**
     * GPS定位数据
     */
    @Data
    public static class GpsLocationData {
        
        /**
         * 设备编号（IMEI）
         */
        private String Macid;
        
        /**
         * GPS时间戳（毫秒）
         */
        private Long GpsTime;
        
        /**
         * 信号时间
         */
        private Long HeartTime;
        
        /**
         * 最后更新位置时间
         */
        private Long UpdTime;
        
        /**
         * GPS纬度
         */
        private String Lat;
        
        /**
         * GPS经度
         */
        private String Lon;
        
        /**
         * 速度（单位：km/h）
         */
        private String Speed;
        
        /**
         * 方向角度
         */
        private String Dir;
        
        /**
         * 状态字段，逗号分隔
         */
        private String Stats;
        
        /**
         * 状态值字段，逗号分隔
         */
        private String Value;
    }
}