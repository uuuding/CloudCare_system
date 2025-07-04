package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.GpsLocation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GPS定位数据Mapper接口
 * 提供GPS定位数据的数据库访问操作，包括增删改查和复杂查询
 * 支持按老人ID、设备ID、时间范围等条件查询定位记录
 * 提供数据清理和统计分析功能
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Mapper
public interface GpsLocationMapper extends BaseMapper<GpsLocation> {

    /**
     * 根据老人ID查询最新的GPS定位记录
     * 按GPS时间倒序排列，返回最新的一条记录
     * 
     * @param elderlyId 老人ID
     * @return 最新的GPS定位记录，如果没有记录则返回null
     */
    @Select("SELECT * FROM gps_location WHERE elderly_id = #{elderlyId} ORDER BY gps_time DESC LIMIT 1")
    GpsLocation getLatestLocationByElderlyId(@Param("elderlyId") Integer elderlyId);

    /**
     * 根据设备编号查询最新的GPS定位记录
     * 用于获取指定设备的最新位置信息
     * 
     * @param macid 设备编号（IMEI）
     * @return 最新的GPS定位记录，如果没有记录则返回null
     */
    @Select("SELECT * FROM gps_location WHERE macid = #{macid} ORDER BY gps_time DESC LIMIT 1")
    GpsLocation getLatestLocationByMacid(@Param("macid") String macid);

    /**
     * 根据老人ID和时间范围查询GPS定位记录
     * 用于轨迹回放和历史数据分析
     * 注意：gps_time字段存储的是毫秒时间戳，需要将LocalDateTime转换为毫秒时间戳进行比较
     * 
     * @param elderlyId 老人ID
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return GPS定位记录列表，按GPS时间倒序排列
     */
    @Select("SELECT * FROM gps_location WHERE elderly_id = #{elderlyId} " +
            "AND gps_time BETWEEN UNIX_TIMESTAMP(#{startTime}) * 1000 AND UNIX_TIMESTAMP(#{endTime}) * 1000 " +
            "ORDER BY gps_time DESC")
    List<GpsLocation> getLocationsByElderlyIdAndTimeRange(
            @Param("elderlyId") Integer elderlyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据设备编号查询老人ID
     */
    @Select("SELECT elderly_id FROM gps_location WHERE macid = #{macid} ORDER BY gps_time DESC LIMIT 1")
    Integer getElderlyIdByMacid(@Param("macid") String macid);

    /**
     * 删除指定时间之前的GPS记录（数据清理）
     */
    @Select("DELETE FROM gps_location WHERE create_time < #{beforeTime}")
    int deleteLocationsBefore(@Param("beforeTime") LocalDateTime beforeTime);

    /**
     * 获取所有设备绑定记录
     */
    @Select("SELECT DISTINCT g.macid, g.elderly_id, e.name as elderly_name, " +
            "MIN(g.create_time) as create_time, MAX(g.update_time) as update_time " +
            "FROM gps_location g " +
            "LEFT JOIN elderly_profile e ON g.elderly_id = e.id " +
            "WHERE g.elderly_id IS NOT NULL " +
            "GROUP BY g.macid, g.elderly_id, e.name " +
            "ORDER BY MAX(g.update_time) DESC")
    java.util.List<java.util.Map<String, Object>> getAllDeviceBindings();
    
    /**
     * 根据老人ID删除所有GPS定位记录
     */
    @Delete("DELETE FROM gps_location WHERE elderly_id = #{elderlyId}")
    int deleteLocationsByElderlyId(@Param("elderlyId") int elderlyId);
}