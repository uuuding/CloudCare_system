package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.common.ResultCode;
import com.cloudcare.domain.HealthWarning;
import com.cloudcare.service.HealthWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 健康预警控制器
 *
 * @author cloudcare
 */
@RestController
@RequestMapping("/api/health/warning")
public class HealthWarningController {

    @Autowired
    private HealthWarningService healthWarningService;

    /**
     * 分页查询健康预警列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<IPage<HealthWarning>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            HealthWarning healthWarning) {
        Page<HealthWarning> page = new Page<>(pageNum, pageSize);
        IPage<HealthWarning> list = healthWarningService.pageHealthWarnings(page, healthWarning);
        return Result.success(list);
    }

    /**
     * 获取健康预警详情
     */
    @GetMapping("/{warningId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<HealthWarning> getInfo(@PathVariable Long warningId) {
        HealthWarning healthWarning = healthWarningService.getHealthWarningById(warningId);
        if (healthWarning == null) {
            return Result.failure(ResultCode.DATA_NOT_FOUND, "未找到该健康预警信息");
        }
        return Result.success(healthWarning);
    }

    /**
     * 新增健康预警
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Boolean> add(@RequestBody HealthWarning healthWarning) {
        boolean result = healthWarningService.addHealthWarning(healthWarning);
        return Result.success(result);
    }

    /**
     * 修改健康预警
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Boolean> update(@RequestBody HealthWarning healthWarning) {
        boolean result = healthWarningService.updateHealthWarning(healthWarning);
        return Result.success(result);
    }

    /**
     * 删除健康预警
     */
    @DeleteMapping("/{warningIds}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Result<Boolean> remove(@PathVariable Long[] warningIds) {
        boolean result = healthWarningService.deleteHealthWarningByIds(warningIds);
        return Result.success(result);
    }

    /**
     * 处理健康预警
     */
    @PutMapping("/process")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Boolean> process(@RequestBody HealthWarning healthWarning) {
        boolean result = healthWarningService.processHealthWarning(healthWarning);
        return Result.success(result);
    }

    /**
     * 获取未处理的健康预警数量
     */
    @GetMapping("/unprocessed/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Integer> getUnprocessedCount() {
        int count = healthWarningService.getUnprocessedWarningCount();
        return Result.success(count);
    }

    /**
     * 获取老人的健康预警列表
     */
    @GetMapping("/elder/{elderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'ELDERLY', 'FAMILY')")
    public Result<List<HealthWarning>> getElderWarnings(@PathVariable Long elderId) {
        List<HealthWarning> list = healthWarningService.getWarningsByElderId(elderId);
        return Result.success(list);
    }

    /**
     * 获取最近的健康预警列表
     */
    @GetMapping("/recent/{limit}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<HealthWarning>> getRecentWarnings(@PathVariable Integer limit) {
        List<HealthWarning> list = healthWarningService.getRecentWarnings(limit);
        return Result.success(list);
    }
}