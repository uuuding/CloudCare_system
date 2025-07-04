package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.GeoFence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 电子围栏Mapper接口
 * 提供电子围栏数据的数据库访问操作，包括围栏的增删改查
 * 支持按老人ID、围栏状态等条件查询围栏信息
 * 提供围栏管理和状态控制功能
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Mapper
public interface GeoFenceMapper extends BaseMapper<GeoFence> {

    /**
     * 根据老人ID查询启用的围栏列表
     * 只返回状态为启用（status=1）的围栏，用于围栏检测
     * 
     * @param elderlyId 老人ID
     * @return 启用的围栏列表，按创建时间倒序排列
     */
    @Select("SELECT * FROM geo_fence WHERE elderly_id = #{elderlyId} AND status = 1 ORDER BY create_time DESC")
    List<GeoFence> getActiveFencesByElderlyId(@Param("elderlyId") Integer elderlyId);

    /**
     * 查询所有围栏列表
     * 返回系统中的所有围栏，不区分状态，用于管理界面展示
     * 
     * @return 所有围栏列表，按创建时间倒序排列
     */
    @Select("SELECT * FROM geo_fence ORDER BY create_time DESC")
    List<GeoFence> getAllFences();

    /**
     * 根据老人ID查询所有围栏列表
     * 返回指定老人的所有围栏，包括启用和禁用的
     * 
     * @param elderlyId 老人ID
     * @return 该老人的所有围栏列表，按创建时间倒序排列
     */
    @Select("SELECT * FROM geo_fence WHERE elderly_id = #{elderlyId} ORDER BY create_time DESC")
    List<GeoFence> getAllFencesByElderlyId(@Param("elderlyId") Integer elderlyId);

    /**
     * 根据围栏ID查询围栏信息
     */
    @Select("SELECT * FROM geo_fence WHERE id = #{fenceId}")
    GeoFence getFenceById(@Param("fenceId") Long fenceId);

    /**
     * 更新围栏状态
     */
    @Select("UPDATE geo_fence SET status = #{status}, update_time = NOW() WHERE id = #{fenceId}")
    int updateFenceStatus(@Param("fenceId") Long fenceId, @Param("status") Integer status);

    /**
     * 查询所有启用的围栏（用于批量检查）
     */
    @Select("SELECT * FROM geo_fence WHERE status = 1")
    List<GeoFence> getAllActiveFences();
}