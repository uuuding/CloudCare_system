package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.domain.HealthWarning;
import com.cloudcare.mapper.HealthWarningMapper;
import com.cloudcare.service.HealthWarningService;
import com.cloudcare.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 健康预警服务实现类
 *
 * @author cloudcare
 */
@Service
public class HealthWarningServiceImpl extends ServiceImpl<HealthWarningMapper, HealthWarning> implements HealthWarningService {

    @Override
    public IPage<HealthWarning> pageHealthWarnings(IPage<HealthWarning> page, HealthWarning healthWarning) {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        
        // 构建查询条件
        if (healthWarning.getElderId() != null) {
            queryWrapper.eq(HealthWarning::getElderId, healthWarning.getElderId());
        }
        
        if (healthWarning.getWarningType() != null) {
            queryWrapper.eq(HealthWarning::getWarningType, healthWarning.getWarningType());
        }
        
        if (healthWarning.getWarningLevel() != null) {
            queryWrapper.eq(HealthWarning::getWarningLevel, healthWarning.getWarningLevel());
        }
        
        if (healthWarning.getProcessStatus() != null) {
            queryWrapper.eq(HealthWarning::getProcessStatus, healthWarning.getProcessStatus());
        }
        
        // 按预警时间降序排序
        queryWrapper.orderByDesc(HealthWarning::getWarningTime);
        
        return page(page, queryWrapper);
    }

    @Override
    public HealthWarning getHealthWarningById(Long warningId) {
        return getById(warningId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addHealthWarning(HealthWarning healthWarning) {
        // 设置预警时间
        if (healthWarning.getWarningTime() == null) {
            healthWarning.setWarningTime(LocalDateTime.now());
        }
        
        // 设置处理状态为未处理
        healthWarning.setProcessStatus(0);
        
        // 设置创建者和创建时间
        String username = SecurityUtil.getCurrentUsername();
        healthWarning.setCreateBy(username);
        healthWarning.setCreateTime(LocalDateTime.now());
        
        return save(healthWarning);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHealthWarning(HealthWarning healthWarning) {
        // 设置更新者和更新时间
        String username = SecurityUtil.getCurrentUsername();
        healthWarning.setUpdateBy(username);
        healthWarning.setUpdateTime(LocalDateTime.now());
        
        return updateById(healthWarning);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHealthWarningByIds(Long[] warningIds) {
        return removeByIds(Arrays.asList(warningIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processHealthWarning(HealthWarning healthWarning) {
        // 设置处理状态为已处理
        healthWarning.setProcessStatus(1);
        
        // 设置处理时间
        healthWarning.setProcessTime(LocalDateTime.now());
        
        // 设置处理人ID
        // 由于SecurityUtil中没有getUserId方法，暂时使用null
        // TODO: 实现获取当前用户ID的方法
        healthWarning.setProcessUserId(null);
        
        // 设置更新者和更新时间
        String username = SecurityUtil.getCurrentUsername();
        healthWarning.setUpdateBy(username);
        healthWarning.setUpdateTime(LocalDateTime.now());
        
        return updateById(healthWarning);
    }

    @Override
    public int getUnprocessedWarningCount() {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthWarning::getProcessStatus, 0);
        
        Long count = count(queryWrapper);
        return count.intValue();
    }

    @Override
    public List<HealthWarning> getWarningsByElderId(Long elderId) {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthWarning::getElderId, elderId);
        queryWrapper.orderByDesc(HealthWarning::getWarningTime);
        
        return list(queryWrapper);
    }

    @Override
    public List<HealthWarning> getRecentWarnings(int limit) {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(HealthWarning::getWarningTime);
        queryWrapper.last("LIMIT " + limit);
        
        return list(queryWrapper);
    }
}