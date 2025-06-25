package com.cloudcare.service;

import java.util.List;
import java.util.Map;

/**
 * 仪表盘数据服务接口
 *
 * @author CloudCare
 */
public interface DashboardService {

    /**
     * 获取仪表盘统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 获取健康预警趋势数据
     *
     * @param timeRange 时间范围：week-本周, month-本月, year-全年
     * @return 趋势数据
     */
    Map<String, Object> getWarningTrend(String timeRange);

    /**
     * 获取健康记录分布数据
     *
     * @param type 分布类型：type-类型分布, level-等级分布
     * @return 分布数据
     */
    Map<String, Object> getRecordDistribution(String type);

    /**
     * 获取系统公告列表
     *
     * @param limit 获取条数
     * @return 公告列表
     */
    List<Map<String, Object>> getAnnouncements(Integer limit);

    /**
     * 获取天气信息
     *
     * @param city 城市名称
     * @return 天气信息
     */
    Map<String, Object> getWeatherInfo(String city);
}