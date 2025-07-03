package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.common.annotation.Log;
import com.cloudcare.common.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统配置控制器
 */
@Slf4j
@RestController
@RequestMapping("/system")
@Tag(name = "系统配置管理", description = "系统配置相关接口")
public class SystemConfigController {

    /**
     * 更新系统配置
     */
    @PutMapping("/config")
    @Operation(summary = "更新系统配置", description = "更新系统配置参数")
    @Log(title = "SYSTEM", businessType = BusinessType.UPDATE, isSaveRequestData = true, isSaveResponseData = true)
    public Result<String> updateConfig(
            @Parameter(description = "配置参数") @RequestBody Map<String, Object> configParams) {
        try {
            // 这里应该有实际的配置更新逻辑
            log.info("更新系统配置: {}", configParams);
            return Result.success("系统配置更新成功");
        } catch (Exception e) {
            log.error("更新系统配置失败", e);
            return Result.error("更新系统配置失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统配置
     */
    @GetMapping("/config")
    @Operation(summary = "获取系统配置", description = "获取当前系统配置")
    public Result<Map<String, Object>> getConfig() {
        try {
            // 这里应该有实际的配置获取逻辑
            Map<String, Object> config = Map.of(
                "maxFileSize", "10MB",
                "sessionTimeout", "30",
                "enableLogging", true
            );
            return Result.success(config);
        } catch (Exception e) {
            log.error("获取系统配置失败", e);
            return Result.error("获取系统配置失败: " + e.getMessage());
        }
    }
}