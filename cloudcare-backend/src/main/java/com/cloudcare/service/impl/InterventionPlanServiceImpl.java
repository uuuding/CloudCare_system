package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.entity.InterventionExecution;
import com.cloudcare.entity.InterventionPlan;
import com.cloudcare.entity.InterventionTemplate;
import com.cloudcare.mapper.ElderlyProfileMapper;
import com.cloudcare.mapper.InterventionPlanMapper;
import com.cloudcare.service.InterventionExecutionService;
import com.cloudcare.service.InterventionPlanService;
import com.cloudcare.service.InterventionTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 干预方案服务实现类
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Slf4j
@Service
public class InterventionPlanServiceImpl extends ServiceImpl<InterventionPlanMapper, InterventionPlan> implements InterventionPlanService {

    @Autowired
    private InterventionExecutionService interventionExecutionService;

    @Autowired
    private InterventionTemplateService interventionTemplateService;
    
    @Autowired
    private ElderlyProfileMapper elderlyProfileMapper;

    @Override
    public IPage<InterventionPlan> getInterventionPlanPage(Long current, Long size, Integer elderlyId, 
                                                          String elderlyName, String status, String planType, String priorityLevel) {
        Page<InterventionPlan> page = new Page<>(current, size);
        return baseMapper.selectInterventionPlanPage(page, elderlyId, elderlyName, status, planType, priorityLevel);
    }

