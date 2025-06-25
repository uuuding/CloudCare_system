package com.cloudcare.service.impl;

import com.cloudcare.mapper.ElderlyMapper;
import com.cloudcare.mapper.HealthRecordMapper;
import com.cloudcare.mapper.HealthWarningMapper;
import com.cloudcare.mapper.SysUserMapper;
import com.cloudcare.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 仪表盘数据服务实现类
 *
 * @author CloudCare
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ElderlyMapper elderlyMapper;

    @Autowired
    private HealthRecordMapper healthRecordMapper;

    @Autowired
    private HealthWarningMapper healthWarningMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取仪表盘统计数据
     *
     * @return 统计数据
     */
    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();

        // 获取当前月份和上个月份
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = now.withDayOfMonth(1);
        LocalDate firstDayOfLastMonth = firstDayOfCurrentMonth.minusMonths(1);
        LocalDate lastDayOfLastMonth = firstDayOfCurrentMonth.minusDays(1);

        // 老人总数
        Long elderlyCount = elderlyMapper.selectCount(null);
        // 上个月老人总数
        int lastMonthElderlyCount = elderlyMapper.selectCountByCreateTimeBefore(lastDayOfLastMonth);
        // 计算增长率
        double elderlyIncrease = lastMonthElderlyCount == 0 ? 100 : (elderlyCount.intValue() - lastMonthElderlyCount) * 100.0 / lastMonthElderlyCount;

        // 健康记录总数
        Long recordCount = healthRecordMapper.selectCount(null);
        // 上个月健康记录总数
        int lastMonthRecordCount = healthRecordMapper.selectCountByCreateTimeBefore(lastDayOfLastMonth);
        // 计算增长率
        double recordIncrease = lastMonthRecordCount == 0 ? 100 : (recordCount.intValue() - lastMonthRecordCount) * 100.0 / lastMonthRecordCount;

        // 健康预警总数
        Long warningCount = healthWarningMapper.selectCount(null);
        // 上个月健康预警总数
        int lastMonthWarningCount = healthWarningMapper.selectCountByWarningTimeBefore(lastDayOfLastMonth);
        // 计算增长率
        double warningIncrease = lastMonthWarningCount == 0 ? 100 : (warningCount.intValue() - lastMonthWarningCount) * 100.0 / lastMonthWarningCount;

        // 系统用户总数
        Long userCount = sysUserMapper.selectCount(null);
        // 上个月系统用户总数
        int lastMonthUserCount = sysUserMapper.selectCountByCreateTimeBefore(lastDayOfLastMonth);
        // 计算增长率
        double userIncrease = lastMonthUserCount == 0 ? 100 : (userCount.intValue() - lastMonthUserCount) * 100.0 / lastMonthUserCount;

        result.put("elderlyCount", elderlyCount);
        result.put("elderlyIncrease", Math.round(elderlyIncrease * 10) / 10.0);
        result.put("recordCount", recordCount);
        result.put("recordIncrease", Math.round(recordIncrease * 10) / 10.0);
        result.put("warningCount", warningCount);
        result.put("warningIncrease", Math.round(warningIncrease * 10) / 10.0);
        result.put("userCount", userCount);
        result.put("userIncrease", Math.round(userIncrease * 10) / 10.0);

        return result;
    }

    /**
     * 获取健康预警趋势数据
     *
     * @param timeRange 时间范围：week-本周, month-本月, year-全年
     * @return 趋势数据
     */
    @Override
    public Map<String, Object> getWarningTrend(String timeRange) {
        Map<String, Object> result = new HashMap<>();
        List<String> xAxisData = new ArrayList<>();
        List<Integer> lightData = new ArrayList<>();
        List<Integer> mediumData = new ArrayList<>();
        List<Integer> severeData = new ArrayList<>();

        LocalDate now = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate = now;

        // 根据时间范围确定起始日期和X轴数据
        switch (timeRange) {
            case "week":
                // 本周数据，从周一到周日
                startDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                for (int i = 0; i < 7; i++) {
                    LocalDate date = startDate.plusDays(i);
                    xAxisData.add(getDayOfWeekChinese(date.getDayOfWeek()));

                    // 获取每天不同级别的预警数量
                    int lightCount = healthWarningMapper.selectCountByWarningLevelAndDate(1, date);
                    int mediumCount = healthWarningMapper.selectCountByWarningLevelAndDate(2, date);
                    int severeCount = healthWarningMapper.selectCountByWarningLevelAndDate(3, date);

                    lightData.add(lightCount);
                    mediumData.add(mediumCount);
                    severeData.add(severeCount);
                }
                break;
            case "month":
                // 本月数据，按周统计
                startDate = now.withDayOfMonth(1);
                LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());
                int weeks = (int) Math.ceil((lastDayOfMonth.getDayOfMonth() - startDate.getDayOfMonth() + 1) / 7.0);

                for (int i = 0; i < weeks; i++) {
                    LocalDate weekStart = startDate.plusDays(i * 7);
                    LocalDate weekEnd = weekStart.plusDays(6);
                    if (weekEnd.isAfter(lastDayOfMonth)) {
                        weekEnd = lastDayOfMonth;
                    }

                    xAxisData.add("第" + (i + 1) + "周");

                    // 获取每周不同级别的预警数量
                    int lightCount = healthWarningMapper.selectCountByWarningLevelAndDateRange(1, weekStart, weekEnd);
                    int mediumCount = healthWarningMapper.selectCountByWarningLevelAndDateRange(2, weekStart, weekEnd);
                    int severeCount = healthWarningMapper.selectCountByWarningLevelAndDateRange(3, weekStart, weekEnd);

                    lightData.add(lightCount);
                    mediumData.add(mediumCount);
                    severeData.add(severeCount);
                }
                break;
            case "year":
                // 全年数据，按月统计
                startDate = now.withMonth(1).withDayOfMonth(1);
                for (int i = 0; i < 12; i++) {
                    LocalDate monthStart = startDate.plusMonths(i);
                    LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

                    xAxisData.add((i + 1) + "月");

                    // 获取每月不同级别的预警数量
                    int lightCount = healthWarningMapper.selectCountByWarningLevelAndDateRange(1, monthStart, monthEnd);
                    int mediumCount = healthWarningMapper.selectCountByWarningLevelAndDateRange(2, monthStart, monthEnd);
                    int severeCount = healthWarningMapper.selectCountByWarningLevelAndDateRange(3, monthStart, monthEnd);

                    lightData.add(lightCount);
                    mediumData.add(mediumCount);
                    severeData.add(severeCount);
                }
                break;
            default:
                // 默认为本周
                return getWarningTrend("week");
        }

        result.put("xAxisData", xAxisData);
        result.put("lightData", lightData);
        result.put("mediumData", mediumData);
        result.put("severeData", severeData);

        return result;
    }

    /**
     * 获取健康记录分布数据
     *
     * @param type 分布类型：type-类型分布, level-等级分布
     * @return 分布数据
     */
    @Override
    public Map<String, Object> getRecordDistribution(String type) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();

        if ("type".equals(type)) {
            // 按类型分布
            List<String> legendData = Arrays.asList("体温", "血压", "血糖", "心率", "血氧", "体重");
            result.put("legendData", legendData);

            // 获取各类型记录数量
            for (int i = 0; i < legendData.size(); i++) {
                int count = healthRecordMapper.selectCountByRecordType(i + 1);
                Map<String, Object> item = new HashMap<>();
                item.put("name", legendData.get(i));
                item.put("value", count);
                data.add(item);
            }
        } else if ("level".equals(type)) {
            // 按等级分布
            List<String> legendData = Arrays.asList("正常", "轻度异常", "中度异常", "重度异常");
            result.put("legendData", legendData);

            // 获取各等级记录数量
            for (int i = 0; i < legendData.size(); i++) {
                int count = healthRecordMapper.selectCountByRecordLevel(i);
                Map<String, Object> item = new HashMap<>();
                item.put("name", legendData.get(i));
                item.put("value", count);
                data.add(item);
            }
        }

        result.put("data", data);
        return result;
    }

    /**
     * 获取系统公告列表
     *
     * @param limit 获取条数
     * @return 公告列表
     */
    @Override
    public List<Map<String, Object>> getAnnouncements(Integer limit) {
        // 模拟数据，实际项目中应该从数据库获取
        List<Map<String, Object>> announcements = new ArrayList<>();

        Map<String, Object> announcement1 = new HashMap<>();
        announcement1.put("title", "系统升级通知");
        announcement1.put("content", "尊敬的用户，系统将于2023年6月15日凌晨2:00-4:00进行升级维护，期间系统将暂停服务，请提前做好相关工作安排。");
        announcement1.put("createTime", "2023-06-10 10:00:00");
        announcement1.put("createBy", "系统管理员");
        announcements.add(announcement1);

        Map<String, Object> announcement2 = new HashMap<>();
        announcement2.put("title", "健康监测新功能上线");
        announcement2.put("content", "云护理系统新增健康监测功能，支持实时监测老人的体温、血压、心率等健康指标，欢迎使用。");
        announcement2.put("createTime", "2023-06-05 14:30:00");
        announcement2.put("createBy", "系统管理员");
        announcements.add(announcement2);

        // 限制返回条数
        if (announcements.size() > limit) {
            return announcements.subList(0, limit);
        }

        return announcements;
    }

    /**
     * 获取天气信息
     *
     * @param city 城市名称
     * @return 天气信息
     */
    @Override
    public Map<String, Object> getWeatherInfo(String city) {
        // 模拟数据，实际项目中应该调用天气API获取
        Map<String, Object> weather = new HashMap<>();
        weather.put("city", city);
        weather.put("temp", 25);
        weather.put("text", "晴");
        weather.put("humidity", 40);
        weather.put("wind", "东北风 3级");

        return weather;
    }

    /**
     * 获取星期几的中文表示
     *
     * @param dayOfWeek 星期几
     * @return 中文表示
     */
    private String getDayOfWeekChinese(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "周一";
            case TUESDAY:
                return "周二";
            case WEDNESDAY:
                return "周三";
            case THURSDAY:
                return "周四";
            case FRIDAY:
                return "周五";
            case SATURDAY:
                return "周六";
            case SUNDAY:
                return "周日";
            default:
                return "";
        }
    }
}