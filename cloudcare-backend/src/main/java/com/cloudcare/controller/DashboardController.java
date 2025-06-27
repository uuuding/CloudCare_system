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
     * 获取天气信息
     *
     * @param city 城市名称
     */
    @GetMapping("/weather")
    public AjaxResult getWeatherInfo(@RequestParam(defaultValue = "北京") String city) {
        return success(dashboardService.getWeatherInfo(city));
    }
}