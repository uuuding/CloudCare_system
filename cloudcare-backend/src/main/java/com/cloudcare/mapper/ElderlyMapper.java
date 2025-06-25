package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.domain.Elderly;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 老人信息 数据层
 *
 * @author CloudCare
 */
public interface ElderlyMapper extends BaseMapper<Elderly> {

    /**
     * 查询指定日期之前创建的老人数量
     *
     * @param date 日期
     * @return 老人数量
     */
    int selectCountByCreateTimeBefore(@Param("date") LocalDate date);
}