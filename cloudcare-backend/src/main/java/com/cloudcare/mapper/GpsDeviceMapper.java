package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.GpsDevice;
import org.apache.ibatis.annotations.Mapper;

/**
 * GPS设备Mapper接口
 *
 * @author CloudCare
 */
@Mapper
public interface GpsDeviceMapper extends BaseMapper<GpsDevice> {
}