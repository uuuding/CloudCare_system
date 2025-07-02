package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.entity.GeoFence;
import com.cloudcare.service.GeoFenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电子围栏管理控制器
 *
 * @author CloudCare
 */
@Slf4j
@RestController
@RequestMapping("/api/geo-fence")
@RequiredArgsConstructor
@Tag(name = "电子围栏管理", description = "电子围栏相关接口")
public class GeoFenceController {

    private final GeoFenceService geoFenceService;

    @GetMapping("/list")
    @Operation(summary = "分页查询围栏列表", description = "根据条件分页查询电子围栏列表")
    public Result<IPage<GeoFence>> getGeoFenceList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long elderlyId,
            @RequestParam(required = false) Integer status) {
        try {
            Page<GeoFence> page = new Page<>(pageNum, pageSize);
            IPage<GeoFence> result = geoFenceService.getGeoFencePage(page, elderlyId, status);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询围栏列表失败", e);
            return Result.error("查询围栏列表失败");
        }
    }

    @GetMapping("/elderly/{elderlyId}")
    @Operation(summary = "根据老人ID获取围栏列表", description = "获取指定老人的所有电子围栏")
    public Result<List<GeoFence>> getGeoFencesByElderlyId(@PathVariable Long elderlyId) {
        try {
            List<GeoFence> fences = geoFenceService.getGeoFencesByElderlyId(elderlyId);
            return Result.success(fences);
        } catch (Exception e) {
            log.error("查询老人围栏列表失败", e);
            return Result.error("查询老人围栏列表失败");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取围栏详情", description = "根据围栏ID获取详细信息")
    public Result<GeoFence> getGeoFenceById(@PathVariable Long id) {
        try {
            GeoFence fence = geoFenceService.getById(id);
            if (fence != null) {
                return Result.success(fence);
            } else {
                return Result.error("围栏不存在");
            }
        } catch (Exception e) {
            log.error("查询围栏详情失败", e);
            return Result.error("查询围栏详情失败");
        }
    }

    @PostMapping
    @Operation(summary = "创建围栏", description = "创建新的电子围栏")
    public Result<String> createGeoFence(@RequestBody GeoFence geoFence) {
        try {
            if (geoFenceService.createGeoFence(geoFence)) {
                return Result.success("创建围栏成功");
            } else {
                return Result.error("创建围栏失败");
            }
        } catch (Exception e) {
            log.error("创建围栏失败", e);
            return Result.error("创建围栏失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新围栏", description = "更新电子围栏信息")
    public Result<String> updateGeoFence(@PathVariable Long id, @RequestBody GeoFence geoFence) {
        try {
            geoFence.setId(id);
            if (geoFenceService.updateGeoFence(geoFence)) {
                return Result.success("更新围栏成功");
            } else {
                return Result.error("更新围栏失败");
            }
        } catch (Exception e) {
            log.error("更新围栏失败", e);
            return Result.error("更新围栏失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除围栏", description = "根据ID删除电子围栏")
    public Result<String> deleteGeoFence(@PathVariable Long id) {
        try {
            if (geoFenceService.deleteGeoFence(id)) {
                return Result.success("删除围栏成功");
            } else {
                return Result.error("删除围栏失败");
            }
        } catch (Exception e) {
            log.error("删除围栏失败", e);
            return Result.error("删除围栏失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新围栏状态", description = "启用或禁用电子围栏")
    public Result<String> updateGeoFenceStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            if (geoFenceService.updateGeoFenceStatus(id, status)) {
                return Result.success("更新围栏状态成功");
            } else {
                return Result.error("更新围栏状态失败");
            }
        } catch (Exception e) {
            log.error("更新围栏状态失败", e);
            return Result.error("更新围栏状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/check")
    @Operation(summary = "检查位置是否在围栏内", description = "检查指定位置是否在围栏范围内")
    public Result<Boolean> checkLocationInFence(@RequestBody CheckLocationRequest request) {
        try {
            GeoFence fence = geoFenceService.getById(request.getFenceId());
            if (fence == null) {
                return Result.error("围栏不存在");
            }
            
            boolean isInFence = geoFenceService.isLocationInFence(fence, request.getLat(), request.getLon());
            return Result.success(isInFence);
        } catch (Exception e) {
            log.error("检查位置是否在围栏内失败", e);
            return Result.error("检查位置失败: " + e.getMessage());
        }
    }

    /**
     * 检查位置请求参数
     */
    public static class CheckLocationRequest {
        private Long fenceId;
        private Double lat;
        private Double lon;

        public Long getFenceId() {
            return fenceId;
        }

        public void setFenceId(Long fenceId) {
            this.fenceId = fenceId;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }
    }
}