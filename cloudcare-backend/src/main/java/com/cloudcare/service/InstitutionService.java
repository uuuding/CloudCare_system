package com.cloudcare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.Institution;

import java.util.List;
import java.util.Map;

/**
 * 机构信息服务接口
 *
 * @author cloudcare
 */
public interface InstitutionService extends IService<Institution> {

    /**
     * 分页查询机构信息
     *
     * @param page 分页参数
     * @param institution 查询条件
     * @return 分页结果
     */
    Page<Institution> selectInstitutionPage(Page<Institution> page, Institution institution);

    /**
     * 根据ID查询机构信息
     *
     * @param institutionId 机构ID
     * @return 机构信息
     */
    Institution selectInstitutionById(Long institutionId);

    /**
     * 新增机构信息
     *
     * @param institution 机构信息
     * @return 结果
     */
    boolean insertInstitution(Institution institution);

    /**
     * 修改机构信息
     *
     * @param institution 机构信息
     * @return 结果
     */
    boolean updateInstitution(Institution institution);

    /**
     * 批量删除机构信息
     *
     * @param institutionIds 需要删除的机构ID
     * @return 结果
     */
    boolean deleteInstitutionByIds(Long[] institutionIds);

    /**
     * 删除机构信息
     *
     * @param institutionId 机构ID
     * @return 结果
     */
    boolean deleteInstitutionById(Long institutionId);

    /**
     * 获取机构统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getInstitutionStatistics();

    /**
     * 获取床位统计信息
     *
     * @return 床位统计
     */
    Map<String, Object> getBedStatistics();

    /**
     * 根据名称模糊查询机构
     *
     * @param name 机构名称
     * @return 机构列表
     */
    List<Institution> selectByNameLike(String name);

    /**
     * 获取入住率排行榜
     *
     * @param limit 限制数量
     * @return 排行榜
     */
    List<Map<String, Object>> getOccupancyRanking(int limit);

    /**
     * 校验机构名称是否唯一
     *
     * @param institution 机构信息
     * @return 结果
     */
    boolean checkInstitutionNameUnique(Institution institution);
}