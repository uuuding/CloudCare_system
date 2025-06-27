package com.cloudcare.service;

import java.util.Map;

/**
 * 仪表盘数据服务接口
 *
 * @author CloudCare
 */
public interface DashboardService {

    /**
     * 获取天气信息
     *
     * @param city 城市名称
     * @return 天气信息
     */
    Map<String, Object> getWeatherInfo(String city);
}