package com.cloudcare.controller;

import com.cloudcare.dto.DeviceBindRequest;
import com.cloudcare.dto.GpsDataDTO;
import com.cloudcare.service.DeviceBindingService;
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
@RequestMapping("/gps")
@RequiredArgsConstructor
public class GpsController {

    private final GpsLocationService gpsLocationService;
    private final DeviceBindingService deviceBindingService;

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
    public ResponseEntity<?> bindDeviceToElderly(@RequestBody DeviceBindRequest request) {
        
        try {
            if (request.getMacid() == null || request.getMacid().trim().isEmpty()) {
                java.util.Map<String, Object> errorResponse = new java.util.HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "MAC地址不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            if (request.getElderlyId() == null) {
                java.util.Map<String, Object> errorResponse = new java.util.HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "老人ID不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            boolean success = deviceBindingService.bindDevice(request.getMacid(), request.getElderlyId(), "system");
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "设备绑定成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "设备绑定失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("设备绑定失败: {}", e.getMessage(), e);
            java.util.Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "设备绑定失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 获取设备绑定列表
     */
    @GetMapping("/bindings")
    public ResponseEntity<?> getDeviceBindings() {
        try {
            java.util.List<java.util.Map<String, Object>> bindings = deviceBindingService.getAllDeviceBindings();
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("success", true);
            response.put("data", bindings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取设备绑定列表失败: {}", e.getMessage(), e);
            java.util.Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取设备绑定列表失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * 解绑设备
     */
    @DeleteMapping("/unbind/{macid}")
    public ResponseEntity<?> unbindDevice(@PathVariable("macid") String macid) {
        try {
            // 先获取绑定关系，然后解绑
            com.cloudcare.entity.DeviceBinding binding = deviceBindingService.getBindingByMacid(macid);
            boolean success = false;
            if (binding != null) {
                success = deviceBindingService.unbindDevice(macid, binding.getElderlyId(), "system");
            }
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "设备解绑成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "设备解绑失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("设备解绑失败: {}", e.getMessage(), e);
            java.util.Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "设备解绑失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}