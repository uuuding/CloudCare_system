package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudcare.entity.InterventionExecution;
import com.cloudcare.service.InterventionExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 干预执行记录控制器
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/api/intervention-execution")
@CrossOrigin(origins = "*")
public class InterventionExecutionController {

    @Autowired
    private InterventionExecutionService interventionExecutionService;

    /**
     * 分页查询干预执行记录
     */
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getInterventionExecutionPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long planId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            IPage<InterventionExecution> page = interventionExecutionService.getExecutionPage(
                    current, size, planId, status, startDate, endDate);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", page);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询干预执行记录分页失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据ID查询干预执行记录详情
     */
    @GetMapping("/{executionId}")
    public ResponseEntity<Map<String, Object>> getInterventionExecutionById(@PathVariable Long executionId) {
        try {
            Map<String, Object> detail = interventionExecutionService.getExecutionDetail(executionId);
            
            Map<String, Object> result = new HashMap<>();
            if (detail != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", detail);
            } else {
                result.put("code", 404);
                result.put("message", "执行记录不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询干预执行记录详情失败，记录ID: {}", executionId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 创建干预执行记录
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createInterventionExecution(@RequestBody InterventionExecution interventionExecution) {
        try {
            boolean success = interventionExecutionService.createExecution(interventionExecution);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "创建成功");
                result.put("data", interventionExecution);
            } else {
                result.put("code", 500);
                result.put("message", "创建失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("创建干预执行记录失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 更新干预执行记录
     */
    @PutMapping("/{executionId}")
    public ResponseEntity<Map<String, Object>> updateInterventionExecution(
            @PathVariable Long executionId, @RequestBody InterventionExecution interventionExecution) {
        try {
            interventionExecution.setExecutionId(executionId);
            boolean success = interventionExecutionService.updateById(interventionExecution);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
                result.put("data", interventionExecution);
            } else {
                result.put("code", 500);
                result.put("message", "更新失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("更新干预执行记录失败，记录ID: {}", executionId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 删除干预执行记录
     */
    @DeleteMapping("/{executionId}")
    public ResponseEntity<Map<String, Object>> deleteInterventionExecution(@PathVariable Long executionId) {
        try {
            boolean success = interventionExecutionService.removeById(executionId);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 500);
                result.put("message", "删除失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("删除干预执行记录失败，记录ID: {}", executionId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据方案ID查询执行记录
     */
    @GetMapping("/plan/{planId}")
    public ResponseEntity<Map<String, Object>> getExecutionsByPlanId(@PathVariable Long planId) {
        try {
            List<InterventionExecution> executions = interventionExecutionService.getExecutionsByPlanId(planId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", executions);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询方案执行记录失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询最近的执行记录
     */
    @GetMapping("/plan/{planId}/recent")
    public ResponseEntity<Map<String, Object>> getRecentExecutionByPlanId(@PathVariable Long planId) {
        try {
            List<InterventionExecution> executions = interventionExecutionService.getRecentExecutions(planId, 1);
            InterventionExecution execution = executions.isEmpty() ? null : executions.get(0);
            
            Map<String, Object> result = new HashMap<>();
            if (execution != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", execution);
            } else {
                result.put("code", 404);
                result.put("message", "暂无执行记录");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询最近执行记录失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 完成干预执行
     */
    @PostMapping("/{executionId}/complete")
    public ResponseEntity<Map<String, Object>> completeInterventionExecution(
            @PathVariable Long executionId,
            @RequestParam BigDecimal completionRate,
            @RequestParam(required = false) String executionDetails,
            @RequestParam(required = false) String sideEffects,
            @RequestParam(required = false) String patientFeedback,
            @RequestParam(required = false) BigDecimal effectivenessRating) {
        try {
            boolean success = interventionExecutionService.completeExecution(
                    executionId, executionDetails, patientFeedback, effectivenessRating != null ? effectivenessRating.intValue() : null);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "完成成功");
            } else {
                result.put("code", 500);
                result.put("message", "完成失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("完成干预执行失败，记录ID: {}", executionId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "完成失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 标记干预执行为未完成
     */
    @PostMapping("/{executionId}/incomplete")
    public ResponseEntity<Map<String, Object>> markInterventionExecutionIncomplete(
            @PathVariable Long executionId, @RequestParam String reason) {
        try {
            boolean success = interventionExecutionService.markExecutionMissed(executionId, reason);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "标记成功");
            } else {
                result.put("code", 500);
                result.put("message", "标记失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("标记干预执行未完成失败，记录ID: {}", executionId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "标记失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 统计执行记录按状态分组
     */
    @GetMapping("/statistics/status")
    public ResponseEntity<Map<String, Object>> getCountByStatus(
            @RequestParam(required = false) Long planId) {
        try {
            List<Map<String, Object>> statistics = interventionExecutionService.getCountByStatus(planId, null, null);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", statistics);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询执行状态统计失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 计算平均完成率
     */
    @GetMapping("/statistics/completion-rate")
    public ResponseEntity<Map<String, Object>> getAverageCompletionRate(
            @RequestParam(required = false) Long planId) {
        try {
            Double avgRate = interventionExecutionService.calculateAverageCompletionRate(planId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", avgRate);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询平均完成率失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 计算平均效果评分
     */
    @GetMapping("/statistics/effectiveness-rating")
    public ResponseEntity<Map<String, Object>> getAverageEffectivenessRating(
            @RequestParam(required = false) Long planId) {
        try {
            Double avgRating = interventionExecutionService.calculateAverageEffectivenessRating(planId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", avgRating);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询平均效果评分失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询指定日期范围内的执行统计
     */
    @GetMapping("/statistics/date-range")
    public ResponseEntity<Map<String, Object>> getExecutionStatisticsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Long planId) {
        try {
            List<Map<String, Object>> statistics = interventionExecutionService.getExecutionStatistics(startDate, endDate);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", statistics);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询日期范围执行统计失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询今日待执行的干预记录
     */
    @GetMapping("/today/pending")
    public ResponseEntity<Map<String, Object>> getTodayPendingExecutions() {
        try {
            List<Map<String, Object>> executions = interventionExecutionService.getTodayPendingExecutions();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", executions);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询今日待执行记录失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询逾期未执行的干预记录
     */
    @GetMapping("/overdue")
    public ResponseEntity<Map<String, Object>> getOverdueExecutions() {
        try {
            List<Map<String, Object>> executions = interventionExecutionService.getOverdueExecutions();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", executions);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询逾期执行记录失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 批量创建执行计划
     */
    @PostMapping("/batch-create")
    public ResponseEntity<Map<String, Object>> batchCreateExecutionPlan(
            @RequestParam Long planId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam String frequency) {
        try {
            boolean success = interventionExecutionService.createExecutionSchedule(planId, startDate, endDate, frequency);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "批量创建成功");
            } else {
                result.put("code", 500);
                result.put("message", "批量创建失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("批量创建执行计划失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "批量创建失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 更新生命体征
     */
    @PostMapping("/{executionId}/vital-signs")
    public ResponseEntity<Map<String, Object>> updateVitalSigns(
            @PathVariable Long executionId,
            @RequestParam(required = false) String preVitalSigns,
            @RequestParam(required = false) String postVitalSigns) {
        try {
            boolean success = interventionExecutionService.updateVitalSigns(executionId, preVitalSigns, postVitalSigns);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
            } else {
                result.put("code", 500);
                result.put("message", "更新失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("更新生命体征失败，记录ID: {}", executionId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}