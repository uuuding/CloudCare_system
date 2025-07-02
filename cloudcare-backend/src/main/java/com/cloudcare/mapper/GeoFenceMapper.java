package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.GeoFence;
import org.apache.ibatis.annotations.Mapper;

/**
 * 电子围栏Mapper接口
 *
 * @author CloudCare
 */
@Mapper
public interface GeoFenceMapper extends BaseMapper<GeoFence> {
}