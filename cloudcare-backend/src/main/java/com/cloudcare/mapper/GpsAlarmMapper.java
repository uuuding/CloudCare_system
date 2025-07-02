package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.GpsAlarm;
import org.apache.ibatis.annotations.Mapper;

/**
 * GPS报警数据Mapper接口
 *
 * @author CloudCare
 */
@Mapper
public interface GpsAlarmMapper extends BaseMapper<GpsAlarm> {
}