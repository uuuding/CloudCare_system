package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.InterventionExecution;
import com.cloudcare.entity.InterventionPlan;
import com.cloudcare.mapper.InterventionExecutionMapper;
import com.cloudcare.service.InterventionExecutionService;
import com.cloudcare.service.InterventionPlanService;
import lombok.extern.slf4j.Slf4j;
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
 * 干预执行记录服务实现类
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Slf4j
@Service
public class InterventionExecutionServiceImpl extends ServiceImpl<InterventionExecutionMapper, InterventionExecution> implements InterventionExecutionService {

    @Autowired
    private InterventionPlanService interventionPlanService;

    @Override
    public IPage<InterventionExecution> getExecutionPage(Long current, Long size, Long planId, 
                                                        String executionStatus, LocalDate startDate, LocalDate endDate) {
        Page<InterventionExecution> page = new Page<>(current, size);
        return baseMapper.selectExecutionPage(page, planId, executionStatus, startDate, endDate);
    }

    @Override
    @Transactional
    public boolean createExecution(InterventionExecution execution) {
        try {
            // 设置默认值
            if (execution.getExecutionDate() == null) {
                execution.setExecutionDate(LocalDate.now());
            }
            if (execution.getExecutionTime() == null) {
                execution.setExecutionTime(LocalTime.now());
            }
            if (execution.getExecutionStatus() == null) {
                execution.setExecutionStatus("PENDING");
            }
            if (execution.getCompletionRate() == null) {
                execution.setCompletionRate(BigDecimal.ZERO);
            }
            if (execution.getCreatedTime() == null) {
                execution.setCreatedTime(LocalDateTime.now());
            }
            
            boolean result = save(execution);
            if (result) {
                log.info("创建执行记录成功，记录ID: {}, 方案ID: {}", execution.getExecutionId(), execution.getPlanId());
                
                // 更新方案进度
                updatePlanProgress(execution.getPlanId());
            }
            return result;
        } catch (Exception e) {
            log.error("创建执行记录失败", e);
            throw new RuntimeException("创建执行记录失败: " + e.getMessage());
        }
    }

    @Override
    public List<InterventionExecution> getExecutionsByPlanId(Long planId) {
        return baseMapper.selectByPlanId(planId);
    }

    @Override
    public List<InterventionExecution> getRecentExecutions(Long planId, Integer limit) {
        return baseMapper.selectRecentExecutions(planId, limit);
    }

    @Override
    public List<Map<String, Object>> getCountByStatus(Long planId, LocalDate startDate, LocalDate endDate) {
        return baseMapper.countByStatus(planId, startDate, endDate);
    }

    @Override
    public Double calculateAverageCompletionRate(Long planId) {
        return baseMapper.calculateAverageCompletionRate(planId);
    }

    @Override
    public Double calculateAverageEffectivenessRating(Long planId) {
        return baseMapper.calculateAverageEffectivenessRating(planId);
    }

