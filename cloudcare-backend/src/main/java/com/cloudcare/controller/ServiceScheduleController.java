package com.cloudcare.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.entity.ServiceSchedule;
import com.cloudcare.service.ServiceScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务调度控制器
 *
 * @author CloudCare
 */
@Slf4j
@RestController
@RequestMapping("/service-schedule")
@RequiredArgsConstructor
@Tag(name = "服务调度管理", description = "养老服务调度管理相关接口")
public class ServiceScheduleController {

    private final ServiceScheduleService serviceScheduleService;

    /**
     * 分页查询服务调度列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询服务调度列表", description = "根据条件分页查询服务调度列表")
    public Result<Page<ServiceSchedule>> getSchedulePage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "服务名称") @RequestParam(required = false) String serviceName,
            @Parameter(description = "服务类型") @RequestParam(required = false) Integer serviceType,
            @Parameter(description = "老人ID") @RequestParam(required = false) Long elderId,
            @Parameter(description = "服务人员ID") @RequestParam(required = false) Long staffId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endTime) {
        
        // 将日期转换为当天的开始和结束时间（如果提供了日期参数）
        LocalDateTime actualStartTime = startTime != null ? startTime.toLocalDate().atStartOfDay() : null;
        LocalDateTime actualEndTime = endTime != null ? endTime.toLocalDate().atTime(23, 59, 59) : null;
        
        Page<ServiceSchedule> page = serviceScheduleService.getSchedulePage(
                current, size, serviceName, serviceType, elderId, staffId, status, actualStartTime, actualEndTime);
        return Result.success(page);
    }

    /**
     * 根据ID查询服务调度详情
     */
    @GetMapping("/{scheduleId}")
    @Operation(summary = "查询服务调度详情", description = "根据ID查询服务调度详情")
    public Result<ServiceSchedule> getScheduleById(
            @Parameter(description = "调度ID") @PathVariable Long scheduleId) {
        ServiceSchedule schedule = serviceScheduleService.getById(scheduleId);
        return Result.success(schedule);
    }

    /**
     * 创建服务调度
     */
    @PostMapping
    @Operation(summary = "创建服务调度", description = "创建新的服务调度")
    public Result<Boolean> createSchedule(@RequestBody ServiceSchedule serviceSchedule) {
        boolean result = serviceScheduleService.createSchedule(serviceSchedule);
        return Result.success(result);
    }

    /**
     * 更新服务调度
     */
    @PutMapping("/{scheduleId}")
    @Operation(summary = "更新服务调度", description = "更新服务调度信息")
    public Result<Boolean> updateSchedule(
            @Parameter(description = "调度ID") @PathVariable Long scheduleId,
            @RequestBody ServiceSchedule serviceSchedule) {
        serviceSchedule.setScheduleId(scheduleId);
        boolean result = serviceScheduleService.updateSchedule(serviceSchedule);
        return Result.success(result);
    }

    /**
     * 删除服务调度
     */
    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "删除服务调度", description = "删除服务调度")
    public Result<Boolean> deleteSchedule(
            @Parameter(description = "调度ID") @PathVariable Long scheduleId) {
        boolean result = serviceScheduleService.deleteSchedule(scheduleId);
        return Result.success(result);
    }

    /**
     * 根据老人ID查询服务调度列表
     */
    @GetMapping("/elder/{elderId}")
    @Operation(summary = "查询老人的服务调度", description = "根据老人ID查询服务调度列表")
    public Result<List<ServiceSchedule>> getSchedulesByElderId(
            @Parameter(description = "老人ID") @PathVariable Long elderId) {
        List<ServiceSchedule> schedules = serviceScheduleService.getSchedulesByElderId(elderId);
        return Result.success(schedules);
    }

    /**
     * 根据服务人员ID查询服务调度列表
     */
    @GetMapping("/staff/{staffId}")
    @Operation(summary = "查询服务人员的调度", description = "根据服务人员ID查询服务调度列表")
    public Result<List<ServiceSchedule>> getSchedulesByStaffId(
            @Parameter(description = "服务人员ID") @PathVariable Long staffId) {
        List<ServiceSchedule> schedules = serviceScheduleService.getSchedulesByStaffId(staffId);
        return Result.success(schedules);
    }

    /**
     * 根据时间范围查询服务调度列表
     */
    @GetMapping("/time-range")
    @Operation(summary = "按时间范围查询调度", description = "根据时间范围查询服务调度列表")
    public Result<List<ServiceSchedule>> getSchedulesByTimeRange(
            @Parameter(description = "开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endTime) {
        // 将日期转换为当天的开始和结束时间
        LocalDateTime actualStartTime = startTime.toLocalDate().atStartOfDay();
        LocalDateTime actualEndTime = endTime.toLocalDate().atTime(23, 59, 59);
        
        List<ServiceSchedule> schedules = serviceScheduleService.getSchedulesByTimeRange(actualStartTime, actualEndTime);
        return Result.success(schedules);
    }

    /**
     * 根据状态查询服务调度列表
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "按状态查询调度", description = "根据状态查询服务调度列表")
    public Result<List<ServiceSchedule>> getSchedulesByStatus(
            @Parameter(description = "状态") @PathVariable Integer status) {
        List<ServiceSchedule> schedules = serviceScheduleService.getSchedulesByStatus(status);
        return Result.success(schedules);
    }

    /**
     * 开始执行服务
     */
    @PutMapping("/{scheduleId}/start")
    @Operation(summary = "开始执行服务", description = "开始执行服务调度")
    public Result<Boolean> startService(
            @Parameter(description = "调度ID") @PathVariable Long scheduleId) {
        boolean result = serviceScheduleService.startService(scheduleId);
        return Result.success(result);
    }

    /**
     * 完成服务
     */
    @PutMapping("/{scheduleId}/complete")
    @Operation(summary = "完成服务", description = "完成服务调度")
    public Result<Boolean> completeService(
            @Parameter(description = "调度ID") @PathVariable Long scheduleId,
            @Parameter(description = "服务备注") @RequestParam(required = false) String remark) {
        boolean result = serviceScheduleService.completeService(scheduleId, remark);
        return Result.success(result);
    }

    /**
     * 取消服务
     */
    @PutMapping("/{scheduleId}/cancel")
    @Operation(summary = "取消服务", description = "取消服务调度")
    public Result<Boolean> cancelService(
            @Parameter(description = "调度ID") @PathVariable Long scheduleId,
            @Parameter(description = "取消原因") @RequestParam(required = false) String remark) {
        boolean result = serviceScheduleService.cancelService(scheduleId, remark);
        return Result.success(result);
    }

    /**
     * 获取今日待执行的服务调度列表
     */
    @GetMapping("/today/pending")
    @Operation(summary = "今日待执行调度", description = "获取今日待执行的服务调度列表")
    public Result<List<ServiceSchedule>> getTodayPendingSchedules() {
        List<ServiceSchedule> schedules = serviceScheduleService.getTodayPendingSchedules();
        return Result.success(schedules);
    }

    /**
     * 获取紧急服务调度列表
     */
    @GetMapping("/urgent")
    @Operation(summary = "紧急服务调度", description = "获取紧急服务调度列表")
    public Result<List<ServiceSchedule>> getUrgentSchedules() {
        List<ServiceSchedule> schedules = serviceScheduleService.getUrgentSchedules();
        return Result.success(schedules);
    }
}