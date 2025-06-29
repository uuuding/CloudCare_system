package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudcare.entity.InterventionTemplate;
import com.cloudcare.service.InterventionTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 干预模板控制器
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/api/intervention-template")
@CrossOrigin(origins = "*")
public class InterventionTemplateController {

    @Autowired
    private InterventionTemplateService interventionTemplateService;

    /**
     * 分页查询干预模板
     */
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getInterventionTemplatePage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String templateName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String alertType,
            @RequestParam(required = false) String priorityLevel,
            @RequestParam(required = false) Boolean isEnabled) {
        try {
            log.info("分页查询参数 - current: {}, size: {}, templateName: {}, category: {}, alertType: {}, isEnabled: {}", 
                    current, size, templateName, category, alertType, isEnabled);
            
            IPage<InterventionTemplate> page = interventionTemplateService.getTemplatePage(
                    current, size, templateName, category, alertType, isEnabled);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", page);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询干预模板分页失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 获取模板使用统计
     */
    @GetMapping(value = "/statistics/usage", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getTemplateUsageStatistics() {
        try {
            List<InterventionTemplate> statistics = interventionTemplateService.getTemplateUsageStatistics();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", statistics);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询模板使用统计失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据ID查询干预模板详情
     */
    @GetMapping("/{templateId}")
    public ResponseEntity<Map<String, Object>> getInterventionTemplateById(@PathVariable Long templateId) {
        try {
            InterventionTemplate template = interventionTemplateService.getById(templateId);
            
            Map<String, Object> result = new HashMap<>();
            if (template != null) {
                result.put("code", 200);
                result.put("message", "查询成功");
                result.put("data", template);
            } else {
                result.put("code", 404);
                result.put("message", "模板不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询干预模板详情失败，模板ID: {}", templateId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 创建干预模板
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createInterventionTemplate(@RequestBody InterventionTemplate interventionTemplate) {
        try {
            boolean success = interventionTemplateService.createTemplate(interventionTemplate);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "创建成功");
                result.put("data", interventionTemplate);
            } else {
                result.put("code", 500);
                result.put("message", "创建失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("创建干预模板失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 更新干预模板
     */
    @PutMapping("/{templateId}")
    public ResponseEntity<Map<String, Object>> updateInterventionTemplate(
            @PathVariable Long templateId, @RequestBody InterventionTemplate interventionTemplate) {
        try {
            interventionTemplate.setTemplateId(templateId);
            boolean success = interventionTemplateService.updateTemplate(interventionTemplate);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
                result.put("data", interventionTemplate);
            } else {
                result.put("code", 500);
                result.put("message", "更新失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("更新干预模板失败，模板ID: {}", templateId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 删除干预模板
     */
    @DeleteMapping("/{templateId}")
    public ResponseEntity<Map<String, Object>> deleteInterventionTemplate(@PathVariable Long templateId) {
        try {
            boolean success = interventionTemplateService.removeById(templateId);
            
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
            log.error("删除干预模板失败，模板ID: {}", templateId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据预警类型查询推荐模板
     */
    @GetMapping("/recommend/{alertType}")
    public ResponseEntity<Map<String, Object>> getRecommendedTemplatesByAlertType(@PathVariable String alertType) {
        try {
            List<InterventionTemplate> templates = interventionTemplateService.getRecommendedTemplates(alertType);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", templates);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询推荐模板失败，预警类型: {}", alertType, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据分类查询启用的模板
     */
    @GetMapping("/category/{category}/enabled")
    public ResponseEntity<Map<String, Object>> getEnabledTemplatesByCategory(@PathVariable String category) {
        try {
            List<InterventionTemplate> templates = interventionTemplateService.getTemplatesByCategory(category);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", templates);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询分类启用模板失败，分类: {}", category, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询热门模板
     */
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularTemplates(
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<InterventionTemplate> templates = interventionTemplateService.getPopularTemplates(limit);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", templates);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询热门模板失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 增加模板使用次数
     */
    @PostMapping("/{templateId}/use")
    public ResponseEntity<Map<String, Object>> incrementTemplateUsage(@PathVariable Long templateId) {
        try {
            boolean success = interventionTemplateService.incrementUsageCount(templateId);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "使用次数更新成功");
            } else {
                result.put("code", 500);
                result.put("message", "使用次数更新失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("增加模板使用次数失败，模板ID: {}", templateId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 启用模板
     */
    @PostMapping("/{templateId}/enable")
    public ResponseEntity<Map<String, Object>> enableTemplate(@PathVariable Long templateId) {
        try {
            boolean success = interventionTemplateService.updateActiveStatus(templateId, true);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "启用成功");
            } else {
                result.put("code", 500);
                result.put("message", "启用失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("启用模板失败，模板ID: {}", templateId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "启用失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 禁用模板
     */
    @PostMapping("/{templateId}/disable")
    public ResponseEntity<Map<String, Object>> disableTemplate(@PathVariable Long templateId) {
        try {
            boolean success = interventionTemplateService.updateActiveStatus(templateId, false);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "禁用成功");
            } else {
                result.put("code", 500);
                result.put("message", "禁用失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("禁用模板失败，模板ID: {}", templateId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "禁用失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询所有模板分类
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        try {
            List<String> categories = interventionTemplateService.getAllCategories();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", categories);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询所有分类失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 查询所有预警类型
     */
    @GetMapping("/alert-types")
    public ResponseEntity<Map<String, Object>> getAllAlertTypes() {
        try {
            List<String> alertTypes = interventionTemplateService.getAllAlertTypes();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", alertTypes);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询所有预警类型失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 根据关键词搜索模板
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchTemplatesByKeyword(@RequestParam String keyword) {
        try {
            List<InterventionTemplate> templates = interventionTemplateService.searchTemplates(keyword);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "搜索成功");
            result.put("data", templates);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("搜索模板失败，关键词: {}", keyword, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "搜索失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 复制模板
     */
    @PostMapping("/{templateId}/copy")
    public ResponseEntity<Map<String, Object>> copyInterventionTemplate(
            @PathVariable Long templateId, @RequestParam String newTemplateName) {
        try {
            Long newTemplateId = interventionTemplateService.copyTemplate(templateId, newTemplateName);
            
            Map<String, Object> result = new HashMap<>();
            if (newTemplateId != null) {
                result.put("code", 200);
                result.put("message", "复制成功");
                result.put("data", newTemplateId);
            } else {
                result.put("code", 500);
                result.put("message", "复制失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("复制模板失败，模板ID: {}", templateId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "复制失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 批量启用模板
     */
    @PostMapping("/batch-enable")
    public ResponseEntity<Map<String, Object>> batchEnableTemplates(@RequestBody List<Long> templateIds) {
        try {
            boolean success = interventionTemplateService.batchUpdateActiveStatus(templateIds, true);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "批量启用成功");
            } else {
                result.put("code", 500);
                result.put("message", "批量启用失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("批量启用模板失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "批量启用失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 批量禁用模板
     */
    @PostMapping("/batch-disable")
    public ResponseEntity<Map<String, Object>> batchDisableTemplates(@RequestBody List<Long> templateIds) {
        try {
            boolean success = interventionTemplateService.batchUpdateActiveStatus(templateIds, false);
            
            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("code", 200);
                result.put("message", "批量禁用成功");
            } else {
                result.put("code", 500);
                result.put("message", "批量禁用失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("批量禁用模板失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "批量禁用失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

}