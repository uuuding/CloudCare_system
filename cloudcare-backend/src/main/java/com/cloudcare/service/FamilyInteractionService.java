package com.cloudcare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.dto.FamilyInteractionDTO;
import com.cloudcare.entity.FamilyInteraction;

import java.util.List;
import java.util.Map;

/**
 * 家属互动服务接口
 */
public interface FamilyInteractionService extends IService<FamilyInteraction> {

    /**
     * 分页查询家属互动记录
     */
    IPage<FamilyInteractionDTO> getInteractionPage(
            int current,
            int size,
            Long elderId,
            Long familyMemberId,
            String interactionType,
            String status,
            String startTime,
            String endTime
    );

    /**
     * 根据ID查询家属互动记录详情
     */
    FamilyInteractionDTO getInteractionById(Long id);

    /**
     * 新增家属互动记录
     */
    boolean addInteraction(FamilyInteraction interaction);

    /**
     * 更新家属互动记录
     */
    boolean updateInteraction(FamilyInteraction interaction);

    /**
     * 删除家属互动记录
     */
    boolean deleteInteraction(Long id);

    /**
     * 批量删除家属互动记录
     */
    boolean batchDeleteInteractions(List<Long> ids);

    /**
     * 更新互动记录状态
     */
    boolean updateInteractionStatus(Long id, String status, String replyContent);

    /**
     * 获取互动统计数据
     */
    Map<String, Object> getInteractionStats();

    /**
     * 获取老人的家属列表
     */
    List<Map<String, Object>> getFamilyMembersByElderId(Long elderId);
}