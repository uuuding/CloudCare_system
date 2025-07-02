package com.cloudcare.service;

import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GeoFenceEvent;
import com.cloudcare.entity.GpsLocation;

import java.util.List;

/**
 * 电子围栏服务接口
 */
public interface GeoFenceService {

    /**
     * 创建电子围栏
     * @param geoFence 围栏信息
     * @return 创建结果
     */
    boolean createGeoFence(GeoFence geoFence);

    /**
     * 更新电子围栏
     * @param geoFence 围栏信息
     * @return 更新结果
     */
    boolean updateGeoFence(GeoFence geoFence);

    /**
     * 删除电子围栏
     * @param fenceId 围栏ID
     * @return 删除结果
     */
    boolean deleteGeoFence(Long fenceId);

    /**
     * 根据老人ID查询启用的围栏列表
     * @param elderlyId 老人ID
     * @return 围栏列表
     */
    List<GeoFence> getActiveFencesByElderlyId(Integer elderlyId);

    /**
     * 查询所有围栏列表
     * @return 围栏列表
     */
    List<GeoFence> getAllFences();

    /**
     * 根据老人ID查询所有围栏列表
     * @param elderlyId 老人ID
     * @return 围栏列表
     */
    List<GeoFence> getAllFencesByElderlyId(Integer elderlyId);

    /**
     * 根据围栏ID查询围栏信息
     * @param fenceId 围栏ID
     * @return 围栏信息
     */
    GeoFence getFenceById(Long fenceId);

    /**
     * 更新围栏状态
     * @param fenceId 围栏ID
     * @param status 状态（1-启用，0-禁用）
     * @return 更新结果
     */
    boolean updateFenceStatus(Long fenceId, Integer status);

    /**
     * 检查GPS位置是否在围栏内
     * @param gpsLocation GPS位置
     * @param geoFence 围栏信息
     * @return 是否在围栏内
     */
    boolean isLocationInFence(GpsLocation gpsLocation, GeoFence geoFence);

    /**
     * 检查GPS位置并触发围栏事件
     * @param gpsLocation GPS位置
     */
    void checkFenceEvents(GpsLocation gpsLocation);

    /**
     * 计算两点之间的距离（米）
     * @param lat1 纬度1
     * @param lon1 经度1
     * @param lat2 纬度2
     * @param lon2 经度2
     * @return 距离（米）
     */
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);

    /**
     * 检查点是否在多边形内
     * @param lat 纬度
     * @param lon 经度
     * @param coordinates 多边形坐标点JSON字符串
     * @return 是否在多边形内
     */
    boolean isPointInPolygon(double lat, double lon, String coordinates);
}