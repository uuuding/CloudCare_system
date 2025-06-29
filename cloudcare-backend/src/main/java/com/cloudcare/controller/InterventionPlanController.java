package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudcare.entity.InterventionPlan;
import com.cloudcare.service.InterventionPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 干预方案控制器
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/api/intervention-plan")
@CrossOrigin(origins = "*")
public class InterventionPlanController {

    @Autowired
    private InterventionPlanService interventionPlanService;

    /**
     * 分页查询干预方案
     */
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getInterventionPlanPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Integer elderlyId,
            @RequestParam(required = false) String elderlyName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String planType,
            @RequestParam(required = false) String priorityLevel) {
        try {
            IPage<InterventionPlan> page = interventionPlanService.getInterventionPlanPage(
                    current, size, elderlyId, elderlyName, status, planType, priorityLevel);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", page);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询干预方案分页失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据ID查询干预方案详情
     */
    @GetMapping("/{planId}")
    public ResponseEntity<Map<String, Object>> getInterventionPlanById(@PathVariable Long planId) {
        try {
            Map<String, Object> detail = interventionPlanService.getInterventionPlanDetail(planId);
            
            Map<String, Object> result = new HashMap<>();
            if (detail != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", detail);
            } else {
                result.put("code", 404);
                result.put("message", "干预方案不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询干预方案详情失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 创建干预方案
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createInterventionPlan(@RequestBody InterventionPlan interventionPlan) {
        try {
            boolean success = interventionPlanService.createInterventionPlan(interventionPlan);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "创建成功");
                result.put("data", interventionPlan);
            } else {
                result.put("code", 500);
                result.put("message", "创建失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("创建干预方案失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 更新干预方案
     */
    @PutMapping("/{planId}")
    public ResponseEntity<Map<String, Object>> updateInterventionPlan(
            @PathVariable Long planId, @RequestBody InterventionPlan interventionPlan) {
        try {
            interventionPlan.setPlanId(planId);
            boolean success = interventionPlanService.updateInterventionPlan(interventionPlan);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
                result.put("data", interventionPlan);
            } else {
                result.put("code", 500);
                result.put("message", "更新失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("更新干预方案失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 删除干预方案
     */
    @DeleteMapping("/{planId}")
    public ResponseEntity<Map<String, Object>> deleteInterventionPlan(@PathVariable Long planId) {
        try {
            boolean success = interventionPlanService.removeById(planId);
            
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
            log.error("删除干预方案失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据老人ID查询活跃的干预方案
     */
    @GetMapping("/elderly/{elderlyId}/active")
    public ResponseEntity<Map<String, Object>> getActiveInterventionsByElderlyId(@PathVariable Integer elderlyId) {
        try {
            List<InterventionPlan> plans = interventionPlanService.getActiveInterventionsByElderlyId(elderlyId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", plans);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询老人活跃干预方案失败，老人ID: {}", elderlyId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据预警ID查询关联的干预方案
     */
    @GetMapping("/alert/{alertId}")
    public ResponseEntity<Map<String, Object>> getInterventionsByAlertId(@PathVariable Long alertId) {
        try {
            List<InterventionPlan> plans = interventionPlanService.getInterventionsByAlertId(alertId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", plans);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询预警关联干预方案失败，预警ID: {}", alertId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 启动干预方案
     */
    @PostMapping("/{planId}/start")
    public ResponseEntity<Map<String, Object>> startInterventionPlan(@PathVariable Long planId) {
        try {
            boolean success = interventionPlanService.startInterventionPlan(planId);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "启动成功");
            } else {
                result.put("code", 500);
                result.put("message", "启动失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("启动干预方案失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "启动失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 暂停干预方案
     */
    @PostMapping("/{planId}/pause")
    public ResponseEntity<Map<String, Object>> pauseInterventionPlan(@PathVariable Long planId) {
        try {
            boolean success = interventionPlanService.pauseInterventionPlan(planId);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "暂停成功");
            } else {
                result.put("code", 500);
                result.put("message", "暂停失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("暂停干预方案失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "暂停失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 完成干预方案
     */
    @PostMapping("/{planId}/complete")
    public ResponseEntity<Map<String, Object>> completeInterventionPlan(
            @PathVariable Long planId, @RequestParam BigDecimal effectivenessScore) {
        try {
            boolean success = interventionPlanService.completeInterventionPlan(planId, effectivenessScore);
            
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
            log.error("完成干预方案失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "完成失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 取消干预方案
     */
    @PostMapping("/{planId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelInterventionPlan(
            @PathVariable Long planId, @RequestParam String reason) {
        try {
            boolean success = interventionPlanService.cancelInterventionPlan(planId, reason);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "取消成功");
            } else {
                result.put("code", 500);
                result.put("message", "取消失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("取消干预方案失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "取消失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 统计干预方案数量按状态分组
     */
    @GetMapping("/statistics/status")
    public ResponseEntity<Map<String, Object>> getCountByStatus(
            @RequestParam(required = false) Integer elderlyId) {
        try {
            List<Map<String, Object>> statistics = interventionPlanService.getCountByStatus(elderlyId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", statistics);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询状态统计失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 统计干预方案数量按类型分组
     */
    @GetMapping("/statistics/type")
    public ResponseEntity<Map<String, Object>> getCountByType(
            @RequestParam(required = false) Integer elderlyId) {
        try {
            List<Map<String, Object>> statistics = interventionPlanService.getCountByType(elderlyId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", statistics);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询类型统计失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询即将到期的干预方案
     */
    @GetMapping("/expiring")
    public ResponseEntity<Map<String, Object>> getExpiringPlans(
            @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<InterventionPlan> plans = interventionPlanService.getExpiringPlans(days);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", plans);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询即将到期方案失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询高优先级未完成的干预方案
     */
    @GetMapping("/high-priority")
    public ResponseEntity<Map<String, Object>> getHighPriorityPlans() {
        try {
            List<InterventionPlan> plans = interventionPlanService.getHighPriorityPlans();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", plans);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询高优先级方案失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据健康预警自动创建干预方案
     */
    @PostMapping("/create-from-alert")
    public ResponseEntity<Map<String, Object>> createInterventionFromAlert(
            @RequestParam Long alertId,
            @RequestParam Integer elderlyId,
            @RequestParam String elderlyName,
            @RequestParam String alertType) {
        try {
            boolean success = interventionPlanService.createInterventionFromAlert(
                    alertId, elderlyId, elderlyName, alertType);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "自动创建干预方案成功");
            } else {
                result.put("code", 500);
                result.put("message", "自动创建失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("自动创建干预方案失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "自动创建失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 复制干预方案
     */
    @PostMapping("/{planId}/copy")
    public ResponseEntity<Map<String, Object>> copyInterventionPlan(
            @PathVariable Long planId,
            @RequestParam Integer elderlyId,
            @RequestParam String elderlyName) {
        try {
            Long newPlanId = interventionPlanService.copyInterventionPlan(planId, elderlyId, elderlyName);
            
            Map<String, Object> result = new HashMap<>();
            if (newPlanId != null) {
                result.put("code", 200);
                result.put("message", "复制成功");
                result.put("data", newPlanId);
            } else {
                result.put("code", 500);
                result.put("message", "复制失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("复制干预方案失败，方案ID: {}", planId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "复制失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}