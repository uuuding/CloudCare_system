package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.common.annotation.Log;
import com.cloudcare.common.enums.BusinessType;
import com.cloudcare.entity.HealthAlert;
import com.cloudcare.entity.HealthAlertRule;
import com.cloudcare.service.HealthAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 健康预警控制器
 */
@Slf4j
@RestController
@RequestMapping("/health-alert")
@CrossOrigin(origins = "*")
public class HealthAlertController {
    
    @Autowired
    private HealthAlertService healthAlertService;
    
    /**
     * 获取所有预警记录
     */
    @GetMapping("/all")
    public Result<List<HealthAlert>> getAllAlerts() {
        try {
            List<HealthAlert> alerts = healthAlertService.getAllAlerts();
            return Result.success(alerts);
        } catch (Exception e) {
            log.error("获取预警记录失败", e);
            return Result.error("获取预警记录失败");
        }
    }
    
    /**
     * 根据老人ID获取预警记录
     */
    @GetMapping("/elderly/{elderlyId}")
    public Result<List<HealthAlert>> getAlertsByElderlyId(@PathVariable String elderlyId) {
        try {
            // 参数验证和转换
            if (elderlyId == null || elderlyId.trim().isEmpty() || "undefined".equals(elderlyId) || "null".equals(elderlyId)) {
                return Result.error("老人ID不能为空");
            }
            
            Long elderlyIdLong;
            try {
                elderlyIdLong = Long.parseLong(elderlyId.trim());
            } catch (NumberFormatException e) {
                log.error("老人ID格式错误: {}", elderlyId);
                return Result.error("老人ID格式错误，请输入有效的数字");
            }
            
            if (elderlyIdLong <= 0) {
                return Result.error("老人ID必须大于0");
            }
            
            List<HealthAlert> alerts = healthAlertService.getAlertsByElderlyId(elderlyIdLong);
            return Result.success(alerts);
        } catch (Exception e) {
            log.error("根据老人ID获取预警记录失败，elderlyId: {}, 错误信息: {}", elderlyId, e.getMessage(), e);
            return Result.error("获取预警记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据预警状态获取预警记录
     */
    @GetMapping("/status/{status}")
    public Result<List<HealthAlert>> getAlertsByStatus(@PathVariable String status) {
        try {
            List<HealthAlert> alerts = healthAlertService.getAlertsByStatus(status);
            return Result.success(alerts);
        } catch (Exception e) {
            log.error("根据状态获取预警记录失败", e);
            return Result.error("获取预警记录失败");
        }
    }
    
    /**
     * 根据预警级别获取预警记录
     */
    @GetMapping("/level/{alertLevel}")
    public Result<List<HealthAlert>> getAlertsByLevel(@PathVariable String alertLevel) {
        try {
            List<HealthAlert> alerts = healthAlertService.getAlertsByLevel(alertLevel);
            return Result.success(alerts);
        } catch (Exception e) {
            log.error("根据级别获取预警记录失败", e);
            return Result.error("获取预警记录失败");
        }
    }
    
    /**
     * 根据时间范围获取预警记录
     */
    @GetMapping("/time-range")
    public Result<List<HealthAlert>> getAlertsByTimeRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        try {
            List<HealthAlert> alerts = healthAlertService.getAlertsByTimeRange(startTime, endTime);
            return Result.success(alerts);
        } catch (Exception e) {
            log.error("根据时间范围获取预警记录失败", e);
            return Result.error("获取预警记录失败");
        }
    }
    
    /**
     * 根据预警类型获取预警记录
     */
    @GetMapping("/type/{alertType}")
    public Result<List<HealthAlert>> getAlertsByType(@PathVariable String alertType) {
        try {
            List<HealthAlert> alerts = healthAlertService.getAlertsByType(alertType);
            return Result.success(alerts);
        } catch (Exception e) {
            log.error("根据类型获取预警记录失败", e);
            return Result.error("获取预警记录失败");
        }
    }
    
    /**
     * 处理预警（标记为已解决）
     */
    @PutMapping("/resolve/{alertId}")
    @Log(title = "HEALTH", businessType = BusinessType.UPDATE, isSaveRequestData = true, isSaveResponseData = true)
    public Result<String> resolveAlert(@PathVariable Long alertId, 
                                     @RequestParam String resolvedBy,
                                     @RequestParam(required = false) String resolvedNote) {
        try {
            boolean success = healthAlertService.resolveAlert(alertId, resolvedBy, resolvedNote);
            if (success) {
                return Result.success("预警处理成功");
            } else {
                return Result.error("预警处理失败");
            }
        } catch (Exception e) {
            log.error("处理预警失败", e);
            return Result.error("处理预警失败");
        }
    }
    
    /**
     * 忽略预警
     */
    @PutMapping("/ignore/{alertId}")
    public Result<String> ignoreAlert(@PathVariable Long alertId, 
                                    @RequestParam String resolvedBy,
                                    @RequestParam(required = false) String resolvedNote) {
        try {
            boolean success = healthAlertService.ignoreAlert(alertId, resolvedBy, resolvedNote);
            if (success) {
                return Result.success("预警忽略成功");
            } else {
                return Result.error("预警忽略失败");
            }
        } catch (Exception e) {
            log.error("忽略预警失败", e);
            return Result.error("忽略预警失败");
        }
    }
    
    /**
     * 获取活跃预警数量
     */
    @GetMapping("/active-count")
    public Result<Long> getActiveAlertCount() {
        try {
            Long count = healthAlertService.getActiveAlertCount();
            return Result.success(count);
        } catch (Exception e) {
            log.error("获取活跃预警数量失败", e);
            return Result.error("获取活跃预警数量失败");
        }
    }
    
    /**
     * 获取预警统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getAlertStatistics() {
        try {
            Map<String, Object> statistics = healthAlertService.getAlertStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取预警统计信息失败", e);
            return Result.error("获取预警统计信息失败");
        }
    }
    
    /**
     * 获取所有预警规则
     */
    @GetMapping("/rules")
    public Result<List<HealthAlertRule>> getAllRules() {
        try {
            List<HealthAlertRule> rules = healthAlertService.getAllRules();
            return Result.success(rules);
        } catch (Exception e) {
            log.error("获取预警规则失败", e);
            return Result.error("获取预警规则失败");
        }
    }
    
    /**
     * 根据类型获取启用的预警规则
     */
    @GetMapping("/rules/type/{alertType}")
    public Result<List<HealthAlertRule>> getEnabledRulesByType(@PathVariable String alertType) {
        try {
            List<HealthAlertRule> rules = healthAlertService.getEnabledRulesByType(alertType);
            return Result.success(rules);
        } catch (Exception e) {
            log.error("根据类型获取预警规则失败", e);
            return Result.error("获取预警规则失败");
        }
    }
    
    /**
     * 添加预警规则
     */
    @PostMapping("/rules")
    public Result<String> addRule(@RequestBody HealthAlertRule rule) {
        try {
            boolean success = healthAlertService.addRule(rule);
            if (success) {
                return Result.success("预警规则添加成功");
            } else {
                return Result.error("预警规则添加失败");
            }
        } catch (Exception e) {
            log.error("添加预警规则失败", e);
            return Result.error("添加预警规则失败");
        }
    }
    
    /**
     * 更新预警规则
     */
    @PutMapping("/rules")
    public Result<String> updateRule(@RequestBody HealthAlertRule rule) {
        try {
            boolean success = healthAlertService.updateRule(rule);
            if (success) {
                return Result.success("预警规则更新成功");
            } else {
                return Result.error("预警规则更新失败");
            }
        } catch (Exception e) {
            log.error("更新预警规则失败", e);
            return Result.error("更新预警规则失败");
        }
    }
    
    /**
     * 删除预警规则
     */
    @DeleteMapping("/rules/{ruleId}")
    public Result<String> deleteRule(@PathVariable Long ruleId) {
        try {
            boolean success = healthAlertService.deleteRule(ruleId);
            if (success) {
                return Result.success("预警规则删除成功");
            } else {
                return Result.error("预警规则删除失败");
            }
        } catch (Exception e) {
            log.error("删除预警规则失败", e);
            return Result.error("删除预警规则失败");
        }
    }
    
    /**
     * 启用/禁用预警规则
     */
    @PutMapping("/rules/{ruleId}/toggle")
    public Result<String> toggleRuleStatus(@PathVariable Long ruleId, @RequestParam Boolean enabled) {
        try {
            boolean success = healthAlertService.toggleRuleStatus(ruleId, enabled);
            if (success) {
                return Result.success("预警规则状态更新成功");
            } else {
                return Result.error("预警规则状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新预警规则状态失败", e);
            return Result.error("更新预警规则状态失败");
        }
    }
    
    /**
     * 初始化默认预警规则
     */
    @PostMapping("/rules/init")
    public Result<String> initializeDefaultRules() {
        try {
            healthAlertService.initializeDefaultRules();
            return Result.success("默认预警规则初始化成功");
        } catch (Exception e) {
            log.error("初始化默认预警规则失败", e);
            return Result.error("初始化默认预警规则失败");
        }
    }
    
    /**
     * 根据观察记录ID触发预警检查
     */
    @PostMapping("/check/{observationId}")
    public Result<String> checkAndGenerateAlerts(@PathVariable Integer observationId) {
        try {
            healthAlertService.checkAndGenerateAlertsByObservationId(observationId);
            return Result.success("预警检查完成");
        } catch (Exception e) {
            log.error("预警检查失败", e);
            return Result.error("预警检查失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量检查所有未处理的观察记录
     */
    @PostMapping("/check/batch")
    public Result<String> batchCheckAlerts() {
        try {
            int processedCount = healthAlertService.batchCheckAllObservations();
            return Result.success("批量预警检查完成，处理了 " + processedCount + " 条观察记录");
        } catch (Exception e) {
            log.error("批量预警检查失败", e);
            return Result.error("批量预警检查失败: " + e.getMessage());
        }
    }
}