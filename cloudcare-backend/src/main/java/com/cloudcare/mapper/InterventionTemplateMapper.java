package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.entity.InterventionTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 干预模板 Mapper 接口
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Mapper
public interface InterventionTemplateMapper extends BaseMapper<InterventionTemplate> {

    /**
     * 分页查询干预模板
     *
     * @param page 分页参数
     * @param templateCategory 模板分类
     * @param alertType 预警类型
     * @param isActive 是否启用
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT * FROM intervention_template WHERE 1=1" +
            "<if test='templateName != null and templateName != &quot;&quot;'> AND template_name LIKE CONCAT('%', #{templateName}, '%')</if>" +
            "<if test='templateCategory != null and templateCategory != &quot;&quot;'> AND template_category = #{templateCategory}</if>" +
            "<if test='alertType != null and alertType != &quot;&quot;'> AND alert_type = #{alertType}</if>" +
            "<if test='isActive != null'> AND is_active = #{isActive}</if>" +
            " ORDER BY usage_count DESC, created_time DESC" +
            "</script>")
    IPage<InterventionTemplate> selectTemplatePage(Page<InterventionTemplate> page,
                                                   @Param("templateName") String templateName,
                                                   @Param("templateCategory") String templateCategory,
                                                   @Param("alertType") String alertType,
                                                   @Param("isActive") Boolean isActive);

    /**
     * 根据预警类型查询推荐模板
     *
     * @param alertType 预警类型
     * @return 模板列表
     */
    @Select("SELECT * FROM intervention_template WHERE alert_type = #{alertType} AND is_active = true ORDER BY usage_count DESC LIMIT 5")
    List<InterventionTemplate> selectRecommendedTemplates(@Param("alertType") String alertType);

    /**
     * 根据分类查询启用的模板
     *
     * @param templateCategory 模板分类
     * @return 模板列表
     */
    @Select("SELECT * FROM intervention_template WHERE template_category = #{templateCategory} AND is_active = true ORDER BY usage_count DESC")
    List<InterventionTemplate> selectByCategory(@Param("templateCategory") String templateCategory);

    /**
     * 查询热门模板（使用次数最多）
     *
     * @param limit 限制数量
     * @return 模板列表
     */
    @Select("SELECT * FROM intervention_template WHERE is_active = true ORDER BY usage_count DESC LIMIT #{limit}")
    List<InterventionTemplate> selectPopularTemplates(@Param("limit") Integer limit);

    /**
     * 增加模板使用次数
     *
     * @param templateId 模板ID
     * @return 更新行数
     */
    @Update("UPDATE intervention_template SET usage_count = usage_count + 1, updated_time = NOW() WHERE template_id = #{templateId}")
    int incrementUsageCount(@Param("templateId") Long templateId);

    /**
     * 启用/禁用模板
     *
     * @param templateId 模板ID
     * @param isActive 是否启用
     * @return 更新行数
     */
    @Update("UPDATE intervention_template SET is_active = #{isActive}, updated_time = NOW() WHERE template_id = #{templateId}")
    int updateActiveStatus(@Param("templateId") Long templateId, @Param("isActive") Boolean isActive);

    /**
     * 查询所有模板分类
     *
     * @return 分类列表
     */
    @Select("SELECT DISTINCT template_category FROM intervention_template WHERE is_active = true ORDER BY template_category")
    List<String> selectAllCategories();

    /**
     * 查询所有预警类型
     *
     * @return 预警类型列表
     */
    @Select("SELECT DISTINCT alert_type FROM intervention_template WHERE is_active = true AND alert_type IS NOT NULL ORDER BY alert_type")
    List<String> selectAllAlertTypes();

    /**
     * 根据关键词搜索模板
     *
     * @param keyword 关键词
     * @return 模板列表
     */
    @Select("SELECT * FROM intervention_template WHERE is_active = true AND (" +
            "template_name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "template_content LIKE CONCAT('%', #{keyword}, '%') OR " +
            "implementation_guide LIKE CONCAT('%', #{keyword}, '%')" +
            ") ORDER BY usage_count DESC")
    List<InterventionTemplate> searchTemplates(@Param("keyword") String keyword);
}