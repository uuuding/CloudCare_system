package com.cloudcare.controller;

import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GpsAlarm;
import com.cloudcare.entity.GpsDevice;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.service.GpsAlarmService;
import com.cloudcare.service.GpsDeviceService;
import com.cloudcare.service.GpsLocationService;
import com.cloudcare.service.GeoFenceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * GPS数据接收控制器
 * 用于接收GPS定位器推送的数据
 */
@Slf4j
@RestController
@RequestMapping("/api/gps")
public class GpsDataReceiveController {

    @Autowired
    private GpsDeviceService gpsDeviceService;

    @Autowired
    private GpsLocationService gpsLocationService;

    @Autowired
    private GpsAlarmService gpsAlarmService;

    @Autowired
    private GeoFenceService geoFenceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 接收GPS数据推送的统一接口
     * 支持报警推送、位置状态推送、多媒体上传和测试推送
     */
    @PostMapping("/receive")
    public String receiveGpsData(HttpServletRequest request, @RequestParam Map<String, String> params) {
        try {
            String method = params.get("method");
            String serialNumber = params.get("serialNumber");
            String data = params.get("data");
            
            log.info("接收到GPS数据推送: method={}, serialNumber={}", method, serialNumber);
            
            if (serialNumber == null || serialNumber.isEmpty()) {
                log.error("serialNumber不能为空");
                return "ERROR: serialNumber is required";
            }
            
            switch (method) {
                case "alarm":
                    handleAlarmData(data);
                    break;
                case "status":
                    handleStatusData(data);
                    break;
                case "pic":
                    handlePicData(data);
                    break;
                case "voice":
                    handleVoiceData(data);
                    break;
                case "video":
                    handleVideoData(data);
                    break;
                case "PushTest":
                    handlePushTest();
                    break;
                default:
                    log.warn("未知的推送类型: {}", method);
                    return "ERROR: Unknown method";
            }
            
            // 返回序列号表示接收成功
            return serialNumber;
            
        } catch (Exception e) {
            log.error("处理GPS数据推送时发生错误", e);
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * 处理报警数据推送
     */
    private void handleAlarmData(String data) {
        try {
            JsonNode dataArray = objectMapper.readTree(data);
            for (JsonNode alarmNode : dataArray) {
                GpsAlarm alarm = new GpsAlarm();
                alarm.setAlarmId(alarmNode.get("Id").asText());
                alarm.setMacid(alarmNode.get("Macid").asText());
                alarm.setDeviceName(alarmNode.get("FullName").asText());
                alarm.setAlarmTime(System.currentTimeMillis()); // 转换时间戳
                alarm.setLat(alarmNode.get("Lat").asDouble());
                alarm.setLon(alarmNode.get("Lon").asDouble());
                alarm.setMapLat(alarmNode.get("MapLat").asDouble());
                alarm.setMapLon(alarmNode.get("MapLon").asDouble());
                alarm.setSpeed(alarmNode.get("Speed").asDouble());
                alarm.setDirection(alarmNode.get("Dir").asInt());
                alarm.setClassify(alarmNode.get("Classify").asInt());
                alarm.setDescribeInfo(alarmNode.get("Describe").asText());
                alarm.setNote(alarmNode.get("Notea").asText());
                alarm.setAddTime(System.currentTimeMillis());
                alarm.setHandleStatus(0); // 未处理
                alarm.setCreateTime(LocalDateTime.now());
                
                // 保存报警记录
                gpsAlarmService.save(alarm);
                
                // 检查电子围栏
                checkGeoFence(alarm);
                
                log.info("保存报警记录: 设备={}, 类型={}", alarm.getMacid(), alarm.getClassify());
            }
        } catch (Exception e) {
            log.error("处理报警数据时发生错误", e);
        }
    }

    /**
     * 处理位置状态数据推送
     */
    private void handleStatusData(String data) {
        try {
            JsonNode dataArray = objectMapper.readTree(data);
            for (JsonNode statusNode : dataArray) {
                String deviceImei = statusNode.get("Macid").asText();
                
                // 更新设备状态
                GpsDevice device = gpsDeviceService.getByImei(deviceImei);
                if (device != null) {
                    device.setLastGpsTime(LocalDateTime.now()); // 转换时间戳
                    device.setLastHeartTime(LocalDateTime.now()); // 转换时间戳
                    device.setStatus(1); // 在线
                    gpsDeviceService.updateById(device);
                }
                
                // 保存位置记录
                GpsLocation location = new GpsLocation();
                location.setMacid(deviceImei);
                location.setLat(statusNode.get("Lat").asDouble());
                location.setLon(statusNode.get("Lon").asDouble());
                location.setMapLat(statusNode.has("MapLat") ? statusNode.get("MapLat").asDouble() : statusNode.get("Lat").asDouble());
                location.setMapLon(statusNode.has("MapLon") ? statusNode.get("MapLon").asDouble() : statusNode.get("Lon").asDouble());
                location.setSpeed(statusNode.get("Speed").asDouble());
                location.setDirection(statusNode.get("Dir").asDouble());
                location.setGpsTime(System.currentTimeMillis()); // 当前时间戳
                location.setCreateTime(LocalDateTime.now());
                
                // 解析状态信息
                String stats = statusNode.get("Stats").asText();
                location.setStats(stats);
                String[] statArray = stats.split(",");
                if (statArray.length >= 6) {
                    location.setSignalType(Integer.parseInt(statArray[5])); // 定位状态
                }
                
                gpsLocationService.save(location);
                
                // 检查电子围栏
                checkGeoFenceForLocation(location);
                
                log.info("保存位置记录: 设备={}, 位置=({}, {})", deviceImei, location.getLat(), location.getLon());
            }
        } catch (Exception e) {
            log.error("处理位置状态数据时发生错误", e);
        }
    }

    /**
     * 处理图片上传消息
     */
    private void handlePicData(String data) {
        try {
            JsonNode dataArray = objectMapper.readTree(data);
            for (JsonNode picNode : dataArray) {
                String deviceImei = picNode.get("Macid").asText();
                String picUrl = picNode.get("PicName").asText();
                
                log.info("接收到图片上传: 设备={}, 图片地址={}", deviceImei, picUrl);
                
                // 这里可以保存图片信息到数据库或进行其他处理
            }
        } catch (Exception e) {
            log.error("处理图片数据时发生错误", e);
        }
    }

    /**
     * 处理语音上传消息
     */
    private void handleVoiceData(String data) {
        try {
            JsonNode dataArray = objectMapper.readTree(data);
            for (JsonNode voiceNode : dataArray) {
                String deviceImei = voiceNode.get("Macid").asText();
                String voiceUrl = voiceNode.get("VoiceName").asText();
                
                log.info("接收到语音上传: 设备={}, 语音地址={}", deviceImei, voiceUrl);
                
                // 这里可以保存语音信息到数据库或进行其他处理
            }
        } catch (Exception e) {
            log.error("处理语音数据时发生错误", e);
        }
    }

    /**
     * 处理视频上传消息
     */
    private void handleVideoData(String data) {
        try {
            JsonNode dataArray = objectMapper.readTree(data);
            for (JsonNode videoNode : dataArray) {
                String deviceImei = videoNode.get("Macid").asText();
                String videoUrl = videoNode.get("VideoName").asText();
                
                log.info("接收到视频上传: 设备={}, 视频地址={}", deviceImei, videoUrl);
                
                // 这里可以保存视频信息到数据库或进行其他处理
            }
        } catch (Exception e) {
            log.error("处理视频数据时发生错误", e);
        }
    }

    /**
     * 处理推送测试
     */
    private void handlePushTest() {
        log.info("接收到推送测试请求");
    }

    /**
     * 支持JSONP的测试接口
     */
    @GetMapping("/receive")
    public String receiveGpsDataJsonp(@RequestParam Map<String, String> params) {
        String method = params.get("method");
        String serialNumber = params.get("serialNumber");
        String callback = params.get("callback");
        
        if ("PushTest".equals(method)) {
            log.info("接收到JSONP推送测试请求: serialNumber={}, callback={}", serialNumber, callback);
            
            if (callback != null && !callback.isEmpty()) {
                // JSONP响应
                return callback + "(\"" + serialNumber + "\")"; 
            } else {
                // 普通响应
                return serialNumber;
            }
        }
        
        return "ERROR: Invalid method";
    }

    /**
     * 检查报警的电子围栏
     */
    private void checkGeoFence(GpsAlarm alarm) {
        try {
            // 根据设备IMEI获取绑定的老人
            GpsDevice device = gpsDeviceService.getByImei(alarm.getMacid());
            if (device != null && device.getElderlyId() != null) {
                // 检查是否触发围栏报警
                boolean isInFence = geoFenceService.isLocationInFence(
                    null, // 这里需要传入具体的围栏对象，暂时传null
                    alarm.getLat(), 
                    alarm.getLon()
                );
                
                if (!isInFence) {
                    // 触发围栏报警，可以在这里添加额外的处理逻辑
                    log.warn("设备 {} 触发围栏报警", alarm.getMacid());
                }
            }
        } catch (Exception e) {
            log.error("检查围栏时发生错误", e);
        }
    }

    /**
     * 检查位置的电子围栏
     */
    private void checkGeoFenceForLocation(GpsLocation location) {
        try {
            // 根据设备IMEI获取绑定的老人
            GpsDevice device = gpsDeviceService.getByImei(location.getMacid());
            if (device != null && device.getElderlyId() != null) {
                // 检查围栏报警
                List<GeoFence> triggeredFences = geoFenceService.checkFenceAlarms(
                    device.getElderlyId(), 
                    location
                );
                
                // 为每个触发的围栏创建报警记录
                for (GeoFence fence : triggeredFences) {
                    GpsAlarm alarm = new GpsAlarm();
                    alarm.setMacid(location.getMacid());
                    alarm.setElderlyId(device.getElderlyId());
                    alarm.setAlarmTime(location.getGpsTime());
                    alarm.setAddTime(System.currentTimeMillis());
                    alarm.setLat(location.getLat());
                    alarm.setLon(location.getLon());
                    alarm.setMapLat(location.getMapLat());
                    alarm.setMapLon(location.getMapLon());
                    alarm.setSpeed(location.getSpeed());
                    alarm.setDirection(location.getDirection().intValue());
                    alarm.setClassify(3); // 围栏报警类型
                    alarm.setDescribeInfo("电子围栏报警: " + fence.getFenceName());
                    alarm.setHandleStatus(0); // 未处理
                    alarm.setCreateTime(LocalDateTime.now());
                    gpsAlarmService.save(alarm);
                }
            }
        } catch (Exception e) {
            log.error("检查位置围栏时发生错误", e);
        }
    }
}