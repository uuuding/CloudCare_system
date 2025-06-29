package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.InterventionTemplate;
import com.cloudcare.mapper.InterventionTemplateMapper;
import com.cloudcare.service.InterventionTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 干预模板服务实现类
 *
 * @author CloudCare
 * @since 2024-01-15
 */
@Slf4j
@Service
public class InterventionTemplateServiceImpl extends ServiceImpl<InterventionTemplateMapper, InterventionTemplate> implements InterventionTemplateService {

    @Override
    public IPage<InterventionTemplate> getTemplatePage(Long current, Long size, String templateName, String templateCategory, 
                                                      String alertType, Boolean isActive) {
        Page<InterventionTemplate> page = new Page<>(current, size);
        return baseMapper.selectTemplatePage(page, templateName, templateCategory, alertType, isActive);
    }

    @Override
    public List<InterventionTemplate> getRecommendedTemplates(String alertType) {
        return baseMapper.selectRecommendedTemplates(alertType);
    }

    @Override
    public List<InterventionTemplate> getTemplatesByCategory(String templateCategory) {
        return baseMapper.selectByCategory(templateCategory);
    }

    @Override
    public List<InterventionTemplate> getPopularTemplates(Integer limit) {
        return baseMapper.selectPopularTemplates(limit);
    }

    @Override
    @Transactional
    public boolean incrementUsageCount(Long templateId) {
        try {
            int result = baseMapper.incrementUsageCount(templateId);
            if (result > 0) {
                log.info("增加模板使用次数成功，模板ID: {}", templateId);
            }
            return result > 0;
        } catch (Exception e) {
            log.error("增加模板使用次数失败，模板ID: {}", templateId, e);
            throw new RuntimeException("增加使用次数失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateActiveStatus(Long templateId, Boolean isActive) {
        try {
            int result = baseMapper.updateActiveStatus(templateId, isActive);
            if (result > 0) {
                log.info("更新模板状态成功，模板ID: {}, 状态: {}", templateId, isActive ? "启用" : "禁用");
            }
            return result > 0;
        } catch (Exception e) {
            log.error("更新模板状态失败，模板ID: {}", templateId, e);
            throw new RuntimeException("更新模板状态失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> getAllCategories() {
        return baseMapper.selectAllCategories();
    }

    @Override
    public List<String> getAllAlertTypes() {
        return baseMapper.selectAllAlertTypes();
    }

    @Override
    public List<InterventionTemplate> searchTemplates(String keyword) {
        return baseMapper.searchTemplates(keyword);
    }

    @Override
    @Transactional
    public boolean createTemplate(InterventionTemplate template) {
        try {
            // 设置默认值
            if (template.getIsActive() == null) {
                template.setIsActive(true);
            }
            if (template.getUsageCount() == null) {
                template.setUsageCount(0);
            }
            if (template.getCreatedTime() == null) {
                template.setCreatedTime(LocalDateTime.now());
            }
            if (template.getCreatedBy() == null) {
                template.setCreatedBy("系统管理员");
            }
            
            boolean result = save(template);
            if (result) {
                log.info("创建干预模板成功，模板ID: {}, 模板名称: {}", template.getTemplateId(), template.getTemplateName());
            }
            return result;
        } catch (Exception e) {
            log.error("创建干预模板失败", e);
            throw new RuntimeException("创建模板失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateTemplate(InterventionTemplate template) {
        try {
            template.setUpdatedTime(LocalDateTime.now());
            boolean result = updateById(template);
            if (result) {
                log.info("更新干预模板成功，模板ID: {}", template.getTemplateId());
            }
            return result;
        } catch (Exception e) {
            log.error("更新干预模板失败，模板ID: {}", template.getTemplateId(), e);
            throw new RuntimeException("更新模板失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Long copyTemplate(Long templateId, String newTemplateName) {
        try {
            InterventionTemplate originalTemplate = getById(templateId);
            if (originalTemplate == null) {
                throw new RuntimeException("原模板不存在");
            }
            
            InterventionTemplate newTemplate = new InterventionTemplate();
            BeanUtils.copyProperties(originalTemplate, newTemplate);
            
            // 重置关键字段
            newTemplate.setTemplateId(null);
            newTemplate.setTemplateName(newTemplateName);
            newTemplate.setUsageCount(0);
            newTemplate.setCreatedBy("复制创建");
            newTemplate.setCreatedTime(LocalDateTime.now());
            newTemplate.setUpdatedTime(LocalDateTime.now());
            
            boolean result = createTemplate(newTemplate);
            if (result) {
                log.info("复制干预模板成功，原模板ID: {}, 新模板ID: {}", templateId, newTemplate.getTemplateId());
                return newTemplate.getTemplateId();
            }
            
            return null;
        } catch (Exception e) {
            log.error("复制干预模板失败，原模板ID: {}", templateId, e);
            throw new RuntimeException("复制模板失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean batchUpdateActiveStatus(List<Long> templateIds, Boolean isActive) {
        try {
            for (Long templateId : templateIds) {
                updateActiveStatus(templateId, isActive);
            }
            log.info("批量更新模板状态成功，数量: {}, 状态: {}", templateIds.size(), isActive ? "启用" : "禁用");
            return true;
        } catch (Exception e) {
            log.error("批量更新模板状态失败", e);
            throw new RuntimeException("批量更新状态失败: " + e.getMessage());
        }
    }

    @Override
    public List<InterventionTemplate> getTemplateUsageStatistics() {
        try {
            // 查询所有启用的模板，按使用次数排序
            return getPopularTemplates(100);
        } catch (Exception e) {
            log.error("获取模板使用统计失败", e);
            throw new RuntimeException("获取统计信息失败: " + e.getMessage());
        }
    }
}