package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.entity.InterventionPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 干预方案 Mapper 接口
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Mapper
public interface InterventionPlanMapper extends BaseMapper<InterventionPlan> {

    /**
     * 分页查询干预方案列表
     *
     * @param page 分页参数
     * @param elderlyId 老人ID
     * @param status 状态
     * @param planType 干预类型
     * @param priorityLevel 优先级
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT * FROM intervention_plan WHERE 1=1" +
            "<if test='elderlyId != null'> AND elderly_id = #{elderlyId}</if>" +
            "<if test='elderlyName != null and elderlyName != &quot;&quot;'> AND elderly_name LIKE CONCAT('%', #{elderlyName}, '%')</if>" +
            "<if test='status != null and status != &quot;&quot;'> AND status = #{status}</if>" +
            "<if test='planType != null and planType != &quot;&quot;'> AND plan_type = #{planType}</if>" +
            "<if test='priorityLevel != null and priorityLevel != &quot;&quot;'> AND priority_level = #{priorityLevel}</if>" +
            " ORDER BY created_time DESC" +
            "</script>")
    IPage<InterventionPlan> selectInterventionPlanPage(Page<InterventionPlan> page,
                                                       @Param("elderlyId") Integer elderlyId,
                                                       @Param("elderlyName") String elderlyName,
                                                       @Param("status") String status,
                                                       @Param("planType") String planType,
                                                       @Param("priorityLevel") String priorityLevel);

    /**
     * 根据老人ID查询活跃的干预方案
     *
     * @param elderlyId 老人ID
     * @return 干预方案列表
     */
    @Select("SELECT * FROM intervention_plan WHERE elderly_id = #{elderlyId} AND status IN ('PENDING', 'ACTIVE') ORDER BY priority_level DESC, created_time DESC")
    List<InterventionPlan> selectActiveInterventionsByElderlyId(@Param("elderlyId") Integer elderlyId);

    /**
     * 根据预警ID查询关联的干预方案
     *
     * @param alertId 预警ID
     * @return 干预方案列表
     */
    @Select("SELECT * FROM intervention_plan WHERE alert_id = #{alertId} ORDER BY created_time DESC")
    List<InterventionPlan> selectInterventionsByAlertId(@Param("alertId") Long alertId);

    /**
     * 更新干预方案进度
     *
     * @param planId 方案ID
     * @param progressRate 进度百分比
     * @return 更新行数
     */
    @Update("UPDATE intervention_plan SET progress_rate = #{progressRate}, updated_time = NOW() WHERE plan_id = #{planId}")
    int updateProgress(@Param("planId") Long planId, @Param("progressRate") BigDecimal progressRate);

    /**
     * 更新干预方案状态
     *
     * @param planId 方案ID
     * @param status 新状态
     * @return 更新行数
     */
    @Update("UPDATE intervention_plan SET status = #{status}, updated_time = NOW() WHERE plan_id = #{planId}")
    int updateStatus(@Param("planId") Long planId, @Param("status") String status);

    /**
     * 统计干预方案数量按状态分组
     *
     * @param elderlyId 老人ID（可选）
     * @return 统计结果
     */
    @Select("<script>" +
            "SELECT status, COUNT(1) as count FROM intervention_plan WHERE 1=1" +
            "<if test='elderlyId != null'> AND elderly_id = #{elderlyId}</if>" +
            " GROUP BY status" +
            "</script>")
    List<Map<String, Object>> countByStatus(@Param("elderlyId") Integer elderlyId);

    /**
     * 统计干预方案数量按类型分组
     *
     * @param elderlyId 老人ID（可选）
     * @return 统计结果
     */
    @Select("<script>" +
            "SELECT plan_type, COUNT(1) as count FROM intervention_plan WHERE 1=1" +
            "<if test='elderlyId != null'> AND elderly_id = #{elderlyId}</if>" +
            " GROUP BY plan_type" +
            "</script>")
    List<Map<String, Object>> countByType(@Param("elderlyId") Integer elderlyId);

    /**
     * 查询即将到期的干预方案
     *
     * @param days 天数
     * @return 干预方案列表
     */
    @Select("SELECT * FROM intervention_plan WHERE status = 'ACTIVE' AND end_date <= DATE_ADD(CURDATE(), INTERVAL #{days} DAY) ORDER BY end_date ASC")
    List<InterventionPlan> selectExpiringPlans(@Param("days") Integer days);

    /**
     * 查询高优先级未完成的干预方案
     *
     * @return 干预方案列表
     */
    @Select("SELECT * FROM intervention_plan WHERE priority_level IN ('HIGH', 'URGENT') AND status IN ('PENDING', 'ACTIVE') ORDER BY priority_level DESC, created_time ASC")
    List<InterventionPlan> selectHighPriorityPlans();
}