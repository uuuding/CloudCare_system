package com.cloudcare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.InterventionTemplate;

import java.util.List;

/**
 * 干预模板服务接口
 *
 * @author CloudCare
 * @since 2024-01-15
 */
public interface InterventionTemplateService extends IService<InterventionTemplate> {

    /**
     * 分页查询干预模板
     *
     * @param current 当前页
     * @param size 页大小
     * @param templateCategory 模板分类
     * @param alertType 预警类型
     * @param isActive 是否启用
     * @return 分页结果
     */
    IPage<InterventionTemplate> getTemplatePage(Long current, Long size, String templateName, String templateCategory,
                                                String alertType, Boolean isActive);

    /**
     * 根据预警类型查询推荐模板
     *
     * @param alertType 预警类型
     * @return 模板列表
     */
    List<InterventionTemplate> getRecommendedTemplates(String alertType);

    /**
     * 根据分类查询启用的模板
     *
     * @param templateCategory 模板分类
     * @return 模板列表
     */
    List<InterventionTemplate> getTemplatesByCategory(String templateCategory);

    /**
     * 查询热门模板（使用次数最多）
     *
     * @param limit 限制数量
     * @return 模板列表
     */
    List<InterventionTemplate> getPopularTemplates(Integer limit);

    /**
     * 增加模板使用次数
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean incrementUsageCount(Long templateId);

    /**
     * 启用/禁用模板
     *
     * @param templateId 模板ID
     * @param isActive 是否启用
     * @return 是否成功
     */
    boolean updateActiveStatus(Long templateId, Boolean isActive);

    /**
     * 查询所有模板分类
     *
     * @return 分类列表
     */
    List<String> getAllCategories();

    /**
     * 查询所有预警类型
     *
     * @return 预警类型列表
     */
    List<String> getAllAlertTypes();

    /**
     * 根据关键词搜索模板
     *
     * @param keyword 关键词
     * @return 模板列表
     */
    List<InterventionTemplate> searchTemplates(String keyword);

    /**
     * 创建干预模板
     *
     * @param template 模板信息
     * @return 是否成功
     */
    boolean createTemplate(InterventionTemplate template);

    /**
     * 更新干预模板
     *
     * @param template 模板信息
     * @return 是否成功
     */
    boolean updateTemplate(InterventionTemplate template);

    /**
     * 复制模板
     *
     * @param templateId 原模板ID
     * @param newTemplateName 新模板名称
     * @return 新模板ID
     */
    Long copyTemplate(Long templateId, String newTemplateName);

    /**
     * 批量启用/禁用模板
     *
     * @param templateIds 模板ID列表
     * @param isActive 是否启用
     * @return 是否成功
     */
    boolean batchUpdateActiveStatus(List<Long> templateIds, Boolean isActive);

    /**
     * 获取模板使用统计
     *
     * @return 统计信息
     */
    List<InterventionTemplate> getTemplateUsageStatistics();
}