package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudcare.common.Result;
import com.cloudcare.dto.FamilyInteractionDTO;
import com.cloudcare.entity.FamilyInteraction;
import com.cloudcare.service.FamilyInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 家属互动控制器
 */
@RestController
@RequestMapping("/family-interaction")
@CrossOrigin
public class FamilyInteractionController {

    @Autowired
    private FamilyInteractionService familyInteractionService;

    /**
     * 分页查询家属互动记录
     */
    @GetMapping("/page")
    public Result<IPage<FamilyInteractionDTO>> getInteractionPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) Long familyMemberId,
            @RequestParam(required = false) String interactionType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime
    ) {
        try {
            IPage<FamilyInteractionDTO> page = familyInteractionService.getInteractionPage(
                    current, size, elderId, familyMemberId, interactionType, status, startTime, endTime
            );
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询家属互动记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询家属互动记录详情
     */
    @GetMapping("/{id}")
    public Result<FamilyInteractionDTO> getInteractionById(@PathVariable Long id) {
        try {
            FamilyInteractionDTO interaction = familyInteractionService.getInteractionById(id);
            if (interaction != null) {
                return Result.success(interaction);
            } else {
                return Result.error("家属互动记录不存在");
            }
        } catch (Exception e) {
            return Result.error("查询家属互动记录详情失败: " + e.getMessage());
        }
    }

    /**
     * 新增家属互动记录
     */
    @PostMapping
    public Result<String> addInteraction(@RequestBody FamilyInteraction interaction) {
        try {
            boolean success = familyInteractionService.addInteraction(interaction);
            if (success) {
                return Result.success("新增家属互动记录成功");
            } else {
                return Result.error("新增家属互动记录失败");
            }
        } catch (Exception e) {
            return Result.error("新增家属互动记录失败: " + e.getMessage());
        }
    }

    /**
     * 更新家属互动记录
     */
    @PutMapping
    public Result<String> updateInteraction(@RequestBody FamilyInteraction interaction) {
        try {
            boolean success = familyInteractionService.updateInteraction(interaction);
            if (success) {
                return Result.success("更新家属互动记录成功");
            } else {
                return Result.error("更新家属互动记录失败");
            }
        } catch (Exception e) {
            return Result.error("更新家属互动记录失败: " + e.getMessage());
        }
    }

    /**
     * 删除家属互动记录
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteInteraction(@PathVariable Long id) {
        try {
            boolean success = familyInteractionService.deleteInteraction(id);
            if (success) {
                return Result.success("删除家属互动记录成功");
            } else {
                return Result.error("删除家属互动记录失败");
            }
        } catch (Exception e) {
            return Result.error("删除家属互动记录失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除家属互动记录
     */
    @DeleteMapping("/batch")
    public Result<String> batchDeleteInteractions(@RequestBody List<Long> ids) {
        try {
            boolean success = familyInteractionService.batchDeleteInteractions(ids);
            if (success) {
                return Result.success("批量删除家属互动记录成功");
            } else {
                return Result.error("批量删除家属互动记录失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除家属互动记录失败: " + e.getMessage());
        }
    }

    /**
     * 更新互动记录状态
     */
    @PutMapping("/{id}/status")
    public Result<String> updateInteractionStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String replyContent
    ) {
        try {
            boolean success = familyInteractionService.updateInteractionStatus(id, status, replyContent);
            if (success) {
                return Result.success("更新互动记录状态成功");
            } else {
                return Result.error("更新互动记录状态失败");
            }
        } catch (Exception e) {
            return Result.error("更新互动记录状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取互动统计数据
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getInteractionStats() {
        try {
            Map<String, Object> stats = familyInteractionService.getInteractionStats();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取互动统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取老人的家属列表
     */
    @GetMapping("/family-members/{elderId}")
    public Result<List<Map<String, Object>>> getFamilyMembersByElderId(@PathVariable Long elderId) {
        try {
            List<Map<String, Object>> familyMembers = familyInteractionService.getFamilyMembersByElderId(elderId);
            return Result.success(familyMembers);
        } catch (Exception e) {
            return Result.error("获取家属列表失败: " + e.getMessage());
        }
    }
}