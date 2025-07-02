package com.cloudcare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GpsLocation;

import java.util.List;

/**
 * 电子围栏服务接口
 *
 * @author CloudCare
 */
public interface GeoFenceService extends IService<GeoFence> {

    /**
     * 分页查询围栏列表
     *
     * @param page      分页参数
     * @param elderlyId 老人ID（可选）
     * @param status    状态（可选）
     * @return 分页结果
     */
    IPage<GeoFence> getGeoFencePage(Page<GeoFence> page, Long elderlyId, Integer status);

    /**
     * 根据老人ID获取围栏列表
     *
     * @param elderlyId 老人ID
     * @return 围栏列表
     */
    List<GeoFence> getGeoFencesByElderlyId(Long elderlyId);

    /**
     * 创建围栏
     *
     * @param geoFence 围栏信息
     * @return 是否成功
     */
    boolean createGeoFence(GeoFence geoFence);

    /**
     * 更新围栏
     *
     * @param geoFence 围栏信息
     * @return 是否成功
     */
    boolean updateGeoFence(GeoFence geoFence);

    /**
     * 删除围栏
     *
     * @param id 围栏ID
     * @return 是否成功
     */
    boolean deleteGeoFence(Long id);

    /**
     * 启用/禁用围栏
     *
     * @param id     围栏ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateGeoFenceStatus(Long id, Integer status);

    /**
     * 检查位置是否在围栏内
     *
     * @param geoFence 围栏
     * @param lat      纬度
     * @param lon      经度
     * @return 是否在围栏内
     */
    boolean isLocationInFence(GeoFence geoFence, Double lat, Double lon);

    /**
     * 检查位置是否触发围栏报警
     *
     * @param elderlyId 老人ID
     * @param location  位置信息
     * @return 触发的围栏列表
     */
    List<GeoFence> checkFenceAlarms(Long elderlyId, GpsLocation location);

    /**
     * 计算两点之间的距离（米）
     *
     * @param lat1 纬度1
     * @param lon1 经度1
     * @param lat2 纬度2
     * @param lon2 经度2
     * @return 距离（米）
     */
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);

    /**
     * 检查点是否在多边形内
     *
     * @param lat           纬度
     * @param lon           经度
     * @param polygonPoints 多边形坐标点JSON
     * @return 是否在多边形内
     */
    boolean isPointInPolygon(double lat, double lon, String polygonPoints);
}