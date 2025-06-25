package com.cloudcare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.domain.HealthWarning;

import java.util.List;

/**
 * 健康预警服务接口
 *
 * @author cloudcare
 */
public interface HealthWarningService extends IService<HealthWarning> {

    /**
     * 分页查询健康预警列表
     *
     * @param page 分页参数
     * @param healthWarning 查询条件
     * @return 健康预警列表
     */
    IPage<HealthWarning> pageHealthWarnings(IPage<HealthWarning> page, HealthWarning healthWarning);
    
    /**
     * 获取健康预警详情
     *
     * @param warningId 预警ID
     * @return 健康预警详情
     */
    HealthWarning getHealthWarningById(Long warningId);
    
    /**
     * 新增健康预警
     *
     * @param healthWarning 健康预警信息
     * @return 结果
     */
    boolean addHealthWarning(HealthWarning healthWarning);
    
    /**
     * 修改健康预警
     *
     * @param healthWarning 健康预警信息
     * @return 结果
     */
    boolean updateHealthWarning(HealthWarning healthWarning);
    
    /**
     * 删除健康预警
     *
     * @param warningIds 预警ID数组
     * @return 结果
     */
    boolean deleteHealthWarningByIds(Long[] warningIds);
    
    /**
     * 处理健康预警
     *
     * @param healthWarning 健康预警信息
     * @return 结果
     */
    boolean processHealthWarning(HealthWarning healthWarning);
    
    /**
     * 获取未处理的健康预警数量
     *
     * @return 未处理的健康预警数量
     */
    int getUnprocessedWarningCount();
    
    /**
     * 获取老人的健康预警列表
     *
     * @param elderId 老人ID
     * @return 健康预警列表
     */
    List<HealthWarning> getWarningsByElderId(Long elderId);
    
    /**
     * 获取最近的健康预警列表
     *
     * @param limit 限制数量
     * @return 最近的健康预警列表
     */
    List<HealthWarning> getRecentWarnings(int limit);
}