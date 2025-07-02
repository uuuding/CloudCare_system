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
     */
    @PutMapping("/update")
    public Result<Boolean> updateGeoFence(@RequestBody GeoFence geoFence) {
        try {
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
     * 更新围栏状态
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
    public Result<List<GeoFenceEvent>> getRecentFenceEvents(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<GeoFenceEvent> events = geoFenceEventService.getRecentEvents(limit);
            return Result.success(events);
        } catch (Exception e) {
            log.error("查询最近围栏事件失败: {}", e.getMessage(), e);
            return Result.error("查询最近围栏事件失败: " + e.getMessage());
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