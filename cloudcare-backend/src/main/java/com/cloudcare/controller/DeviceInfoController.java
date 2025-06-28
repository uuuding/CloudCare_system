package com.cloudcare.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.dto.DeviceInfoDTO;
import com.cloudcare.entity.DeviceInfo;
import com.cloudcare.service.DeviceInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备信息控制器
 *
 * @author cloudcare
 */
@Slf4j
@RestController
@RequestMapping("/device")
@Tag(name = "设备管理", description = "设备信息管理接口")
public class DeviceInfoController {

    @Autowired
    private DeviceInfoService deviceInfoService;

    /**
     * 分页查询设备信息列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询设备信息列表", description = "分页查询设备信息列表")
    public Result<Page<DeviceInfo>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "设备编号") @RequestParam(required = false) String deviceCode,
            @Parameter(description = "设备名称") @RequestParam(required = false) String deviceName,
            @Parameter(description = "设备类型") @RequestParam(required = false) Integer deviceType,
            @Parameter(description = "设备状态") @RequestParam(required = false) Integer deviceStatus,
            @Parameter(description = "负责人姓名") @RequestParam(required = false) String managerName,
            @Parameter(description = "使用位置") @RequestParam(required = false) String location) {
        
        Page<DeviceInfo> page = new Page<>(pageNum, pageSize);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceCode(deviceCode);
        deviceInfo.setDeviceName(deviceName);
        deviceInfo.setDeviceType(deviceType);
        deviceInfo.setDeviceStatus(deviceStatus);
        deviceInfo.setManagerName(managerName);
        deviceInfo.setLocation(location);
        
        Page<DeviceInfo> result = deviceInfoService.selectDeviceInfoPage(page, deviceInfo);
        return Result.success(result);
    }

    /**
     * 根据ID查询设备信息
     */
    @GetMapping("/{deviceId}")
    @Operation(summary = "根据ID查询设备信息", description = "根据ID查询设备信息详情")
    public Result<DeviceInfo> getById(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        DeviceInfo deviceInfo = deviceInfoService.selectDeviceInfoById(deviceId);
        return Result.success(deviceInfo);
    }

    /**
     * 新增设备信息
     */
    @PostMapping
    @Operation(summary = "新增设备信息", description = "新增设备信息")
    public Result<Void> add(@Validated @RequestBody DeviceInfoDTO deviceInfoDTO) {
        DeviceInfo deviceInfo = new DeviceInfo();
        BeanUtils.copyProperties(deviceInfoDTO, deviceInfo);
        
        boolean result = deviceInfoService.insertDeviceInfo(deviceInfo);
        return result ? Result.success(null) : Result.error("新增设备信息失败");
    }

    /**
     * 修改设备信息
     */
    @PutMapping
    @Operation(summary = "修改设备信息", description = "修改设备信息")
    public Result<Void> update(@Validated @RequestBody DeviceInfoDTO deviceInfoDTO) {
        DeviceInfo deviceInfo = new DeviceInfo();
        BeanUtils.copyProperties(deviceInfoDTO, deviceInfo);
        
        boolean result = deviceInfoService.updateDeviceInfo(deviceInfo);
        return result ? Result.success(null) : Result.error("修改设备信息失败");
    }

    /**
     * 删除设备信息
     */
    @DeleteMapping("/{deviceIds}")
    @Operation(summary = "删除设备信息", description = "根据ID删除设备信息")
    public Result<Void> remove(@Parameter(description = "设备ID，多个用逗号分隔") @PathVariable Long[] deviceIds) {
        boolean result = deviceInfoService.deleteDeviceInfoByIds(deviceIds);
        return result ? Result.success(null) : Result.error("删除设备信息失败");
    }

    /**
     * 更新设备状态
     */
    @PutMapping("/status")
    @Operation(summary = "更新设备状态", description = "更新设备状态")
    public Result<Void> updateStatus(
            @Parameter(description = "设备ID") @RequestParam Long deviceId,
            @Parameter(description = "设备状态") @RequestParam Integer status) {
        
        boolean result = deviceInfoService.updateDeviceStatus(deviceId, status);
        return result ? Result.success(null) : Result.error("更新设备状态失败");
    }

    /**
     * 获取设备统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取设备统计信息", description = "获取设备统计信息")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = deviceInfoService.getDeviceStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取需要维护的设备列表
     */
    @GetMapping("/maintenance-required")
    @Operation(summary = "获取需要维护的设备列表", description = "获取需要维护的设备列表")
    public Result<List<DeviceInfo>> getMaintenanceRequiredDevices() {
        List<DeviceInfo> devices = deviceInfoService.getMaintenanceRequiredDevices();
        return Result.success(devices);
    }

    /**
     * 获取设备类型选项
     */
    @GetMapping("/device-types")
    @Operation(summary = "获取设备类型选项", description = "获取设备类型选项")
    public Result<Map<Integer, String>> getDeviceTypes() {
        Map<Integer, String> deviceTypes = Map.of(
                1, "体温计",
                2, "血压计",
                3, "血糖仪",
                4, "心率监测仪",
                5, "血氧仪",
                6, "体重秤",
                7, "其他"
        );
        return Result.success(deviceTypes);
    }

    /**
     * 获取设备状态选项
     */
    @GetMapping("/device-status")
    @Operation(summary = "获取设备状态选项", description = "获取设备状态选项")
    public Result<Map<Integer, String>> getDeviceStatus() {
        Map<Integer, String> deviceStatus = Map.of(
                0, "停用",
                1, "正常",
                2, "维修中",
                3, "已报废"
        );
        return Result.success(deviceStatus);
    }
}