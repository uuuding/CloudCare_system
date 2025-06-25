package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.domain.HealthRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 健康记录 数据层
 *
 * @author CloudCare
 */
public interface HealthRecordMapper extends BaseMapper<HealthRecord> {

    /**
     * 查询指定日期之前创建的健康记录数量
     *
     * @param date 日期
     * @return 健康记录数量
     */
    int selectCountByCreateTimeBefore(@Param("date") LocalDate date);

    /**
     * 根据记录类型查询健康记录数量
     *
     * @param recordType 记录类型
     * @return 健康记录数量
     */
    int selectCountByRecordType(@Param("recordType") Integer recordType);

    /**
     * 根据记录等级查询健康记录数量
     *
     * @param recordLevel 记录等级
     * @return 健康记录数量
     */
    int selectCountByRecordLevel(@Param("recordLevel") Integer recordLevel);
}