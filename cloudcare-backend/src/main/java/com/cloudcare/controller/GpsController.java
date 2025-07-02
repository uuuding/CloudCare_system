package com.cloudcare.controller;

import com.cloudcare.dto.GpsDataDTO;
import com.cloudcare.service.GpsLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * GPS数据接收控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/gps")
@RequiredArgsConstructor
public class GpsController {

    private final GpsLocationService gpsLocationService;

    /**
     * 接收GPS设备推送的定位数据
     * 接口地址：POST /api/gps/push
     * 请求参数格式：表单提交（application/x-www-form-urlencoded）
     * 
     * @param method 数据类型标识，值包括：status、alarm、pic、voice、video、PushTest
     * @param serialNumber 推送序号，毫秒时间戳字符串
     * @param data 对应的设备数据数组，格式详见每个 method
     * @return 成功返回serialNumber，失败返回错误信息
     */
    @PostMapping("/push")
    public ResponseEntity<String> receiveGpsData(
            @RequestParam("method") String method,
            @RequestParam("serialNumber") String serialNumber,
            @RequestParam("data") String data) {
        
        try {
            log.info("接收到GPS数据推送请求: method={}, serialNumber={}, data={}", 
                    method, serialNumber, data);
            
            // 构建GPS数据DTO
            GpsDataDTO gpsDataDTO = new GpsDataDTO();
            gpsDataDTO.setMethod(method);
            gpsDataDTO.setSerialNumber(serialNumber);
            
            // 解析data字段（JSON数组格式）
            if (data != null && !data.trim().isEmpty()) {
                try {
                    // 使用Jackson解析JSON数据
                    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    com.fasterxml.jackson.core.type.TypeReference<java.util.List<GpsDataDTO.GpsLocationData>> typeRef = 
                            new com.fasterxml.jackson.core.type.TypeReference<java.util.List<GpsDataDTO.GpsLocationData>>() {};
                    java.util.List<GpsDataDTO.GpsLocationData> locationDataList = objectMapper.readValue(data, typeRef);
                    gpsDataDTO.setData(locationDataList);
                } catch (Exception e) {
                    log.error("解析GPS数据失败: {}", e.getMessage(), e);
                    return ResponseEntity.badRequest().body("数据格式错误");
                }
            }
            
            // 处理GPS数据
            String result = gpsLocationService.processGpsData(gpsDataDTO);
            
            log.info("GPS数据处理成功，返回: {}", result);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("处理GPS数据推送失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("处理失败: " + e.getMessage());
        }
    }
    
    /**
     * JSONP格式的GPS数据接收接口（兼容性支持）
     */
    @PostMapping("/push/jsonp")
    public ResponseEntity<String> receiveGpsDataJsonp(
            @RequestParam("method") String method,
            @RequestParam("serialNumber") String serialNumber,
            @RequestParam("data") String data,
            @RequestParam(value = "callback", required = false) String callback) {
        
        try {
            // 复用普通接口的处理逻辑
            ResponseEntity<String> response = receiveGpsData(method, serialNumber, data);
            String result = response.getBody();
            
            // 如果有callback参数，返回JSONP格式
            if (callback != null && !callback.trim().isEmpty()) {
                String jsonpResponse = callback + "(\"" + result + "\")"; 
                return ResponseEntity.ok(jsonpResponse);
            }
            
            return response;
            
        } catch (Exception e) {
            log.error("处理JSONP格式GPS数据推送失败: {}", e.getMessage(), e);
            if (callback != null && !callback.trim().isEmpty()) {
                String jsonpError = callback + "(\"处理失败: " + e.getMessage() + "\")"; 
                return ResponseEntity.internalServerError().body(jsonpError);
            }
            return ResponseEntity.internalServerError().body("处理失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试接口
     */
    @GetMapping("/test")
    public ResponseEntity<String> testGpsInterface() {
        return ResponseEntity.ok("GPS接口服务正常运行");
    }
    
    /**
     * 设备绑定接口
     * 将GPS设备与老人进行绑定
     */
    @PostMapping("/bind")
    public ResponseEntity<String> bindDeviceToElderly(
            @RequestParam("macid") String macid,
            @RequestParam("elderlyId") Integer elderlyId) {
        
        try {
            boolean success = gpsLocationService.bindDeviceToElderly(macid, elderlyId);
            if (success) {
                return ResponseEntity.ok("设备绑定成功");
            } else {
                return ResponseEntity.badRequest().body("设备绑定失败");
            }
        } catch (Exception e) {
            log.error("设备绑定失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("设备绑定失败: " + e.getMessage());
        }
    }
}