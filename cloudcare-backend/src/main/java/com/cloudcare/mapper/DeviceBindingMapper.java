package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.DeviceBinding;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 设备绑定Mapper接口
 */
@Mapper
public interface DeviceBindingMapper extends BaseMapper<DeviceBinding> {

    /**
     * 获取所有有效的设备绑定关系
     * @return 设备绑定列表
     */
    @Select("SELECT db.macid, db.elderly_id, ep.name as elderly_name, db.bind_time, " +
            "       COALESCE(gl.last_active_time, '暂无数据') as last_active_time " +
            "FROM device_binding db " +
            "LEFT JOIN elderly_profile ep ON db.elderly_id = ep.id " +
            "LEFT JOIN (" +
            "    SELECT macid, MAX(create_time) as last_active_time " +
            "    FROM gps_location " +
            "    GROUP BY macid" +
            ") gl ON db.macid = gl.macid " +
            "WHERE db.status = 1 " +
            "ORDER BY db.bind_time DESC")
    List<Map<String, Object>> getAllDeviceBindings();

    /**
     * 根据设备编号查询绑定关系
     * @param macid 设备编号
     * @return 绑定关系
     */
    @Select("SELECT * FROM device_binding WHERE macid = #{macid} AND status = 1")
    DeviceBinding getBindingByMacid(@Param("macid") String macid);

    /**
     * 根据老人ID查询绑定关系
     * @param elderlyId 老人ID
     * @return 绑定关系列表
     */
    @Select("SELECT * FROM device_binding WHERE elderly_id = #{elderlyId} AND status = 1")
    List<DeviceBinding> getBindingsByElderlyId(@Param("elderlyId") Integer elderlyId);

    /**
     * 检查设备是否已绑定
     * @param macid 设备编号
     * @param elderlyId 老人ID
     * @return 绑定关系
     */
    @Select("SELECT * FROM device_binding WHERE macid = #{macid} AND elderly_id = #{elderlyId} AND status = 1")
    DeviceBinding checkBinding(@Param("macid") String macid, @Param("elderlyId") Integer elderlyId);

    /**
     * 解绑设备
     * @param macid 设备编号
     * @param elderlyId 老人ID
     * @return 更新行数
     */
    @Update("UPDATE device_binding SET status = 0, unbind_time = NOW(), update_time = NOW() " +
            "WHERE macid = #{macid} AND elderly_id = #{elderlyId} AND status = 1")
    int unbindDevice(@Param("macid") String macid, @Param("elderlyId") Integer elderlyId);

    /**
     * 解绑设备的所有绑定关系
     * @param macid 设备编号
     * @return 更新行数
     */
    @Update("UPDATE device_binding SET status = 0, unbind_time = NOW(), update_time = NOW() " +
            "WHERE macid = #{macid} AND status = 1")
    int unbindAllByMacid(@Param("macid") String macid);
    
    /**
     * 根据老人ID删除所有设备绑定记录
     * @param elderlyId 老人ID
     * @return 删除行数
     */
    @Delete("DELETE FROM device_binding WHERE elderly_id = #{elderlyId}")
    int deleteBindingsByElderlyId(@Param("elderlyId") int elderlyId);
}