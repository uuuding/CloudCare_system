package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.entity.InterventionExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 干预执行记录 Mapper 接口
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Mapper
public interface InterventionExecutionMapper extends BaseMapper<InterventionExecution> {

    /**
     * 分页查询执行记录
     *
     * @param page 分页参数
     * @param planId 方案ID
     * @param executionStatus 执行状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页结果
     */
    IPage<InterventionExecution> selectExecutionPage(Page<InterventionExecution> page,
                                                     @Param("planId") Long planId,
                                                     @Param("executionStatus") String executionStatus,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    /**
     * 根据方案ID查询执行记录
     *
     * @param planId 方案ID
     * @return 执行记录列表
     */
    List<InterventionExecution> selectByPlanId(@Param("planId") Long planId);

    /**
     * 查询最近的执行记录
     *
     * @param planId 方案ID
     * @param limit 限制数量
     * @return 执行记录列表
     */
    List<InterventionExecution> selectRecentExecutions(@Param("planId") Long planId, @Param("limit") Integer limit);

    /**
     * 统计执行记录按状态分组
     *
     * @param planId 方案ID（可选）
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus(@Param("planId") Long planId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    /**
     * 计算方案的平均完成率
     *
     * @param planId 方案ID
     * @return 平均完成率
     */
    Double calculateAverageCompletionRate(@Param("planId") Long planId);

    /**
     * 计算方案的平均效果评分
     *
     * @param planId 方案ID
     * @return 平均效果评分
     */
    Double calculateAverageEffectivenessRating(@Param("planId") Long planId);

    /**
     * 查询指定日期范围内的执行统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> getExecutionStatistics(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    /**
     * 查询今日待执行的干预记录
     *
     * @return 执行记录列表
     */
    List<Map<String, Object>> selectTodayPendingExecutions();

    /**
     * 查询逾期未执行的记录
     *
     * @return 执行记录列表
     */
    List<Map<String, Object>> selectOverdueExecutions();
}