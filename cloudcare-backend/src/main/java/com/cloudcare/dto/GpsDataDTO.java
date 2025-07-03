package com.cloudcare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * GPS数据传输对象
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
     * GPS设备数据数组
     */
    private List<GpsLocationData> data;
    
    /**
     * GPS定位数据内部类
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GpsLocationData {
        
        /**
         * 设备编号（IMEI）
         */
        @JsonProperty("Macid")
        private String macid;
        
        /**
         * GPS时间戳（毫秒）
         */
        @JsonProperty("GpsTime")
        private Long gpsTime;
        
        /**
         * 信号时间
         */
        @JsonProperty("HeartTime")
        private Long heartTime;
        
        /**
         * 最后更新位置时间
         */
        @JsonProperty("UpdTime")
        private Long updTime;
        
        /**
         * GPS纬度
         */
        @JsonProperty("Lat")
        private String lat;
        
        /**
         * GPS经度
         */
        @JsonProperty("Lon")
        private String lon;
        
        /**
         * 地图纬度
         */
        @JsonProperty("MapLat")
        private String mapLat;
        
        /**
         * 地图经度
         */
        @JsonProperty("MapLon")
        private String mapLon;
        
        /**
         * 速度（单位：km/h）
         */
        @JsonProperty("Speed")
        private String speed;
        
        /**
         * 方向角度
         */
        @JsonProperty("Dir")
        private String dir;
        
        /**
         * 状态字段，逗号分隔
         */
        @JsonProperty("Stats")
        private String stats;
        
        /**
         * 状态值字段，逗号分隔
         */
        @JsonProperty("Value")
        private String value;
    }
}