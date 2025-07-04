package com.cloudcare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * GPS数据传输对象
 * 用于接收GPS设备推送的定位数据，支持多种数据类型和批量数据传输
 * 数据格式遵循设备厂商的标准协议，支持JSON序列化和反序列化
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class GpsDataDTO {
    
    /**
     * 数据类型标识
     * 用于区分不同类型的设备数据推送
     * 支持的值：status（状态数据）、alarm（告警数据）、pic（图片）、voice（语音）、video（视频）、PushTest（测试）
     */
    private String method;
    
    /**
     * 推送序号，毫秒时间戳字符串
     * 用于数据去重和排序，通常为设备生成的唯一标识
     */
    private String serialNumber;
    
    /**
     * GPS设备数据数组
     * 包含一个或多个设备的定位数据，支持批量数据传输
     */
    private List<GpsLocationData> data;
    
    /**
     * GPS定位数据内部类
     * 包含单个设备的完整定位信息，包括坐标、时间、状态等
     * 使用Jackson注解支持JSON字段映射和未知字段忽略
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GpsLocationData {
        
        /**
         * 设备编号（IMEI）
         * 设备的唯一标识符，用于识别和绑定设备
         */
        @JsonProperty("Macid")
        private String macid;
        
        /**
         * GPS时间戳（毫秒）
         * 设备获取GPS信号的时间，用于数据时序分析
         */
        @JsonProperty("GpsTime")
        private Long gpsTime;
        
        /**
         * 信号时间
         * 设备发送心跳信号的时间，用于设备在线状态监控
         */
        @JsonProperty("HeartTime")
        private String heartTime;
        
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