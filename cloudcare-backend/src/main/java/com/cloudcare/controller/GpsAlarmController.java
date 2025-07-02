package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.entity.GpsAlarm;
import com.cloudcare.service.impl.GpsAlarmServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * GPS报警管理控制器
 *
 * @author CloudCare
 */
@Slf4j
@RestController
@RequestMapping("/api/gps-alarm")
@RequiredArgsConstructor
@Tag(name = "GPS报警管理", description = "GPS报警相关接口")
public class GpsAlarmController {

    private final GpsAlarmServiceImpl gpsAlarmService;

    @GetMapping("/list")
    @Operation(summary = "分页查询报警列表", description = "根据条件分页查询GPS报警记录")
    public Result<IPage<GpsAlarm>> getAlarmList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long elderlyId,
            @RequestParam(required = false) Integer handleStatus,
            @RequestParam(required = false) Integer classify) {
        try {
            Page<GpsAlarm> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<GpsAlarm> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(elderlyId != null, GpsAlarm::getElderlyId, elderlyId)
                   .eq(handleStatus != null, GpsAlarm::getHandleStatus, handleStatus)
                   .eq(classify != null, GpsAlarm::getClassify, classify)
                   .orderByDesc(GpsAlarm::getAlarmTime);
            IPage<GpsAlarm> result = gpsAlarmService.page(page, wrapper);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询报警列表失败", e);
            return Result.error("查询报警列表失败");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取报警详情", description = "根据报警ID获取详细信息")
    public Result<GpsAlarm> getAlarmById(@PathVariable Long id) {
        try {
            GpsAlarm alarm = gpsAlarmService.getById(id);
            if (alarm != null) {
                return Result.success(alarm);
            } else {
                return Result.error("报警记录不存在");
            }
        } catch (Exception e) {
            log.error("查询报警详情失败", e);
            return Result.error("查询报警详情失败");
        }
    }

    @PutMapping("/{id}/handle")
    @Operation(summary = "处理报警", description = "处理GPS报警记录")
    public Result<String> handleAlarm(@PathVariable Long id, @RequestBody HandleAlarmRequest request) {
        try {
            GpsAlarm alarm = new GpsAlarm();
            alarm.setId(id);
            alarm.setHandleStatus(1); // 已处理
            alarm.setHandleBy(request.getHandlePerson());
            alarm.setHandleTime(LocalDateTime.now());
            alarm.setNote(request.getNote());
            
            if (gpsAlarmService.updateById(alarm)) {
                return Result.success("处理报警成功");
            } else {
                return Result.error("处理报警失败");
            }
        } catch (Exception e) {
            log.error("处理报警失败", e);
            return Result.error("处理报警失败: " + e.getMessage());
        }
    }

    @GetMapping("/unhandled/count")
    @Operation(summary = "获取未处理报警数量", description = "获取当前未处理的报警数量")
    public Result<Long> getUnhandledAlarmCount(@RequestParam(required = false) Long elderlyId) {
        try {
            LambdaQueryWrapper<GpsAlarm> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GpsAlarm::getHandleStatus, 0)
                   .eq(elderlyId != null, GpsAlarm::getElderlyId, elderlyId);
            long count = gpsAlarmService.count(wrapper);
            return Result.success(count);
        } catch (Exception e) {
            log.error("获取未处理报警数量失败", e);
            return Result.error("获取未处理报警数量失败");
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取报警统计信息", description = "获取报警统计信息")
    public Result<AlarmStatistics> getAlarmStatistics(@RequestParam(required = false) Long elderlyId) {
        try {
            LambdaQueryWrapper<GpsAlarm> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(elderlyId != null, GpsAlarm::getElderlyId, elderlyId);
            
            long totalCount = gpsAlarmService.count(wrapper);
            
            wrapper.eq(GpsAlarm::getHandleStatus, 0);
            long unhandledCount = gpsAlarmService.count(wrapper);
            
            wrapper.clear();
            wrapper.eq(elderlyId != null, GpsAlarm::getElderlyId, elderlyId)
                   .eq(GpsAlarm::getHandleStatus, 1);
            long handledCount = gpsAlarmService.count(wrapper);
            
            AlarmStatistics statistics = new AlarmStatistics();
            statistics.setTotalCount(totalCount);
            statistics.setUnhandledCount(unhandledCount);
            statistics.setHandledCount(handledCount);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取报警统计信息失败", e);
            return Result.error("获取报警统计信息失败");
        }
    }

    /**
     * 处理报警请求参数
     */
    public static class HandleAlarmRequest {
        private String handlePerson;
        private String note;

        public String getHandlePerson() {
            return handlePerson;
        }

        public void setHandlePerson(String handlePerson) {
            this.handlePerson = handlePerson;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    /**
     * 报警统计信息
     */
    public static class AlarmStatistics {
        private Long totalCount;
        private Long unhandledCount;
        private Long handledCount;

        public Long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }

        public Long getUnhandledCount() {
            return unhandledCount;
        }

        public void setUnhandledCount(Long unhandledCount) {
            this.unhandledCount = unhandledCount;
        }

        public Long getHandledCount() {
            return handledCount;
        }

        public void setHandledCount(Long handledCount) {
            this.handledCount = handledCount;
        }
    }
}