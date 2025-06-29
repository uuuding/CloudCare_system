package com.cloudcare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.InterventionPlan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 干预方案服务接口
 *
 * @author CloudCare
 * @since 2024-01-15
 */
public interface InterventionPlanService extends IService<InterventionPlan> {

    /**
     * 分页查询干预方案
     *
     * @param current 当前页
     * @param size 页大小
     * @param elderlyId 老人ID
     * @param status 状态
     * @param planType 干预类型
     * @param priorityLevel 优先级
     * @return 分页结果
     */
    IPage<InterventionPlan> getInterventionPlanPage(Long current, Long size, Integer elderlyId, 
                                                    String elderlyName, String status, String planType, String priorityLevel);

    /**
     * 创建干预方案
     *
     * @param interventionPlan 干预方案
     * @return 是否成功
     */
    boolean createInterventionPlan(InterventionPlan interventionPlan);

    /**
     * 更新干预方案
     *
     * @param interventionPlan 干预方案
     * @return 是否成功
     */
    boolean updateInterventionPlan(InterventionPlan interventionPlan);

    /**
     * 根据老人ID查询活跃的干预方案
     *
     * @param elderlyId 老人ID
     * @return 干预方案列表
     */
    List<InterventionPlan> getActiveInterventionsByElderlyId(Integer elderlyId);

    /**
     * 根据预警ID查询关联的干预方案
     *
     * @param alertId 预警ID
     * @return 干预方案列表
     */
    List<InterventionPlan> getInterventionsByAlertId(Long alertId);

    /**
     * 更新干预方案进度
     *
     * @param planId 方案ID
     * @param progressRate 进度百分比
     * @return 是否成功
     */
    boolean updateProgress(Long planId, BigDecimal progressRate);

    /**
     * 更新干预方案状态
     *
     * @param planId 方案ID
     * @param status 新状态
     * @return 是否成功
     */
    boolean updateStatus(Long planId, String status);

    /**
     * 启动干预方案
     *
     * @param planId 方案ID
     * @return 是否成功
     */
    boolean startInterventionPlan(Long planId);

    /**
     * 暂停干预方案
     *
     * @param planId 方案ID
     * @return 是否成功
     */
    boolean pauseInterventionPlan(Long planId);

    /**
     * 完成干预方案
     *
     * @param planId 方案ID
     * @param effectivenessScore 效果评分
     * @return 是否成功
     */
    boolean completeInterventionPlan(Long planId, BigDecimal effectivenessScore);

    /**
     * 取消干预方案
     *
     * @param planId 方案ID
     * @param reason 取消原因
     * @return 是否成功
     */
    boolean cancelInterventionPlan(Long planId, String reason);

    /**
     * 统计干预方案数量按状态分组
     *
     * @param elderlyId 老人ID（可选）
     * @return 统计结果
     */
    List<Map<String, Object>> getCountByStatus(Integer elderlyId);

    /**
     * 统计干预方案数量按类型分组
     *
     * @param elderlyId 老人ID（可选）
     * @return 统计结果
     */
    List<Map<String, Object>> getCountByType(Integer elderlyId);

    /**
     * 查询即将到期的干预方案
     *
     * @param days 天数
     * @return 干预方案列表
     */
    List<InterventionPlan> getExpiringPlans(Integer days);

    /**
     * 查询高优先级未完成的干预方案
     *
     * @return 干预方案列表
     */
    List<InterventionPlan> getHighPriorityPlans();

    /**
     * 根据健康预警自动创建干预方案
     *
     * @param alertId 预警ID
     * @param elderlyId 老人ID
     * @param elderlyName 老人姓名
     * @param alertType 预警类型
     * @return 是否成功
     */
    boolean createInterventionFromAlert(Long alertId, Integer elderlyId, String elderlyName, String alertType);

    /**
     * 获取干预方案详情（包含执行记录）
     *
     * @param planId 方案ID
     * @return 详情信息
     */
    Map<String, Object> getInterventionPlanDetail(Long planId);

    /**
     * 批量更新干预方案状态
     *
     * @param planIds 方案ID列表
     * @param status 新状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> planIds, String status);

    /**
     * 复制干预方案
     *
     * @param planId 原方案ID
     * @param elderlyId 新老人ID
     * @param elderlyName 新老人姓名
     * @return 新方案ID
     */
    Long copyInterventionPlan(Long planId, Integer elderlyId, String elderlyName);
}