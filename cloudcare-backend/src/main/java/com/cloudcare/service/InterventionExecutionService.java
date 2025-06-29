package com.cloudcare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.InterventionExecution;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 干预执行记录服务接口
 *
 * @author CloudCare
 * @since 2024-01-15
 */
public interface InterventionExecutionService extends IService<InterventionExecution> {

    /**
     * 分页查询执行记录
     *
     * @param current 当前页
     * @param size 页大小
     * @param planId 方案ID
     * @param executionStatus 执行状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页结果
     */
    IPage<InterventionExecution> getExecutionPage(Long current, Long size, Long planId, 
                                                  String executionStatus, LocalDate startDate, LocalDate endDate);

    /**
     * 创建执行记录
     *
     * @param execution 执行记录
     * @return 是否成功
     */
    boolean createExecution(InterventionExecution execution);

    /**
     * 根据方案ID查询执行记录
     *
     * @param planId 方案ID
     * @return 执行记录列表
     */
    List<InterventionExecution> getExecutionsByPlanId(Long planId);

    /**
     * 查询最近的执行记录
     *
     * @param planId 方案ID
     * @param limit 限制数量
     * @return 执行记录列表
     */
    List<InterventionExecution> getRecentExecutions(Long planId, Integer limit);

    /**
     * 统计执行记录按状态分组
     *
     * @param planId 方案ID（可选）
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @return 统计结果
     */
    List<Map<String, Object>> getCountByStatus(Long planId, LocalDate startDate, LocalDate endDate);

    /**
     * 计算方案的平均完成率
     *
     * @param planId 方案ID
     * @return 平均完成率
     */
    Double calculateAverageCompletionRate(Long planId);

    /**
     * 计算方案的平均效果评分
     *
     * @param planId 方案ID
     * @return 平均效果评分
     */
    Double calculateAverageEffectivenessRating(Long planId);

    /**
     * 查询指定日期范围内的执行统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> getExecutionStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 查询今日待执行的干预记录
     *
     * @return 执行记录列表
     */
    List<Map<String, Object>> getTodayPendingExecutions();

    /**
     * 查询逾期未执行的记录
     *
     * @return 执行记录列表
     */
    List<Map<String, Object>> getOverdueExecutions();

    /**
     * 完成执行记录
     *
     * @param executionId 执行记录ID
     * @param executionDetails 执行详情
     * @param patientFeedback 患者反馈
     * @param effectivenessRating 效果评分
     * @return 是否成功
     */
    boolean completeExecution(Long executionId, String executionDetails, 
                             String patientFeedback, Integer effectivenessRating);

    /**
     * 标记执行记录为未完成
     *
     * @param executionId 执行记录ID
     * @param reason 未完成原因
     * @return 是否成功
     */
    boolean markExecutionMissed(Long executionId, String reason);

    /**
     * 批量创建执行计划
     *
     * @param planId 方案ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param frequency 频率（daily, weekly, monthly）
     * @return 是否成功
     */
    boolean createExecutionSchedule(Long planId, LocalDate startDate, LocalDate endDate, String frequency);

    /**
     * 获取执行记录详情
     *
     * @param executionId 执行记录ID
     * @return 详情信息
     */
    Map<String, Object> getExecutionDetail(Long executionId);

    /**
     * 更新执行记录的生命体征
     *
     * @param executionId 执行记录ID
     * @param vitalSignsBefore 执行前生命体征
     * @param vitalSignsAfter 执行后生命体征
     * @return 是否成功
     */
    boolean updateVitalSigns(Long executionId, String vitalSignsBefore, String vitalSignsAfter);
}