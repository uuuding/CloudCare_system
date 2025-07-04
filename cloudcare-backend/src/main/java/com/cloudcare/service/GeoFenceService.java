package com.cloudcare.service;

import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GeoFenceEvent;
import com.cloudcare.entity.GpsLocation;

import java.util.List;

/**
 * 电子围栏服务接口
 * 负责电子围栏的管理和围栏事件的检测，包括围栏的增删改查、位置检测、事件触发等功能
 * 支持圆形围栏和多边形围栏两种类型，可配置进入/离开提醒
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
public interface GeoFenceService {

    /**
     * 创建电子围栏
     * 自动设置创建时间和更新时间
     * 
     * @param geoFence 围栏信息实体，包含围栏名称、类型、坐标、提醒设置等
     * @return true-创建成功，false-创建失败
     * @see GeoFence
     */
    boolean createGeoFence(GeoFence geoFence);

    /**
     * 更新电子围栏信息
     * 自动更新修改时间
     * 
     * @param geoFence 围栏信息实体，必须包含有效的围栏ID
     * @return true-更新成功，false-更新失败
     * @see GeoFence
     */
    boolean updateGeoFence(GeoFence geoFence);

    /**
     * 删除电子围栏
     * 物理删除围栏记录，同时会影响相关的围栏事件记录
     * 
     * @param fenceId 围栏ID，不能为null
     * @return true-删除成功，false-删除失败
     */
    boolean deleteGeoFence(Long fenceId);

    /**
     * 根据老人ID查询启用的围栏列表
     * 只返回状态为启用（status=1）的围栏，用于围栏事件检测
     * 
     * @param elderlyId 老人ID，不能为null
     * @return 启用的围栏列表，如果没有启用的围栏则返回空列表
     * @see GeoFence
     */
    List<GeoFence> getActiveFencesByElderlyId(Integer elderlyId);

    /**
     * 查询系统中所有的围栏列表
     * 包含启用和禁用的围栏，用于管理界面展示
     * 
     * @return 所有围栏列表，按创建时间倒序排列
     * @see GeoFence
     */
    List<GeoFence> getAllFences();

    /**
     * 根据老人ID查询所有围栏列表
     * 包含该老人的启用和禁用围栏，用于管理界面展示
     * 
     * @param elderlyId 老人ID，不能为null
     * @return 该老人的所有围栏列表，按创建时间倒序排列
     * @see GeoFence
     */
    List<GeoFence> getAllFencesByElderlyId(Integer elderlyId);

    /**
     * 根据围栏ID查询围栏详细信息
     * 用于围栏详情展示和编辑
     * 
     * @param fenceId 围栏ID，不能为null
     * @return 围栏信息实体，如果围栏不存在则返回null
     * @see GeoFence
     */
    GeoFence getFenceById(Long fenceId);

    /**
     * 更新围栏启用/禁用状态
     * 禁用的围栏不会触发围栏事件检测
     * 
     * @param fenceId 围栏ID，不能为null
     * @param status 围栏状态，1-启用，0-禁用
     * @return true-更新成功，false-更新失败
     */
    boolean updateFenceStatus(Long fenceId, Integer status);

    /**
     * 检查GPS位置是否在指定围栏内
     * 支持圆形围栏和多边形围栏的检测，优先使用地图坐标
     * 
     * @param gpsLocation GPS位置信息，包含经纬度坐标
     * @param geoFence 围栏信息，包含围栏类型和边界定义
     * @return true-位置在围栏内，false-位置在围栏外或坐标无效
     * @see GpsLocation
     * @see GeoFence
     */
    boolean isLocationInFence(GpsLocation gpsLocation, GeoFence geoFence);

    /**
     * 检查GPS位置并触发围栏事件
     * 核心业务逻辑：检测老人是否进入/离开围栏，并根据配置发送提醒
     * 只有状态发生变化时才会触发事件（首次检测只记录状态）
     * 
     * @param gpsLocation GPS位置信息，必须包含有效的老人ID
     * @see GpsLocation
     * @see GeoFenceEvent
     */
    void checkFenceEvents(GpsLocation gpsLocation);

    /**
     * 计算两个地理坐标点之间的距离
     * 使用Haversine公式计算球面距离，用于圆形围栏的距离判断
     * 
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 两点之间的距离，单位：米
     */
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);

    /**
     * 检查指定点是否在多边形围栏内
     * 使用射线法（Ray Casting Algorithm）判断点与多边形的位置关系
     * 
     * @param lat 待检测点的纬度
     * @param lon 待检测点的经度
     * @param coordinates 多边形顶点坐标JSON字符串，格式：[{"lat":22.123,"lon":114.123},...]
     * @return true-点在多边形内，false-点在多边形外或坐标解析失败
     */
    boolean isPointInPolygon(double lat, double lon, String coordinates);
}