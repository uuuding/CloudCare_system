package com.cloudcare.mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 健康预警 数据层扩展接口
 *
 * @author CloudCare
 */
public interface HealthWarningMapperExt {

    /**
     * 查询指定日期之前的健康预警数量
     *
     * @param date 日期
     * @return 健康预警数量
     */
    int selectCountByWarningTimeBefore(@Param("date") LocalDate date);

    /**
     * 根据预警级别和日期查询健康预警数量
     *
     * @param warningLevel 预警级别
     * @param date 日期
     * @return 健康预警数量
     */
    int selectCountByWarningLevelAndDate(@Param("warningLevel") Integer warningLevel, @Param("date") LocalDate date);

    /**
     * 根据预警级别和日期范围查询健康预警数量
     *
     * @param warningLevel 预警级别
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 健康预警数量
     */
    int selectCountByWarningLevelAndDateRange(@Param("warningLevel") Integer warningLevel, 
                                             @Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);
}