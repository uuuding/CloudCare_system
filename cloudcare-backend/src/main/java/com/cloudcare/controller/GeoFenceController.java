package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GeoFenceEvent;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.service.GeoFenceEventService;
import com.cloudcare.service.GeoFenceService;
import com.cloudcare.service.GpsLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电子围栏管理控制器
 * 提供电子围栏的创建、更新、删除、查询等管理功能
 * 支持圆形和多边形围栏，提供围栏事件查询和统计分析
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/geo-fence")
@RequiredArgsConstructor
public class GeoFenceController {

    private final GeoFenceService geoFenceService;
    private final GeoFenceEventService geoFenceEventService;
    private final GpsLocationService gpsLocationService;

    /**
     * 创建电子围栏
     * 支持创建圆形和多边形围栏，自动设置创建时间和创建者
     * 
     * @param geoFence 围栏信息，包含围栏名称、类型、坐标、老人ID等
     * @return 创建结果，true-成功，false-失败
     * @see GeoFence
     * @see GeoFenceService#createGeoFence(GeoFence)
     */
    @PostMapping("/create")
    public Result<Boolean> createGeoFence(@RequestBody GeoFence geoFence) {
        try {
            boolean success = geoFenceService.createGeoFence(geoFence);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("创建围栏失败");
            }
        } catch (Exception e) {
            log.error("创建围栏失败: {}", e.getMessage(), e);
            return Result.error("创建围栏失败: " + e.getMessage());
        }
    }

    /**
     * 更新电子围栏
     * 更新围栏的基本信息，如名称、坐标、提醒设置等
     * 
     * @param id 围栏ID
     * @param geoFence 更新的围栏信息
     * @return 更新结果，true-成功，false-失败
     * @see GeoFenceService#updateGeoFence(GeoFence)
     */
    @PutMapping("/update/{id}")
    public Result<Boolean> updateGeoFence(@PathVariable Long id, @RequestBody GeoFence geoFence) {
        try {
            // 设置围栏ID
            geoFence.setId(id);
            boolean success = geoFenceService.updateGeoFence(geoFence);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("更新围栏失败");
            }
        } catch (Exception e) {
            log.error("更新围栏失败: {}", e.getMessage(), e);
            return Result.error("更新围栏失败: " + e.getMessage());
        }
    }

    /**
     * 删除电子围栏
     * 软删除围栏记录，同时删除相关的围栏事件记录
     * 
     * @param fenceId 围栏ID
     * @return 删除结果，true-成功，false-失败
     * @see GeoFenceService#deleteGeoFence(Long)
     */
    @DeleteMapping("/delete/{fenceId}")
    public Result<Boolean> deleteGeoFence(@PathVariable Long fenceId) {
        try {
            boolean success = geoFenceService.deleteGeoFence(fenceId);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("删除围栏失败");
            }
        } catch (Exception e) {
            log.error("删除围栏失败: {}", e.getMessage(), e);
            return Result.error("删除围栏失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有围栏（分页）
     * 分页查询系统中的所有围栏，用于管理界面展示
     * 
     * @param page 页码，从1开始，默认为1
     * @param size 每页大小，默认为10
     * @return 分页结果，包含围栏列表、总数、页码等信息
     * @see GeoFenceService#getAllFences()
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getAllFences(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            // 简单实现，实际项目中应该使用分页插件
            List<GeoFence> allFences = geoFenceService.getAllFences();
            
            int total = allFences.size();
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, total);
            
            List<GeoFence> pagedFences = startIndex < total ? 
                allFences.subList(startIndex, endIndex) : List.of();
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", pagedFences);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            log.info(">>> 成功进入 getAllFences()");


            return Result.success(result);
        } catch (Exception e) {
            log.error("查询围栏列表失败: {}", e.getMessage(), e);
            return Result.error("查询围栏列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据老人ID查询所有围栏
     * 查询指定老人的所有围栏（包括启用和禁用的）
     * 
     * @param elderlyId 老人ID
     * @return 该老人的所有围栏列表
     * @see GeoFenceService#getAllFencesByElderlyId(Integer)
     */
    @GetMapping("/list/{elderlyId}")
    public Result<List<GeoFence>> getFencesByElderlyId(@PathVariable Integer elderlyId) {
        try {
            List<GeoFence> fences = geoFenceService.getAllFencesByElderlyId(elderlyId);
            return Result.success(fences);
        } catch (Exception e) {
            log.error("查询围栏列表失败: {}", e.getMessage(), e);
            return Result.error("查询围栏列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据老人ID查询启用的围栏
     * 查询指定老人当前启用的围栏，用于围栏检测
     * 
     * @param elderlyId 老人ID
     * @return 该老人的启用围栏列表
     * @see GeoFenceService#getActiveFencesByElderlyId(Integer)
     */
    @GetMapping("/active/{elderlyId}")
    public Result<List<GeoFence>> getActiveFencesByElderlyId(@PathVariable Integer elderlyId) {
        try {
            List<GeoFence> fences = geoFenceService.getActiveFencesByElderlyId(elderlyId);
            return Result.success(fences);
        } catch (Exception e) {
            log.error("查询启用围栏列表失败: {}", e.getMessage(), e);
            return Result.error("查询启用围栏列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据围栏ID查询围栏详情
     * 获取指定围栏的完整信息
     * 
     * @param fenceId 围栏ID
     * @return 围栏详细信息，不存在时返回错误
     * @see GeoFenceService#getFenceById(Long)
     */
    @GetMapping("/detail/{fenceId}")
    public Result<GeoFence> getFenceById(@PathVariable Long fenceId) {
        try {
            GeoFence fence = geoFenceService.getFenceById(fenceId);
            if (fence != null) {
                return Result.success(fence);
            } else {
                return Result.error("围栏不存在");
            }
        } catch (Exception e) {
            log.error("查询围栏详情失败: {}", e.getMessage(), e);
            return Result.error("查询围栏详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据围栏ID查询围栏详情（兼容前端调用）
     * 提供与detail接口相同的功能，用于兼容不同的前端路由
     * 
     * @param id 围栏ID
     * @return 围栏详细信息
     */
    @GetMapping("/{id}")
    public Result<GeoFence> getFenceByIdCompat(@PathVariable Long id) {
        try {
            GeoFence fence = geoFenceService.getFenceById(id);
            if (fence != null) {
                return Result.success(fence);
            } else {
                return Result.error("围栏不存在");
            }
        } catch (Exception e) {
            log.error("查询围栏详情失败: {}", e.getMessage(), e);
            return Result.error("查询围栏详情失败: " + e.getMessage());
        }
    }

    /**
     * 更新围栏状态
     * 启用或禁用指定的围栏
     * 
     * @param fenceId 围栏ID
     * @param status 围栏状态，1-启用，0-禁用
     * @return 更新结果，true-成功，false-失败
     * @see GeoFenceService#updateFenceStatus(Long, Integer)
     */
    @PutMapping("/status/{fenceId}")
    public Result<Boolean> updateFenceStatus(@PathVariable Long fenceId, @RequestParam Integer status) {
        try {
            boolean success = geoFenceService.updateFenceStatus(fenceId, status);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("更新围栏状态失败");
            }
        } catch (Exception e) {
            log.error("更新围栏状态失败: {}", e.getMessage(), e);
            return Result.error("更新围栏状态失败: " + e.getMessage());
        }
    }

    /**
     * 查询围栏事件记录
     */
    @GetMapping("/events/{elderlyId}")
    public Result<List<GeoFenceEvent>> getFenceEventsByElderlyId(@PathVariable Integer elderlyId) {
        try {
            List<GeoFenceEvent> events = geoFenceEventService.getEventsByElderlyId(elderlyId);
            return Result.success(events);
        } catch (Exception e) {
            log.error("查询围栏事件记录失败: {}", e.getMessage(), e);
            return Result.error("查询围栏事件记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据时间范围查询围栏事件记录
     */
    @GetMapping("/events/{elderlyId}/range")
    public Result<List<GeoFenceEvent>> getFenceEventsByTimeRange(
            @PathVariable Integer elderlyId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        try {
            List<GeoFenceEvent> events = geoFenceEventService.getEventsByElderlyIdAndTimeRange(elderlyId, startTime, endTime);
            return Result.success(events);
        } catch (Exception e) {
            log.error("查询围栏事件记录失败: {}", e.getMessage(), e);
            return Result.error("查询围栏事件记录失败: " + e.getMessage());
        }
    }

    /**
     * 查询最近的围栏事件
     */
    @GetMapping("/events/recent")
    public Result<List<com.cloudcare.dto.GeoFenceEventDTO>> getRecentFenceEvents(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "false") Boolean unread) {
        try {
            List<com.cloudcare.dto.GeoFenceEventDTO> events;
            if (unread) {
                events = geoFenceEventService.getUnreadEventsWithElderlyName(limit);
            } else {
                events = geoFenceEventService.getRecentEventsWithElderlyName(limit);
            }
            return Result.success(events);
        } catch (Exception e) {
            log.error("查询最近围栏事件失败: {}", e.getMessage(), e);
            return Result.error("查询最近围栏事件失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询所有围栏事件（支持筛选）
     */
    @GetMapping("/events/all")
    public Result<Map<String, Object>> getAllFenceEvents(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer elderlyId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        try {
            Map<String, Object> result = geoFenceEventService.getAllEventsWithPagination(
                page, size, elderlyId, eventType, startTime, endTime);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询所有围栏事件失败: {}", e.getMessage(), e);
            return Result.error("查询所有围栏事件失败: " + e.getMessage());
        }
    }

    /**
     * 标记围栏事件为已读
     */
    @PostMapping("/events/{eventId}/read")
    public Result<Boolean> markEventAsRead(@PathVariable Long eventId) {
        try {
            boolean success = geoFenceEventService.markEventAsRead(eventId);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("标记已读失败");
            }
        } catch (Exception e) {
            log.error("标记事件已读失败: {}", e.getMessage(), e);
            return Result.error("标记事件已读失败: " + e.getMessage());
        }
    }

    /**
     * 获取老人当前位置
     */
    @GetMapping("/location/{elderlyId}")
    public Result<GpsLocation> getCurrentLocation(@PathVariable Integer elderlyId) {
        try {
            GpsLocation location = gpsLocationService.getLatestLocationByElderlyId(elderlyId);
            if (location != null) {
                return Result.success(location);
            } else {
                return Result.error("未找到位置信息");
            }
        } catch (Exception e) {
            log.error("查询当前位置失败: {}", e.getMessage(), e);
            return Result.error("查询当前位置失败: " + e.getMessage());
        }
    }

    /**
     * 获取围栏统计信息
     */
    @GetMapping("/statistics/{elderlyId}")
    public Result<Map<String, Object>> getFenceStatistics(@PathVariable Integer elderlyId) {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 统计围栏数量
            List<GeoFence> allFences = geoFenceService.getAllFencesByElderlyId(elderlyId);
            List<GeoFence> activeFences = geoFenceService.getActiveFencesByElderlyId(elderlyId);
            
            statistics.put("totalFences", allFences.size());
            statistics.put("activeFences", activeFences.size());
            
            // 统计今日事件数量
            LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime todayEnd = todayStart.plusDays(1);
            
            Long todayEvents = geoFenceEventService.countEventsByTimeRange(todayStart, todayEnd);
            Long todayEnterEvents = geoFenceEventService.countEventsByTypeAndTimeRange("enter", todayStart, todayEnd);
            Long todayExitEvents = geoFenceEventService.countEventsByTypeAndTimeRange("exit", todayStart, todayEnd);
            
            statistics.put("todayEvents", todayEvents);
            statistics.put("todayEnterEvents", todayEnterEvents);
            statistics.put("todayExitEvents", todayExitEvents);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("查询围栏统计信息失败: {}", e.getMessage(), e);
            return Result.error("查询围栏统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 手动触发围栏检查（测试用）
     */
    @PostMapping("/check/{elderlyId}")
    public Result<String> manualFenceCheck(@PathVariable Integer elderlyId) {
        try {
            GpsLocation location = gpsLocationService.getLatestLocationByElderlyId(elderlyId);
            if (location != null) {
                geoFenceService.checkFenceEvents(location);
                return Result.success("围栏检查完成");
            } else {
                return Result.error("未找到位置信息");
            }
        } catch (Exception e) {
            log.error("手动围栏检查失败: {}", e.getMessage(), e);
            return Result.error("手动围栏检查失败: " + e.getMessage());
        }
    }
}