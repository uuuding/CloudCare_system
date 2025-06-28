package com.cloudcare.service;

import com.cloudcare.entity.HealthAlert;
import com.cloudcare.entity.HealthAlertRule;
import com.cloudcare.entity.ElderlyObservations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 健康预警服务接口
 */
public interface HealthAlertService {
    
    /**
     * 根据观察记录检查并生成预警
     */
    void checkAndGenerateAlerts(ElderlyObservations observation);
    
    /**
     * 根据观察记录ID检查并生成预警
     */
    void checkAndGenerateAlertsByObservationId(Integer observationId);
    
    /**
     * 批量检查所有观察记录并生成预警
     */
    int batchCheckAllObservations();
    
    /**
     * 获取所有预警记录
     */
    List<HealthAlert> getAllAlerts();
    
    /**
     * 根据老人ID获取预警记录
     */
    List<HealthAlert> getAlertsByElderlyId(Long elderlyId);
    
    /**
     * 根据预警状态获取预警记录
     */
    List<HealthAlert> getAlertsByStatus(String status);
    
    /**
     * 根据预警级别获取预警记录
     */
    List<HealthAlert> getAlertsByLevel(String alertLevel);
    
    /**
     * 根据时间范围获取预警记录
     */
    List<HealthAlert> getAlertsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 处理预警（标记为已解决）
     */
    boolean resolveAlert(Long alertId, String resolvedBy, String resolvedNote);
    
    /**
     * 忽略预警
     */
    boolean ignoreAlert(Long alertId, String resolvedBy, String resolvedNote);
    
    /**
     * 获取活跃预警数量
     */
    Long getActiveAlertCount();
    
    /**
     * 获取预警统计信息
     */
    Map<String, Object> getAlertStatistics();
    
    /**
     * 获取所有预警规则
     */
    List<HealthAlertRule> getAllRules();
    
    /**
     * 根据类型获取启用的预警规则
     */
    List<HealthAlertRule> getEnabledRulesByType(String alertType);
    
    /**
     * 添加预警规则
     */
    boolean addRule(HealthAlertRule rule);
    
    /**
     * 更新预警规则
     */
    boolean updateRule(HealthAlertRule rule);
    
    /**
     * 删除预警规则
     */
    boolean deleteRule(Long ruleId);
    
    /**
     * 启用/禁用预警规则
     */
    boolean toggleRuleStatus(Long ruleId, Boolean enabled);
    
    /**
     * 初始化默认预警规则
     */
    void initializeDefaultRules();
}