package com.cloudcare.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.entity.Institution;
import com.cloudcare.service.InstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 机构信息控制器
 *
 * @author cloudcare
 */
@Slf4j
@RestController
@RequestMapping("/institution")
@CrossOrigin(origins = "*")
@Tag(name = "机构管理", description = "机构信息管理相关接口")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    /**
     * 分页查询机构信息列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询机构信息列表", description = "分页查询机构信息列表")
    public Result<Page<Institution>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "机构名称") @RequestParam(required = false) String name,
            @Parameter(description = "机构类型") @RequestParam(required = false) String type,
            @Parameter(description = "联系人") @RequestParam(required = false) String contactPerson,
            @Parameter(description = "联系电话") @RequestParam(required = false) String contactPhone,
            @Parameter(description = "地址") @RequestParam(required = false) String address,
            @Parameter(description = "机构状态：运营中、筹备中、暂停服务") @RequestParam(required = false) String status) {
        
        Page<Institution> page = new Page<>(pageNum, pageSize);
        Institution institution = new Institution();
        institution.setName(name);
        institution.setType(type);
        institution.setContactPerson(contactPerson);
        institution.setContactPhone(contactPhone);
        institution.setAddress(address);
        institution.setStatus(status);
        
        Page<Institution> result = institutionService.selectInstitutionPage(page, institution);
        return Result.success(result);
    }

    /**
     * 根据ID查询机构信息
     */
    @GetMapping("/{institutionId}")
    @Operation(summary = "根据ID查询机构信息", description = "根据ID查询机构信息详情")
    public Result<Institution> getById(@Parameter(description = "机构ID") @PathVariable Long institutionId) {
        Institution institution = institutionService.selectInstitutionById(institutionId);
        return Result.success(institution);
    }

    /**
     * 新增机构信息
     */
    @PostMapping
    @Operation(summary = "新增机构信息", description = "新增机构信息")
    public Result<Void> add(@Validated @RequestBody Institution institution) {
        boolean result = institutionService.insertInstitution(institution);
        return result ? Result.success(null) : Result.error("新增机构信息失败");
    }

    /**
     * 修改机构信息
     */
    @PutMapping
    @Operation(summary = "修改机构信息", description = "修改机构信息")
    public Result<Void> update(@Validated @RequestBody Institution institution) {
        boolean result = institutionService.updateInstitution(institution);
        return result ? Result.success(null) : Result.error("修改机构信息失败");
    }

    /**
     * 批量删除机构信息
     */
    @DeleteMapping("/batch/{institutionIds}")
    @Operation(summary = "批量删除机构信息", description = "根据ID批量删除机构信息")
    public Result<Void> removeBatch(@Parameter(description = "机构ID，多个用逗号分隔") @PathVariable Long[] institutionIds) {
        boolean result = institutionService.deleteInstitutionByIds(institutionIds);
        return result ? Result.success(null) : Result.error("批量删除机构信息失败");
    }

    /**
     * 删除单个机构信息
     */
    @DeleteMapping("/{institutionId}")
    @Operation(summary = "删除机构信息", description = "根据ID删除单个机构信息")
    public Result<Void> remove(@Parameter(description = "机构ID") @PathVariable Long institutionId) {
        boolean result = institutionService.deleteInstitutionById(institutionId);
        return result ? Result.success(null) : Result.error("删除机构信息失败");
    }

    /**
     * 获取机构统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取机构统计信息", description = "获取机构统计信息")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = institutionService.getInstitutionStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取床位统计信息
     */
    @GetMapping("/bed-statistics")
    @Operation(summary = "获取床位统计信息", description = "获取床位统计信息")
    public Result<Map<String, Object>> getBedStatistics() {
        Map<String, Object> statistics = institutionService.getBedStatistics();
        return Result.success(statistics);
    }

    /**
     * 根据名称模糊查询机构
     */
    @GetMapping("/search")
    @Operation(summary = "根据名称模糊查询机构", description = "根据名称模糊查询机构")
    public Result<List<Institution>> searchByName(
            @Parameter(description = "机构名称") @RequestParam String name) {
        List<Institution> institutions = institutionService.selectByNameLike(name);
        return Result.success(institutions);
    }

    /**
     * 获取入住率排行榜
     */
    @GetMapping("/occupancy-ranking")
    @Operation(summary = "获取入住率排行榜", description = "获取入住率排行榜")
    public Result<List<Map<String, Object>>> getOccupancyRanking(
            @Parameter(description = "排行榜数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<Map<String, Object>> ranking = institutionService.getOccupancyRanking(limit);
        return Result.success(ranking);
    }

    /**
     * 校验机构名称唯一性
     */
    @GetMapping("/check-name")
    @Operation(summary = "校验机构名称唯一性", description = "校验机构名称唯一性")
    public Result<Boolean> checkInstitutionNameUnique(
            @Parameter(description = "机构ID") @RequestParam(required = false) Long institutionId,
            @Parameter(description = "机构名称") @RequestParam String name) {
        Institution institution = new Institution();
        institution.setInstitutionId(institutionId);
        institution.setName(name);
        boolean isUnique = institutionService.checkInstitutionNameUnique(institution);
        return Result.success(isUnique);
    }

    /**
     * 更新床位信息
     */
    @PutMapping("/beds")
    @Operation(summary = "更新床位信息", description = "更新机构床位信息")
    public Result<Void> updateBeds(
            @Parameter(description = "机构ID") @RequestParam Long institutionId,
            @Parameter(description = "总床位数") @RequestParam Integer bedTotal,
            @Parameter(description = "可用床位数") @RequestParam Integer bedAvailable) {
        
        Institution institution = new Institution();
        institution.setInstitutionId(institutionId);
        institution.setBedTotal(bedTotal);
        institution.setBedAvailable(bedAvailable);
        
        boolean result = institutionService.updateInstitution(institution);
        return result ? Result.success(null) : Result.error("更新床位信息失败");
    }
}