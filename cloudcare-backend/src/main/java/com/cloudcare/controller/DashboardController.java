package com.cloudcare.controller;

import com.cloudcare.common.annotation.Log;
import com.cloudcare.common.core.controller.BaseController;
import com.cloudcare.common.core.domain.AjaxResult;
import com.cloudcare.common.enums.BusinessType;
import com.cloudcare.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘数据 控制器
 *
 * @author CloudCare
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取仪表盘统计数据
     */
    @PreAuthorize("@ss.hasPermi('dashboard:view')")
    @GetMapping("/statistics")
    public AjaxResult getStatistics() {
        return success(dashboardService.getStatistics());
    }

    /**
     * 获取健康预警趋势数据
     *
     * @param timeRange 时间范围：week-本周, month-本月, year-全年
     */
    @PreAuthorize("@ss.hasPermi('dashboard:view')")
    @GetMapping("/warning/trend")
    public AjaxResult getWarningTrend(@RequestParam(defaultValue = "week") String timeRange) {
        return success(dashboardService.getWarningTrend(timeRange));
    }

    /**
     * 获取健康记录分布数据
     *
     * @param type 分布类型：type-类型分布, level-等级分布
     */
    @PreAuthorize("@ss.hasPermi('dashboard:view')")
    @GetMapping("/record/distribution")
    public AjaxResult getRecordDistribution(@RequestParam(defaultValue = "type") String type) {
        return success(dashboardService.getRecordDistribution(type));
    }

    /**
     * 获取系统公告列表
     *
     * @param limit 获取条数
     */
    @GetMapping("/announcements")
    public AjaxResult getAnnouncements(@RequestParam(defaultValue = "5") Integer limit) {
        return success(dashboardService.getAnnouncements(limit));
    }

    /**
     * 获取天气信息
     *
     * @param city 城市名称
     */
    @GetMapping("/weather")
    public AjaxResult getWeatherInfo(@RequestParam(defaultValue = "北京") String city) {
        return success(dashboardService.getWeatherInfo(city));
    }
}