    @Override
    @Transactional
    public boolean createInterventionPlan(InterventionPlan interventionPlan) {
        try {
            // 如果有elderlyId但没有elderlyName，通过elderlyId查询elderlyName
            if (interventionPlan.getElderlyId() != null && 
                (interventionPlan.getElderlyName() == null || interventionPlan.getElderlyName().trim().isEmpty())) {
                ElderlyProfile elderlyProfile = elderlyProfileMapper.selectById(interventionPlan.getElderlyId());
                if (elderlyProfile != null) {
                    interventionPlan.setElderlyName(elderlyProfile.getName());
                } else {
                    throw new RuntimeException("未找到对应的老人信息，elderlyId: " + interventionPlan.getElderlyId());
                }
            }
            
            // 设置默认值
            if (interventionPlan.getStatus() == null) {
                interventionPlan.setStatus("PENDING");
            }
            if (interventionPlan.getProgressRate() == null) {
                interventionPlan.setProgressRate(BigDecimal.ZERO);
            }
            if (interventionPlan.getCreatedTime() == null) {
                interventionPlan.setCreatedTime(LocalDateTime.now());
            }
            if (interventionPlan.getCreatedBy() == null) {
                interventionPlan.setCreatedBy("系统管理员");
            }
            
            boolean result = save(interventionPlan);
            if (result) {
                log.info("创建干预方案成功，方案ID: {}, 老人ID: {}", interventionPlan.getPlanId(), interventionPlan.getElderlyId());
            }
            return result;
        } catch (Exception e) {
            log.error("创建干预方案失败", e);
            throw new RuntimeException("创建干预方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateInterventionPlan(InterventionPlan interventionPlan) {
        try {
            interventionPlan.setUpdatedTime(LocalDateTime.now());
            boolean result = updateById(interventionPlan);
            if (result) {
                log.info("更新干预方案成功，方案ID: {}", interventionPlan.getPlanId());
            }
            return result;
        } catch (Exception e) {
            log.error("更新干预方案失败，方案ID: {}", interventionPlan.getPlanId(), e);
            throw new RuntimeException("更新干预方案失败: " + e.getMessage());
        }
    }

    @Override
    public List<InterventionPlan> getActiveInterventionsByElderlyId(Integer elderlyId) {
        return baseMapper.selectActiveInterventionsByElderlyId(elderlyId);
    }

    @Override
    public List<InterventionPlan> getInterventionsByAlertId(Long alertId) {
        return baseMapper.selectInterventionsByAlertId(alertId);
    }

    @Override
    @Transactional
    public boolean updateProgress(Long planId, BigDecimal progressRate) {
        try {
            int result = baseMapper.updateProgress(planId, progressRate);
            if (result > 0) {
                log.info("更新干预方案进度成功，方案ID: {}, 进度: {}%", planId, progressRate);
                
                // 如果进度达到100%，自动完成方案
                if (progressRate.compareTo(new BigDecimal("100")) >= 0) {
                    updateStatus(planId, "COMPLETED");
                }
            }
            return result > 0;
        } catch (Exception e) {
            log.error("更新干预方案进度失败，方案ID: {}", planId, e);
            throw new RuntimeException("更新进度失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateStatus(Long planId, String status) {
        try {
            int result = baseMapper.updateStatus(planId, status);
            if (result > 0) {
                log.info("更新干预方案状态成功，方案ID: {}, 状态: {}", planId, status);
            }
            return result > 0;
        } catch (Exception e) {
            log.error("更新干预方案状态失败，方案ID: {}", planId, e);
            throw new RuntimeException("更新状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean startInterventionPlan(Long planId) {
        try {
            // 更新方案状态为ACTIVE
            boolean statusUpdated = updateStatus(planId, "ACTIVE");
            if (!statusUpdated) {
                return false;
            }
            
            // 获取方案详情
            InterventionPlan plan = getById(planId);
            if (plan == null) {
                return false;
            }
            
            // 自动创建执行记录
            createExecutionRecords(plan);
            
            log.info("启动干预方案成功，方案ID: {}", planId);
            return true;
        } catch (Exception e) {
            log.error("启动干预方案失败，方案ID: {}", planId, e);
            throw new RuntimeException("启动方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean pauseInterventionPlan(Long planId) {
        return updateStatus(planId, "PAUSED");
    }

    @Override
    @Transactional
    public boolean completeInterventionPlan(Long planId, BigDecimal effectivenessScore) {
        try {
            InterventionPlan plan = getById(planId);
            if (plan != null) {
                plan.setStatus("COMPLETED");
                plan.setProgressRate(new BigDecimal("100"));
                plan.setEffectivenessScore(effectivenessScore);
                plan.setUpdatedTime(LocalDateTime.now());
                return updateById(plan);
            }
            return false;
        } catch (Exception e) {
            log.error("完成干预方案失败，方案ID: {}", planId, e);
            throw new RuntimeException("完成方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean cancelInterventionPlan(Long planId, String reason) {
        try {
            InterventionPlan plan = getById(planId);
            if (plan != null) {
                plan.setStatus("CANCELLED");
                plan.setNotes(plan.getNotes() + "\n取消原因: " + reason);
                plan.setUpdatedTime(LocalDateTime.now());
                return updateById(plan);
            }
            return false;
        } catch (Exception e) {
            log.error("取消干预方案失败，方案ID: {}", planId, e);
            throw new RuntimeException("取消方案失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getCountByStatus(Integer elderlyId) {
        return baseMapper.countByStatus(elderlyId);
    }

    @Override
    public List<Map<String, Object>> getCountByType(Integer elderlyId) {
        return baseMapper.countByType(elderlyId);
    }

    @Override
    public List<InterventionPlan> getExpiringPlans(Integer days) {
        return baseMapper.selectExpiringPlans(days);
    }

    @Override
    public List<InterventionPlan> getHighPriorityPlans() {
        return baseMapper.selectHighPriorityPlans();
    }

    @Override
    @Transactional
    public boolean createInterventionFromAlert(Long alertId, Integer elderlyId, String elderlyName, String alertType) {
        try {
            // 根据预警类型查找推荐模板
            List<InterventionTemplate> templates = interventionTemplateService.getRecommendedTemplates(alertType);
            
            if (templates.isEmpty()) {
                log.warn("未找到适用于预警类型 {} 的干预模板", alertType);
                return false;
            }
            
            // 使用第一个推荐模板创建干预方案
            InterventionTemplate template = templates.get(0);
            
            InterventionPlan plan = new InterventionPlan();
            plan.setElderlyId(elderlyId);
            plan.setElderlyName(elderlyName);
            plan.setAlertId(alertId);
            plan.setPlanTitle(template.getTemplateName());
            plan.setPlanType(template.getTemplateCategory());
            plan.setPriorityLevel(template.getPriorityLevel() != null ? template.getPriorityLevel() : "MEDIUM");
            plan.setInterventionContent(template.getTemplateContent());
            plan.setImplementationMethod(template.getImplementationGuide());
            plan.setNotes(template.getPrecautions());
            plan.setStartDate(LocalDate.now());
            plan.setStatus("PENDING");
            plan.setProgressRate(BigDecimal.ZERO);
            plan.setCreatedBy("系统自动创建");
            
            boolean result = createInterventionPlan(plan);
            if (result) {
                // 增加模板使用次数
                interventionTemplateService.incrementUsageCount(template.getTemplateId());
                log.info("根据预警自动创建干预方案成功，预警ID: {}, 方案ID: {}", alertId, plan.getPlanId());
            }
            
            return result;
        } catch (Exception e) {
            log.error("根据预警创建干预方案失败，预警ID: {}", alertId, e);
            throw new RuntimeException("自动创建干预方案失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getInterventionPlanDetail(Long planId) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 获取干预方案基本信息
            InterventionPlan plan = getById(planId);
            if (plan == null) {
                return null;
            }
            result.put("plan", plan);
            
            // 获取执行记录
            List<InterventionExecution> executions = interventionExecutionService.getExecutionsByPlanId(planId);
            result.put("executions", executions);
            
            // 计算统计信息
            Double avgCompletionRate = interventionExecutionService.calculateAverageCompletionRate(planId);
            Double avgEffectivenessRating = interventionExecutionService.calculateAverageEffectivenessRating(planId);
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalExecutions", executions.size());
            statistics.put("avgCompletionRate", avgCompletionRate);
            statistics.put("avgEffectivenessRating", avgEffectivenessRating);
            result.put("statistics", statistics);
            
            return result;
        } catch (Exception e) {
            log.error("获取干预方案详情失败，方案ID: {}", planId, e);
            throw new RuntimeException("获取方案详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<Long> planIds, String status) {
        try {
            for (Long planId : planIds) {
                updateStatus(planId, status);
            }
            log.info("批量更新干预方案状态成功，数量: {}, 状态: {}", planIds.size(), status);
            return true;
        } catch (Exception e) {
            log.error("批量更新干预方案状态失败", e);
            throw new RuntimeException("批量更新状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Long copyInterventionPlan(Long planId, Integer elderlyId, String elderlyName) {
        try {
            InterventionPlan originalPlan = getById(planId);
            if (originalPlan == null) {
                throw new RuntimeException("原方案不存在");
            }
            
            InterventionPlan newPlan = new InterventionPlan();
            BeanUtils.copyProperties(originalPlan, newPlan);
            
            // 重置关键字段
            newPlan.setPlanId(null);
            newPlan.setElderlyId(elderlyId);
            newPlan.setElderlyName(elderlyName);
            newPlan.setAlertId(null);
            newPlan.setStatus("PENDING");
            newPlan.setProgressRate(BigDecimal.ZERO);
            newPlan.setEffectivenessScore(null);
            newPlan.setStartDate(LocalDate.now());
            newPlan.setCreatedBy("复制创建");
            newPlan.setCreatedTime(LocalDateTime.now());
            newPlan.setUpdatedTime(LocalDateTime.now());
            
            boolean result = createInterventionPlan(newPlan);
            if (result) {
                log.info("复制干预方案成功，原方案ID: {}, 新方案ID: {}", planId, newPlan.getPlanId());
                return newPlan.getPlanId();
            }
            
            return null;
        } catch (Exception e) {
            log.error("复制干预方案失败，原方案ID: {}", planId, e);
            throw new RuntimeException("复制方案失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据方案配置自动创建执行记录
     */
    private void createExecutionRecords(InterventionPlan plan) {
        try {
            LocalDate startDate = plan.getStartDate();
            LocalDate endDate = plan.getEndDate();
            String frequency = plan.getFrequency();
            
            if (startDate == null || endDate == null || frequency == null) {
                log.warn("方案配置不完整，无法创建执行记录，方案ID: {}", plan.getPlanId());
                return;
            }
            
            // 根据频率计算执行日期
            LocalDate currentDate = startDate;
            int dayIncrement = getDayIncrement(frequency);
            
            while (!currentDate.isAfter(endDate)) {
                InterventionExecution execution = new InterventionExecution();
                execution.setPlanId(plan.getPlanId());
                execution.setExecutionDate(currentDate);
                execution.setExecutionTime(LocalTime.of(9, 0)); // 默认上午9点
                execution.setExecutionStatus("PENDING");
                execution.setCompletionRate(BigDecimal.ZERO);
                execution.setExecutorName(plan.getResponsiblePerson());
                execution.setCreatedTime(LocalDateTime.now());

                
                // 计算下一个执行日期
                currentDate = currentDate.plusDays(dayIncrement);
            }
            
            log.info("为方案 {} 自动创建执行记录完成", plan.getPlanId());
        } catch (Exception e) {
            log.error("创建执行记录失败，方案ID: {}", plan.getPlanId(), e);
        }
    }
    
    /**
     * 根据频率获取天数增量
     */
    private int getDayIncrement(String frequency) {
        if (frequency == null) {
            return 1;
        }
        switch (frequency.toUpperCase()) {
            case "DAILY":
            case "每日":
            case "每日一次":
                return 1;
            case "WEEKLY":
            case "每周":
            case "每周一次":
                return 7;
            case "MONTHLY":
            case "每月":
            case "每月一次":
                return 30;
            case "AS_NEEDED":
            case "按需":
            case "按需执行":
                return 1; // 按需执行默认按每日处理
            default:
                return 1;
        }
    }
}