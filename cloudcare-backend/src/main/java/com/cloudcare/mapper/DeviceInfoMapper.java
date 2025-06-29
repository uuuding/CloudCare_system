package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 设备信息Mapper接口
 *
 * @author cloudcare
 */
@Mapper
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    /**
     * 获取设备统计信息
     */
    @Select("SELECT device_type, COUNT(1) as count FROM device_info WHERE deleted = 0 GROUP BY device_type")
    List<Map<String, Object>> getDeviceStatistics();

    /**
     * 获取设备状态统计
     */
    @Select("SELECT device_status, COUNT(1) as count FROM device_info WHERE deleted = 0 GROUP BY device_status")
    List<Map<String, Object>> getDeviceStatusStatistics();

    /**
     * 获取需要维护的设备列表
     */
    @Select("SELECT * FROM device_info WHERE deleted = 0 AND next_maintenance_time <= NOW() AND device_status = 1")
    List<DeviceInfo> getMaintenanceRequiredDevices();
}