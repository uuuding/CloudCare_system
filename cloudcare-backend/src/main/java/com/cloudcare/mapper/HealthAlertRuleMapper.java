package com.cloudcare.mapper;

import com.cloudcare.entity.HealthAlertRule;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface HealthAlertRuleMapper {

    /**
     * 根据预警类型查询启用的规则
     */
    @Select("SELECT rule_id, rule_name, alert_type, alert_level, min_threshold, " +
            "max_threshold, message_template, min_age, max_age, gender, " +
            "enabled, created_at, updated_at FROM health_alert_rule " +
            "WHERE alert_type = #{alertType} AND enabled = true ORDER BY alert_level DESC")
    @Results({
        @Result(property = "ruleId", column = "rule_id"),
        @Result(property = "ruleName", column = "rule_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "minThreshold", column = "min_threshold"),
        @Result(property = "maxThreshold", column = "max_threshold"),
        @Result(property = "messageTemplate", column = "message_template"),
        @Result(property = "minAge", column = "min_age"),
        @Result(property = "maxAge", column = "max_age"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "enabled", column = "enabled"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<HealthAlertRule> findEnabledByType(@Param("alertType") String alertType);

    /**
     * 查询所有启用的规则
     */
    @Select("SELECT rule_id, rule_name, alert_type, alert_level, min_threshold, " +
            "max_threshold, message_template, min_age, max_age, gender, " +
            "enabled, created_at, updated_at FROM health_alert_rule " +
            "WHERE enabled = true ORDER BY alert_type, alert_level DESC")
    @Results({
        @Result(property = "ruleId", column = "rule_id"),
        @Result(property = "ruleName", column = "rule_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "minThreshold", column = "min_threshold"),
        @Result(property = "maxThreshold", column = "max_threshold"),
        @Result(property = "messageTemplate", column = "message_template"),
        @Result(property = "minAge", column = "min_age"),
        @Result(property = "maxAge", column = "max_age"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "enabled", column = "enabled"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<HealthAlertRule> findAllEnabled();

    /**
     * 查询所有规则
     */
    @Select("SELECT rule_id, rule_name, alert_type, alert_level, min_threshold, " +
            "max_threshold, message_template, min_age, max_age, gender, " +
            "enabled, created_at, updated_at FROM health_alert_rule " +
            "ORDER BY alert_type, alert_level DESC")
    @Results({
        @Result(property = "ruleId", column = "rule_id"),
        @Result(property = "ruleName", column = "rule_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "minThreshold", column = "min_threshold"),
        @Result(property = "maxThreshold", column = "max_threshold"),
        @Result(property = "messageTemplate", column = "message_template"),
        @Result(property = "minAge", column = "min_age"),
        @Result(property = "maxAge", column = "max_age"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "enabled", column = "enabled"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<HealthAlertRule> findAll();

    /**
     * 根据年龄和性别查询适用的规则
     */
    @SelectProvider(type = HealthAlertRuleProvider.class, method = "findApplicableRules")
    @Results({
        @Result(property = "ruleId", column = "rule_id"),
        @Result(property = "ruleName", column = "rule_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "minThreshold", column = "min_threshold"),
        @Result(property = "maxThreshold", column = "max_threshold"),
        @Result(property = "messageTemplate", column = "message_template"),
        @Result(property = "minAge", column = "min_age"),
        @Result(property = "maxAge", column = "max_age"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "enabled", column = "enabled"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<HealthAlertRule> findApplicableRules(@Param("alertType") String alertType);

    /**
     * 插入预警规则
     */
    @Insert("INSERT INTO health_alert_rule (rule_name, alert_type, alert_level, " +
            "min_threshold, max_threshold, message_template, min_age, max_age, " +
            "gender, enabled, created_at, updated_at) VALUES (#{ruleName}, " +
            "#{alertType}, #{alertLevel}, #{minThreshold}, #{maxThreshold}, " +
            "#{messageTemplate}, #{minAge}, #{maxAge}, #{gender}, #{enabled}, " +
            "#{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "ruleId")
    int insert(HealthAlertRule healthAlertRule);

    /**
     * 更新预警规则
     */
    @Update("UPDATE health_alert_rule SET rule_name = #{ruleName}, " +
            "alert_type = #{alertType}, alert_level = #{alertLevel}, " +
            "min_threshold = #{minThreshold}, max_threshold = #{maxThreshold}, " +
            "message_template = #{messageTemplate}, min_age = #{minAge}, " +
            "max_age = #{maxAge}, gender = #{gender}, enabled = #{enabled}, " +
            "updated_at = #{updatedAt} WHERE rule_id = #{ruleId}")
    int update(HealthAlertRule healthAlertRule);

    /**
     * 启用/禁用预警规则
     */
    @Update("UPDATE health_alert_rule SET enabled = #{enabled}, " +
            "updated_at = #{updatedAt} WHERE rule_id = #{ruleId}")
    int updateStatus(@Param("ruleId") Long ruleId, 
                     @Param("enabled") Boolean enabled,
                     @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * 查询规则总数
     */
    @Select("SELECT COUNT(*) FROM health_alert_rule")
    Long countAll();

    /**
     * 删除预警规则
     */
    @Delete("DELETE FROM health_alert_rule WHERE rule_id = #{ruleId}")
    int deleteById(@Param("ruleId") Long ruleId);

    /**
     * 根据ID查询规则
     */
    @Select("SELECT rule_id, rule_name, alert_type, alert_level, min_threshold, " +
            "max_threshold, message_template, min_age, max_age, gender, " +
            "enabled, created_at, updated_at FROM health_alert_rule " +
            "WHERE rule_id = #{ruleId}")
    @Results({
        @Result(property = "ruleId", column = "rule_id"),
        @Result(property = "ruleName", column = "rule_name"),
        @Result(property = "alertType", column = "alert_type"),
        @Result(property = "alertLevel", column = "alert_level"),
        @Result(property = "minThreshold", column = "min_threshold"),
        @Result(property = "maxThreshold", column = "max_threshold"),
        @Result(property = "messageTemplate", column = "message_template"),
        @Result(property = "minAge", column = "min_age"),
        @Result(property = "maxAge", column = "max_age"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "enabled", column = "enabled"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    HealthAlertRule findById(@Param("ruleId") Long ruleId);

    /**
     * 检查规则名称是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM health_alert_rule WHERE rule_name = #{ruleName}")
    boolean existsByName(@Param("ruleName") String ruleName);

    /**
     * 检查规则名称是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) > 0 FROM health_alert_rule " +
            "WHERE rule_name = #{ruleName} AND rule_id != #{ruleId}")
    boolean existsByNameExcludeId(@Param("ruleName") String ruleName, 
                                 @Param("ruleId") Long ruleId);

    /**
     * SQL提供者类，用于动态SQL
     */
    class HealthAlertRuleProvider {
        public String findApplicableRules(@Param("alertType") String alertType) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT rule_id, rule_name, alert_type, alert_level, min_threshold, ");
            sql.append("max_threshold, message_template, min_age, max_age, gender, ");
            sql.append("enabled, created_at, updated_at FROM health_alert_rule ");
            sql.append("WHERE enabled = true ");
            
            if (alertType != null && !alertType.isEmpty()) {
                sql.append("AND alert_type = #{alertType} ");
            }
            
            sql.append("ORDER BY alert_level DESC");
            return sql.toString();
        }
    }
}