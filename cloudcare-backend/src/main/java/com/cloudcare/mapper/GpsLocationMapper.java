package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.GpsLocation;
import org.apache.ibatis.annotations.Mapper;

/**
 * GPS位置数据Mapper接口
 *
 * @author CloudCare
 */
@Mapper
public interface GpsLocationMapper extends BaseMapper<GpsLocation> {
}