    @Override
    public List<Map<String, Object>> getExecutionStatistics(LocalDate startDate, LocalDate endDate) {
        return baseMapper.getExecutionStatistics(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getTodayPendingExecutions() {
        return baseMapper.selectTodayPendingExecutions();
    }

    @Override
    public List<Map<String, Object>> getOverdueExecutions() {
        return baseMapper.selectOverdueExecutions();
    }

    @Override
    @Transactional
    public boolean completeExecution(Long executionId, String executionDetails, 
                                    String patientFeedback, Integer effectivenessRating) {
        try {
            InterventionExecution execution = getById(executionId);
            if (execution == null) {
                throw new RuntimeException("执行记录不存在");
            }
            
            execution.setExecutionStatus("COMPLETED");
            execution.setCompletionRate(new BigDecimal("100"));
            execution.setExecutionDetails(executionDetails);
            execution.setPatientFeedback(patientFeedback);
            execution.setEffectivenessRating(effectivenessRating);
            execution.setUpdatedTime(LocalDateTime.now());
            
            boolean result = updateById(execution);
            if (result) {
                log.info("完成执行记录成功，记录ID: {}", executionId);
                
                // 更新方案进度
                updatePlanProgress(execution.getPlanId());
            }
            return result;
        } catch (Exception e) {
            log.error("完成执行记录失败，记录ID: {}", executionId, e);
            throw new RuntimeException("完成执行记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean markExecutionMissed(Long executionId, String reason) {
        try {
            InterventionExecution execution = getById(executionId);
            if (execution == null) {
                throw new RuntimeException("执行记录不存在");
            }
            
            execution.setExecutionStatus("MISSED");
            execution.setCompletionRate(BigDecimal.ZERO);
            execution.setNotes(execution.getNotes() + "\n未执行原因: " + reason);
            execution.setUpdatedTime(LocalDateTime.now());
            
            boolean result = updateById(execution);
            if (result) {
                log.info("标记执行记录为未完成，记录ID: {}, 原因: {}", executionId, reason);
                
                // 更新方案进度
                updatePlanProgress(execution.getPlanId());
            }
            return result;
        } catch (Exception e) {
            log.error("标记执行记录失败，记录ID: {}", executionId, e);
            throw new RuntimeException("标记执行记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean createExecutionSchedule(Long planId, LocalDate startDate, LocalDate endDate, String frequency) {
        try {
            InterventionPlan plan = interventionPlanService.getById(planId);
            if (plan == null) {
                throw new RuntimeException("干预方案不存在");
            }
            
            LocalDate currentDate = startDate;
            int dayIncrement = getDayIncrement(frequency);
            
            while (!currentDate.isAfter(endDate)) {
                InterventionExecution execution = new InterventionExecution();
                execution.setPlanId(planId);
                execution.setExecutionDate(currentDate);
                execution.setExecutionStatus("PENDING");
                execution.setCompletionRate(BigDecimal.ZERO);
                execution.setExecutorName(plan.getResponsiblePerson());
                execution.setCreatedTime(LocalDateTime.now());
                
                save(execution);
                
                currentDate = currentDate.plusDays(dayIncrement);
            }
            
            log.info("创建执行计划成功，方案ID: {}, 频率: {}", planId, frequency);
            return true;
        } catch (Exception e) {
            log.error("创建执行计划失败，方案ID: {}", planId, e);
            throw new RuntimeException("创建执行计划失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getExecutionDetail(Long executionId) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            InterventionExecution execution = getById(executionId);
            if (execution == null) {
                return null;
            }
            result.put("execution", execution);
            
            // 获取关联的干预方案信息
            InterventionPlan plan = interventionPlanService.getById(execution.getPlanId());
            result.put("plan", plan);
            
            return result;
        } catch (Exception e) {
            log.error("获取执行记录详情失败，记录ID: {}", executionId, e);
            throw new RuntimeException("获取执行记录详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateVitalSigns(Long executionId, String vitalSignsBefore, String vitalSignsAfter) {
        try {
            InterventionExecution execution = getById(executionId);
            if (execution == null) {
                throw new RuntimeException("执行记录不存在");
            }
            
            execution.setVitalSignsBefore(vitalSignsBefore);
            execution.setVitalSignsAfter(vitalSignsAfter);
            execution.setUpdatedTime(LocalDateTime.now());
            
            boolean result = updateById(execution);
            if (result) {
                log.info("更新生命体征成功，记录ID: {}", executionId);
            }
            return result;
        } catch (Exception e) {
            log.error("更新生命体征失败，记录ID: {}", executionId, e);
            throw new RuntimeException("更新生命体征失败: " + e.getMessage());
        }
    }

    /**
     * 根据频率获取天数增量
     */
    private int getDayIncrement(String frequency) {
        switch (frequency.toUpperCase()) {
            case "DAILY":
                return 1;
            case "WEEKLY":
                return 7;
            case "MONTHLY":
                return 30;
            case "AS_NEEDED":
                return 1; // 按需执行默认按每日处理
            default:
                return 1;
        }
    }

    /**
     * 更新方案进度
     */
    private void updatePlanProgress(Long planId) {
        try {
            List<InterventionExecution> executions = getExecutionsByPlanId(planId);
            if (executions.isEmpty()) {
                return;
            }
            
            long completedCount = executions.stream()
                    .filter(e -> "COMPLETED".equals(e.getExecutionStatus()))
                    .count();
            
            BigDecimal progressRate = new BigDecimal(completedCount)
                    .divide(new BigDecimal(executions.size()), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            
            interventionPlanService.updateProgress(planId, progressRate);
        } catch (Exception e) {
            log.error("更新方案进度失败，方案ID: {}", planId, e);
        }
    }
}