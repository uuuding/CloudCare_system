package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.dto.FamilyInteractionDTO;
import com.cloudcare.entity.FamilyInteraction;
import com.cloudcare.mapper.FamilyInteractionMapper;
import com.cloudcare.service.FamilyInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 家属互动服务实现类
 */
@Service
public class FamilyInteractionServiceImpl extends ServiceImpl<FamilyInteractionMapper, FamilyInteraction> implements FamilyInteractionService {

    @Autowired
    private FamilyInteractionMapper familyInteractionMapper;

    @Override
    public IPage<FamilyInteractionDTO> getInteractionPage(
            int current,
            int size,
            Long elderId,
            Long familyMemberId,
            String interactionType,
            String status,
            String startTime,
            String endTime
    ) {
        Page<FamilyInteractionDTO> page = new Page<>(current, size);
        return familyInteractionMapper.selectPageWithDetails(
                page, elderId, familyMemberId, interactionType, status, startTime, endTime
        );
    }

    @Override
    public FamilyInteractionDTO getInteractionById(Long id) {
        return familyInteractionMapper.selectByIdWithDetails(id);
    }

    @Override
    public boolean addInteraction(FamilyInteraction interaction) {
        interaction.setCreateTime(LocalDateTime.now());
        interaction.setUpdateTime(LocalDateTime.now());
        if (interaction.getStatus() == null) {
            interaction.setStatus("pending");
        }
        return save(interaction);
    }

    @Override
    public boolean updateInteraction(FamilyInteraction interaction) {
        interaction.setUpdateTime(LocalDateTime.now());
        return updateById(interaction);
    }

    @Override
    public boolean deleteInteraction(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDeleteInteractions(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public boolean updateInteractionStatus(Long id, String status, String replyContent) {
        FamilyInteraction interaction = getById(id);
        if (interaction != null) {
            interaction.setStatus(status);
            if (replyContent != null && !replyContent.trim().isEmpty()) {
                interaction.setReplyContent(replyContent);
                interaction.setReplyTime(LocalDateTime.now());
            }
            interaction.setUpdateTime(LocalDateTime.now());
            return updateById(interaction);
        }
        return false;
    }

    @Override
    public Map<String, Object> getInteractionStats() {
        return familyInteractionMapper.getInteractionStats();
    }

    @Override
    public List<Map<String, Object>> getFamilyMembersByElderId(Long elderId) {
        return familyInteractionMapper.getFamilyMembersByElderId(elderId);
    }
}