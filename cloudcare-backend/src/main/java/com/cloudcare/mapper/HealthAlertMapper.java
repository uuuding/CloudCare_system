package com.cloudcare.mapper;

import com.cloudcare.entity.HealthAlert;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface HealthAlertMapper {

    /**
     * 查询所有预警记录
     */
    @Select("SELECT alert_id, elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, " +
            "status, created_at, updated_at, resolved_at, resolved_by, " +
            "resolved_note, observation_id FROM health_alert ORDER BY created_at DESC")
    @Results({
        @Result(property = "alertId", column = "alert_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "elderlyName", column = "elderly_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "alertTitle", column = "alert_title"),
        @Result(property = "alertDescription", column = "alert_description"),
        @Result(property = "triggerValue", column = "trigger_value"),
        @Result(property = "normalRange", column = "normal_range"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "resolvedAt", column = "resolved_at"),
        @Result(property = "resolvedBy", column = "resolved_by"),
        @Result(property = "resolvedNote", column = "resolved_note"),
        @Result(property = "observationId", column = "observation_id")
    })
    List<HealthAlert> findAll();

    /**
     * 根据老人ID查询预警记录
     */
    @Select("SELECT alert_id, elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, " +
            "status, created_at, updated_at, resolved_at, resolved_by, " +
            "resolved_note, observation_id FROM health_alert " +
            "WHERE elderly_id = #{elderlyId} ORDER BY created_at DESC")
    @Results({
        @Result(property = "alertId", column = "alert_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "elderlyName", column = "elderly_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "alertTitle", column = "alert_title"),
        @Result(property = "alertDescription", column = "alert_description"),
        @Result(property = "triggerValue", column = "trigger_value"),
        @Result(property = "normalRange", column = "normal_range"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "resolvedAt", column = "resolved_at"),
        @Result(property = "resolvedBy", column = "resolved_by"),
        @Result(property = "resolvedNote", column = "resolved_note"),
        @Result(property = "observationId", column = "observation_id")
    })
    List<HealthAlert> findByElderlyId(@Param("elderlyId") Long elderlyId);

    /**
     * 根据预警状态查询预警记录
     */
    @Select("SELECT alert_id, elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, " +
            "status, created_at, updated_at, resolved_at, resolved_by, " +
            "resolved_note, observation_id FROM health_alert " +
            "WHERE status = #{status} ORDER BY created_at DESC")
    @Results({
        @Result(property = "alertId", column = "alert_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "elderlyName", column = "elderly_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "alertTitle", column = "alert_title"),
        @Result(property = "alertDescription", column = "alert_description"),
        @Result(property = "triggerValue", column = "trigger_value"),
        @Result(property = "normalRange", column = "normal_range"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "resolvedAt", column = "resolved_at"),
        @Result(property = "resolvedBy", column = "resolved_by"),
        @Result(property = "resolvedNote", column = "resolved_note"),
        @Result(property = "observationId", column = "observation_id")
    })
    List<HealthAlert> findByStatus(@Param("status") String status);

    /**
     * 根据预警级别查询预警记录
     */
    @Select("SELECT alert_id, elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, " +
            "status, created_at, updated_at, resolved_at, resolved_by, " +
            "resolved_note, observation_id FROM health_alert " +
            "WHERE alert_level = #{alertLevel} ORDER BY created_at DESC")
    @Results({
        @Result(property = "alertId", column = "alert_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "elderlyName", column = "elderly_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "alertTitle", column = "alert_title"),
        @Result(property = "alertDescription", column = "alert_description"),
        @Result(property = "triggerValue", column = "trigger_value"),
        @Result(property = "normalRange", column = "normal_range"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "resolvedAt", column = "resolved_at"),
        @Result(property = "resolvedBy", column = "resolved_by"),
        @Result(property = "resolvedNote", column = "resolved_note"),
        @Result(property = "observationId", column = "observation_id")
    })
    List<HealthAlert> findByAlertLevel(@Param("alertLevel") String alertLevel);

    /**
     * 根据时间范围查询预警记录
     */
    @Select("SELECT alert_id, elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, " +
            "status, created_at, updated_at, resolved_at, resolved_by, " +
            "resolved_note, observation_id FROM health_alert " +
            "WHERE created_at BETWEEN #{startTime} AND #{endTime} ORDER BY created_at DESC")
    @Results({
        @Result(property = "alertId", column = "alert_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "elderlyName", column = "elderly_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "alertTitle", column = "alert_title"),
        @Result(property = "alertDescription", column = "alert_description"),
        @Result(property = "triggerValue", column = "trigger_value"),
        @Result(property = "normalRange", column = "normal_range"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "resolvedAt", column = "resolved_at"),
        @Result(property = "resolvedBy", column = "resolved_by"),
        @Result(property = "resolvedNote", column = "resolved_note"),
        @Result(property = "observationId", column = "observation_id")
    })
    List<HealthAlert> findByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 根据预警类型查询预警记录
     */
    @Select("SELECT alert_id, elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, " +
            "status, created_at, updated_at, resolved_at, resolved_by, " +
            "resolved_note, observation_id FROM health_alert " +
            "WHERE alert_type = #{alertType} ORDER BY created_at DESC")
    @Results({
        @Result(property = "alertId", column = "alert_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "elderlyName", column = "elderly_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "alertTitle", column = "alert_title"),
        @Result(property = "alertDescription", column = "alert_description"),
        @Result(property = "triggerValue", column = "trigger_value"),
        @Result(property = "normalRange", column = "normal_range"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "resolvedAt", column = "resolved_at"),
        @Result(property = "resolvedBy", column = "resolved_by"),
        @Result(property = "resolvedNote", column = "resolved_note"),
        @Result(property = "observationId", column = "observation_id")
    })
    List<HealthAlert> findByAlertType(@Param("alertType") String alertType);

    /**
     * 根据老人ID和预警类型查询最新预警
     */
    @Select("SELECT COUNT(1) FROM health_alert WHERE status = 'ACTIVE'")
    Long countActive();
    
    /**
     * 查询总预警数量
     */
    @Select("SELECT COUNT(1) FROM health_alert")
    Long countAll();
    
    /**
     * 根据ID查询预警记录
     */
    @Select("SELECT alert_id, elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, " +
            "status, created_at, updated_at, resolved_at, resolved_by, " +
            "resolved_note, observation_id FROM health_alert " +
            "WHERE alert_id = #{alertId}")
    @Results({
        @Result(property = "alertId", column = "alert_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "elderlyName", column = "elderly_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "alertTitle", column = "alert_title"),
        @Result(property = "alertDescription", column = "alert_description"),
        @Result(property = "triggerValue", column = "trigger_value"),
        @Result(property = "normalRange", column = "normal_range"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "resolvedAt", column = "resolved_at"),
        @Result(property = "resolvedBy", column = "resolved_by"),
        @Result(property = "resolvedNote", column = "resolved_note"),
        @Result(property = "observationId", column = "observation_id")
    })
    HealthAlert findById(@Param("alertId") Long alertId);

    /**
     * 根据老人ID和预警类型查询最新预警
     */
    @Select("SELECT alert_id, elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, " +
            "status, created_at, updated_at, resolved_at, resolved_by, " +
            "resolved_note, observation_id FROM health_alert " +
            "WHERE elderly_id = #{elderlyId} AND alert_type = #{alertType} AND status = 'ACTIVE' " +
            "ORDER BY created_at DESC LIMIT 1")
    @Results({
        @Result(property = "alertId", column = "alert_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "elderlyName", column = "elderly_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "alertTitle", column = "alert_title"),
        @Result(property = "alertDescription", column = "alert_description"),
        @Result(property = "triggerValue", column = "trigger_value"),
        @Result(property = "normalRange", column = "normal_range"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "resolvedAt", column = "resolved_at"),
        @Result(property = "resolvedBy", column = "resolved_by"),
        @Result(property = "resolvedNote", column = "resolved_note"),
        @Result(property = "observationId", column = "observation_id")
    })
    HealthAlert findLatestByElderlyIdAndType(@Param("elderlyId") int elderlyId,
                                           @Param("alertType") String alertType);

    /**
     * 插入预警记录
     */
    @Insert("INSERT INTO health_alert (elderly_id, elderly_name, alert_type, alert_level, " +
            "alert_title, alert_description, trigger_value, normal_range, status, " +
            "created_at, updated_at, observation_id) VALUES (#{elderlyId}, #{elderlyName}, " +
            "#{alertType}, #{alertLevel}, #{alertTitle}, #{alertDescription}, " +
            "#{triggerValue}, #{normalRange}, #{status}, #{createdAt}, #{updatedAt}, #{observationId})")
    @Options(useGeneratedKeys = true, keyProperty = "alertId")
    int insert(HealthAlert healthAlert);

    /**
     * 更新预警状态
     */
    @Update("UPDATE health_alert SET status = #{status}, updated_at = #{updatedAt}, " +
            "resolved_at = #{resolvedAt}, resolved_by = #{resolvedBy}, " +
            "resolved_note = #{resolvedNote} WHERE alert_id = #{alertId}")
    int updateStatus(HealthAlert healthAlert);

    /**
     * 统计各级别预警数量
     */
    @Select("SELECT alert_level as level, COUNT(1) as count FROM health_alert " +
            "WHERE status = 'ACTIVE' GROUP BY alert_level")
    List<Map<String, Object>> countByLevel();

    /**
     * 统计各类型预警数量
     */
    @Select("SELECT alert_type as type, COUNT(1) as count FROM health_alert " +
            "WHERE status = 'ACTIVE' GROUP BY alert_type")
    List<Map<String, Object>> countByType();
    
    /**
     * 根据老人ID删除所有健康预警记录
     */
    @Delete("DELETE FROM health_alert WHERE elderly_id = #{elderlyId}")
    int deleteAlertsByElderlyId(@Param("elderlyId") int elderlyId);
}