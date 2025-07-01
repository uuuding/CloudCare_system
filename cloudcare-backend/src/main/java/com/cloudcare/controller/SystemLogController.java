package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.entity.SystemLog;
import com.cloudcare.service.SystemLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 系统日志控制器
 *
 * @author cloudcare
 */
@Slf4j
@RestController
@RequestMapping("/system-log")
@Tag(name = "系统日志管理", description = "系统日志相关接口")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("/list")
    @Operation(summary = "分页查询系统日志", description = "分页查询系统日志列表")
    public Result<IPage<SystemLog>> getLogList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "日志级别") @RequestParam(required = false) String level,
            @Parameter(description = "模块名称") @RequestParam(required = false) String module,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        
        try {
            Page<SystemLog> pageParam = new Page<>(page, size);
            IPage<SystemLog> result = systemLogService.getLogPage(pageParam, level, module, username, keyword, startTime, endTime);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询系统日志失败", e);
            return Result.error("查询系统日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "获取日志统计信息", description = "获取系统日志统计数据")
    public Result<Map<String, Object>> getLogStats() {
        try {
            Map<String, Object> stats = systemLogService.getLogStats();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取日志统计信息失败", e);
            return Result.error("获取日志统计信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清空所有日志", description = "清空系统中的所有日志记录")
    public Result<Void> clearAllLogs() {
        try {
            boolean success = systemLogService.clearAllLogs();
            if (success) {
                // 记录清空日志的操作
                systemLogService.saveLog("INFO", "系统日志", "清空日志", "清空所有系统日志");
                return Result.success();
            } else {
                return Result.error("清空日志失败");
            }
        } catch (Exception e) {
            log.error("清空日志失败", e);
            return Result.error("清空日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/levels")
    @Operation(summary = "获取日志级别选项", description = "获取可用的日志级别列表")
    public Result<List<String>> getLogLevels() {
        try {
            List<String> levels = systemLogService.getLogLevels();
            return Result.success(levels);
        } catch (Exception e) {
            log.error("获取日志级别选项失败", e);
            return Result.error("获取日志级别选项失败: " + e.getMessage());
        }
    }

    @GetMapping("/modules")
    @Operation(summary = "获取模块选项", description = "获取可用的模块列表")
    public Result<List<String>> getLogModules() {
        try {
            List<String> modules = systemLogService.getLogModules();
            return Result.success(modules);
        } catch (Exception e) {
            log.error("获取模块选项失败", e);
            return Result.error("获取模块选项失败: " + e.getMessage());
        }
    }

    @GetMapping("/recent")
    @Operation(summary = "获取最近日志", description = "获取最近的日志记录")
    public Result<List<SystemLog>> getRecentLogs(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<SystemLog> logs = systemLogService.getRecentLogs(limit);
            return Result.success(logs);
        } catch (Exception e) {
            log.error("获取最近日志失败", e);
            return Result.error("获取最近日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/trend")
    @Operation(summary = "获取日志趋势数据", description = "获取指定天数内的日志趋势统计")
    public Result<List<Map<String, Object>>> getLogTrend(
            @Parameter(description = "天数") @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<Map<String, Object>> trend = systemLogService.getLogTrend(days);
            return Result.success(trend);
        } catch (Exception e) {
            log.error("获取日志趋势数据失败", e);
            return Result.error("获取日志趋势数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/business")
    @Operation(summary = "根据业务ID查询日志", description = "根据业务ID和类型查询相关日志")
    public Result<List<SystemLog>> getLogsByBusiness(
            @Parameter(description = "业务ID") @RequestParam String businessId,
            @Parameter(description = "业务类型") @RequestParam(required = false) String businessType) {
        try {
            List<SystemLog> logs = systemLogService.getLogsByBusiness(businessId, businessType);
            return Result.success(logs);
        } catch (Exception e) {
            log.error("根据业务ID查询日志失败", e);
            return Result.error("根据业务ID查询日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "获取日志详情", description = "根据ID获取日志详细信息")
    public Result<SystemLog> getLogDetail(@Parameter(description = "日志ID") @PathVariable Long id) {
        try {
            SystemLog log = systemLogService.getById(id);
            if (log != null) {
                return Result.success(log);
            } else {
                return Result.error("日志不存在");
            }
        } catch (Exception e) {
            log.error("获取日志详情失败", e);
            return Result.error("获取日志详情失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除单条日志", description = "根据ID删除指定的日志记录")
    public Result<Void> deleteLog(@Parameter(description = "日志ID") @PathVariable Long id) {
        try {
            SystemLog logToDelete = systemLogService.getById(id);
            if (logToDelete == null) {
                return Result.error("日志不存在");
            }
            
            boolean success = systemLogService.removeById(id);
            if (success) {
                // 记录删除日志的操作
                systemLogService.saveLog("INFO", "系统日志", "删除日志", 
                    "删除日志ID: " + id + ", 内容: " + logToDelete.getContent());
                return Result.success();
            } else {
                return Result.error("删除日志失败");
            }
        } catch (Exception e) {
            log.error("删除日志失败", e);
            return Result.error("删除日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @Operation(summary = "导出日志数据", description = "根据条件导出日志数据")
    public Result<List<SystemLog>> exportLogs(
            @Parameter(description = "日志级别") @RequestParam(required = false) String level,
            @Parameter(description = "模块名称") @RequestParam(required = false) String module,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        
        try {
            List<SystemLog> logs = systemLogService.exportLogs(level, module, username, keyword, startTime, endTime);
            // 记录导出操作
            systemLogService.saveLog("INFO", "系统日志", "导出日志", 
                "导出日志数据，条件: 级别=" + level + ", 模块=" + module + ", 用户=" + username);
            return Result.success(logs);
        } catch (Exception e) {
            log.error("导出日志数据失败", e);
            return Result.error("导出日志数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @Operation(summary = "手动记录日志", description = "手动添加一条系统日志记录")
    public Result<Void> saveLog(@RequestBody SystemLog systemLog) {
        try {
            systemLogService.saveLog(systemLog);
            return Result.success();
        } catch (Exception e) {
            log.error("记录日志失败", e);
            return Result.error("记录日志失败: " + e.getMessage());
        }
    }

}