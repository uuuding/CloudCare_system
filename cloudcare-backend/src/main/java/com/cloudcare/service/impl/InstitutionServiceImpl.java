package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.Institution;
import com.cloudcare.mapper.InstitutionMapper;
import com.cloudcare.service.InstitutionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构信息服务实现类
 *
 * @author cloudcare
 */
@Slf4j
@Service
public class InstitutionServiceImpl extends ServiceImpl<InstitutionMapper, Institution> implements InstitutionService {

    @Autowired
    private InstitutionMapper institutionMapper;

    @Override
    public Page<Institution> selectInstitutionPage(Page<Institution> page, Institution institution) {
        LambdaQueryWrapper<Institution> queryWrapper = new LambdaQueryWrapper<>();
        
        // 机构名称
        if (StringUtils.isNotBlank(institution.getName())) {
            queryWrapper.like(Institution::getName, institution.getName());
        }
        
        // 机构类型
        if (StringUtils.isNotBlank(institution.getType())) {
            queryWrapper.eq(Institution::getType, institution.getType());
        }
        
        // 联系人
        if (StringUtils.isNotBlank(institution.getContactPerson())) {
            queryWrapper.like(Institution::getContactPerson, institution.getContactPerson());
        }
        
        // 联系电话
        if (StringUtils.isNotBlank(institution.getContactPhone())) {
            queryWrapper.like(Institution::getContactPhone, institution.getContactPhone());
        }
        
        // 地址
        if (StringUtils.isNotBlank(institution.getAddress())) {
            queryWrapper.like(Institution::getAddress, institution.getAddress());
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc(Institution::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    public Institution selectInstitutionById(Long institutionId) {
        return this.getById(institutionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertInstitution(Institution institution) {
        try {
            // 设置创建时间
            institution.setCreateTime(LocalDateTime.now());
            institution.setUpdateTime(LocalDateTime.now());
            
            // 校验机构名称唯一性
            if (!checkInstitutionNameUnique(institution)) {
                log.warn("机构名称已存在: {}", institution.getName());
                return false;
            }
            
            // 校验床位数据合理性
            if (!validateBedData(institution)) {
                return false;
            }
            
            return this.save(institution);
        } catch (Exception e) {
            log.error("新增机构信息失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInstitution(Institution institution) {
        try {
            // 设置更新时间
            institution.setUpdateTime(LocalDateTime.now());
            
            // 校验机构名称唯一性
            if (!checkInstitutionNameUnique(institution)) {
                log.warn("机构名称已存在: {}", institution.getName());
                return false;
            }
            
            // 校验床位数据合理性
            if (!validateBedData(institution)) {
                return false;
            }
            
            return this.updateById(institution);
        } catch (Exception e) {
            log.error("修改机构信息失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 校验床位数据合理性
     */
    private boolean validateBedData(Institution institution) {
        if (institution.getBedTotal() != null && institution.getBedAvailable() != null) {
            if (institution.getBedAvailable() > institution.getBedTotal()) {
                log.warn("可用床位数({})不能大于总床位数({})", 
                    institution.getBedAvailable(), institution.getBedTotal());
                return false;
            }
            if (institution.getBedTotal() < 0 || institution.getBedAvailable() < 0) {
                log.warn("床位数不能为负数");
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInstitutionByIds(Long[] institutionIds) {
        try {
            if (institutionIds == null || institutionIds.length == 0) {
                log.warn("删除机构ID列表为空");
                return false;
            }
            return this.removeByIds(Arrays.asList(institutionIds));
        } catch (Exception e) {
            log.error("批量删除机构信息失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInstitutionById(Long institutionId) {
        try {
            if (institutionId == null) {
                log.warn("删除机构ID为空");
                return false;
            }
            return this.removeById(institutionId);
        } catch (Exception e) {
            log.error("删除机构信息失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getInstitutionStatistics() {
        try {
            Map<String, Object> statistics = institutionMapper.getInstitutionStatistics();
            if (statistics == null) {
                statistics = createDefaultInstitutionStats();
            }
            return statistics;
        } catch (Exception e) {
            log.error("获取机构统计信息失败", e);
            return createDefaultInstitutionStats();
        }
    }
    
    private Map<String, Object> createDefaultInstitutionStats() {
        Map<String, Object> defaultStats = new HashMap<>();
        defaultStats.put("totalCount", 0);
        defaultStats.put("activeCount", 0);
        defaultStats.put("pendingCount", 0);
        defaultStats.put("suspendedCount", 0);
        return defaultStats;
    }

    @Override
    public Map<String, Object> getBedStatistics() {
        try {
            Map<String, Object> statistics = institutionMapper.getBedStatistics();
            if (statistics == null) {
                statistics = createDefaultBedStats();
            }
            return statistics;
        } catch (Exception e) {
            log.error("获取床位统计信息失败", e);
            return createDefaultBedStats();
        }
    }
    
    private Map<String, Object> createDefaultBedStats() {
        Map<String, Object> defaultStats = new HashMap<>();
        defaultStats.put("totalBeds", 0);
        defaultStats.put("availableBeds", 0);
        defaultStats.put("occupiedBeds", 0);
        return defaultStats;
    }

    @Override
    public List<Institution> selectByNameLike(String name) {
        try {
            if (StringUtils.isBlank(name)) {
                log.warn("查询机构名称参数为空");
                return null;
            }
            return institutionMapper.selectByNameLike(name);
        } catch (Exception e) {
            log.error("根据名称查询机构失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getOccupancyRanking(int limit) {
        try {
            if (limit <= 0) {
                log.warn("入住率排行榜查询限制数量无效: {}", limit);
                return null;
            }
            return institutionMapper.getOccupancyRanking(limit);
        } catch (Exception e) {
            log.error("获取入住率排行榜失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean checkInstitutionNameUnique(Institution institution) {
        try {
            LambdaQueryWrapper<Institution> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Institution::getName, institution.getName());
            
            // 如果是更新操作，排除当前记录
            if (institution.getInstitutionId() != null) {
                queryWrapper.ne(Institution::getInstitutionId, institution.getInstitutionId());
            }
            
            long count = this.count(queryWrapper);
            return count == 0;
        } catch (Exception e) {
            log.error("校验机构名称唯一性失败", e);
            return false;
        }
    }
}