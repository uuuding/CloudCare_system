package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.entity.GpsDevice;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.service.GpsDataService;
import com.cloudcare.service.impl.GpsDeviceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GPS设备管理控制器
 *
 * @author CloudCare
 */
@Slf4j
@RestController
@RequestMapping("/api/gps-device")
@RequiredArgsConstructor
@Tag(name = "GPS设备管理", description = "GPS设备相关接口")
public class GpsDeviceController {

    private final GpsDeviceServiceImpl gpsDeviceService;
    private final GpsDataService gpsDataService;

    @GetMapping("/list")
    @Operation(summary = "分页查询设备列表", description = "根据条件分页查询GPS设备列表")
    public Result<IPage<GpsDevice>> getDeviceList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long elderlyId,
            @RequestParam(required = false) Integer status) {
        try {
            Page<GpsDevice> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<GpsDevice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(elderlyId != null, GpsDevice::getElderlyId, elderlyId)
                   .eq(status != null, GpsDevice::getStatus, status)
                   .orderByDesc(GpsDevice::getCreateTime);
            IPage<GpsDevice> result = gpsDeviceService.page(page, wrapper);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询设备列表失败", e);
            return Result.error("查询设备列表失败");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取设备详情", description = "根据设备ID获取详细信息")
    public Result<GpsDevice> getDeviceById(@PathVariable Long id) {
        try {
            GpsDevice device = gpsDeviceService.getById(id);
            if (device != null) {
                return Result.success(device);
            } else {
                return Result.error("设备不存在");
            }
        } catch (Exception e) {
            log.error("查询设备详情失败", e);
            return Result.error("查询设备详情失败");
        }
    }

    @PostMapping
    @Operation(summary = "添加设备", description = "添加新的GPS设备")
    public Result<String> addDevice(@RequestBody GpsDevice device) {
        try {
            // 检查IMEI是否已存在
            GpsDevice existDevice = gpsDeviceService.getOne(
                new LambdaQueryWrapper<GpsDevice>()
                    .eq(GpsDevice::getMacid, device.getMacid())
            );
            if (existDevice != null) {
                return Result.error("设备IMEI已存在");
            }
            
            device.setCreateTime(LocalDateTime.now());
            device.setUpdateTime(LocalDateTime.now());
            if (gpsDeviceService.save(device)) {
                return Result.success("添加设备成功");
            } else {
                return Result.error("添加设备失败");
            }
        } catch (Exception e) {
            log.error("添加设备失败", e);
            return Result.error("添加设备失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新设备", description = "更新GPS设备信息")
    public Result<String> updateDevice(@PathVariable Long id, @RequestBody GpsDevice device) {
        try {
            device.setId(id);
            device.setUpdateTime(LocalDateTime.now());
            if (gpsDeviceService.updateById(device)) {
                return Result.success("更新设备成功");
            } else {
                return Result.error("更新设备失败");
            }
        } catch (Exception e) {
            log.error("更新设备失败", e);
            return Result.error("更新设备失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除设备", description = "根据ID删除GPS设备")
    public Result<String> deleteDevice(@PathVariable Long id) {
        try {
            if (gpsDeviceService.removeById(id)) {
                return Result.success("删除设备成功");
            } else {
                return Result.error("删除设备失败");
            }
        } catch (Exception e) {
            log.error("删除设备失败", e);
            return Result.error("删除设备失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/bind")
    @Operation(summary = "绑定设备到老人", description = "将GPS设备绑定到指定老人")
    public Result<String> bindDeviceToElderly(@PathVariable Long id, @RequestParam Long elderlyId) {
        try {
            GpsDevice device = new GpsDevice();
            device.setId(id);
            device.setElderlyId(elderlyId);
            device.setUpdateTime(LocalDateTime.now());
            if (gpsDeviceService.updateById(device)) {
                return Result.success("绑定设备成功");
            } else {
                return Result.error("绑定设备失败");
            }
        } catch (Exception e) {
            log.error("绑定设备失败", e);
            return Result.error("绑定设备失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/unbind")
    @Operation(summary = "解绑设备", description = "解除GPS设备与老人的绑定关系")
    public Result<String> unbindDevice(@PathVariable Long id) {
        try {
            GpsDevice device = new GpsDevice();
            device.setId(id);
            device.setElderlyId(null);
            device.setUpdateTime(LocalDateTime.now());
            if (gpsDeviceService.updateById(device)) {
                return Result.success("解绑设备成功");
            } else {
                return Result.error("解绑设备失败");
            }
        } catch (Exception e) {
            log.error("解绑设备失败", e);
            return Result.error("解绑设备失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/location")
    @Operation(summary = "获取设备最新位置", description = "获取指定设备的最新GPS位置")
    public Result<GpsLocation> getDeviceLocation(@PathVariable Long id) {
        try {
            GpsDevice device = gpsDeviceService.getById(id);
            if (device == null) {
                return Result.error("设备不存在");
            }
            
            GpsLocation location = gpsDataService.getLatestLocationByMacid(device.getMacid());
            return Result.success(location);
        } catch (Exception e) {
            log.error("获取设备位置失败", e);
            return Result.error("获取设备位置失败: " + e.getMessage());
        }
    }

    @GetMapping("/elderly/{elderlyId}/location")
    @Operation(summary = "获取老人最新位置", description = "获取指定老人的最新GPS位置")
    public Result<GpsLocation> getElderlyLocation(@PathVariable Long elderlyId) {
        try {
            GpsLocation location = gpsDataService.getLatestLocationByElderlyId(elderlyId);
            return Result.success(location);
        } catch (Exception e) {
            log.error("获取老人位置失败", e);
            return Result.error("获取老人位置失败: " + e.getMessage());
        }
    }

    @GetMapping("/elderly/{elderlyId}/history")
    @Operation(summary = "获取老人位置历史", description = "获取指定老人的GPS位置历史记录")
    public Result<List<GpsLocation>> getElderlyLocationHistory(
            @PathVariable Long elderlyId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        try {
            List<GpsLocation> history = gpsDataService.getLocationHistory(elderlyId, startTime, endTime);
            return Result.success(history);
        } catch (Exception e) {
            log.error("获取位置历史失败", e);
            return Result.error("获取位置历史失败: " + e.getMessage());
        }
    }
}