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
 * 提供GPS设备数据接收、设备绑定管理等API接口
 * 支持多种数据格式（表单、JSONP）和设备管理功能
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
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
     * 主要用于接收GPS设备实时推送的位置数据，自动触发围栏检测和事件处理
     * 
     * 接口地址：POST /gps/push
     * 请求格式：application/x-www-form-urlencoded
     * 
     * @param method 数据类型标识，支持的值：
     *               - status: 状态数据（包含GPS位置信息）
     *               - alarm: 告警数据
     *               - pic: 图片数据
     *               - voice: 语音数据
     *               - video: 视频数据
     *               - PushTest: 测试推送
     * @param serialNumber 推送序号，通常为毫秒时间戳字符串，用于去重和排序
     * @param data JSON格式的设备数据数组，包含macid、GPS坐标、时间戳等信息
     * @return 成功时返回serialNumber，失败时返回错误信息
     * @see GpsDataDTO
     * @see GpsLocationService#processGpsData(GpsDataDTO)
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
     * 为支持跨域请求的老版本设备提供JSONP格式的数据接收接口
     * 
     * @param method 数据类型标识，同push接口
     * @param serialNumber 推送序号
     * @param data JSON格式的设备数据
     * @param callback JSONP回调函数名，可选参数
     * @return JSONP格式的响应或普通响应
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
     * GPS服务测试接口
     * 用于检查GPS数据接收服务是否正常运行
     * 
     * @return 服务状态信息
     */
    @GetMapping("/test")
    public ResponseEntity<String> testGpsInterface() {
        return ResponseEntity.ok("GPS接口服务正常运行");
    }
    
    /**
     * 设备绑定接口
     * 将GPS设备与老人建立绑定关系，绑定后设备数据会关联到对应老人
     * 
     * @param request 绑定请求对象，包含macid（设备MAC地址）和elderlyId（老人ID）
     * @return 绑定结果，包含success状态和message信息
     * @see DeviceBindRequest
     * @see DeviceBindingService#bindDevice(String, Integer, String)
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
     * 查询所有设备与老人的绑定关系，用于管理界面展示
     * 
     * @return 绑定关系列表，包含设备信息和老人信息
     * @see DeviceBindingService#getAllDeviceBindings()
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
     * 解除GPS设备与老人的绑定关系，解绑后设备数据不再关联到老人
     * 
     * @param macid 设备MAC地址
     * @return 解绑结果，包含success状态和message信息
     * @see DeviceBindingService#unbindDevice(String, Integer, String)
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