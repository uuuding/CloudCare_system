package com.cloudcare.service;

import com.cloudcare.dto.GpsDataDTO;
import com.cloudcare.entity.GpsLocation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GPS定位服务接口
 */
public interface GpsLocationService {

    /**
     * 处理GPS数据推送
     * @param gpsDataDTO GPS数据
     * @return 处理结果
     */
    String processGpsData(GpsDataDTO gpsDataDTO);

    /**
     * 保存GPS定位记录
     * @param gpsLocation GPS定位数据
     * @return 保存结果
     */
    boolean saveGpsLocation(GpsLocation gpsLocation);

    /**
     * 根据老人ID查询最新的GPS定位记录
     * @param elderlyId 老人ID
     * @return GPS定位记录
     */
    GpsLocation getLatestLocationByElderlyId(Integer elderlyId);

    /**
     * 根据设备编号查询最新的GPS定位记录
     * @param macid 设备编号
     * @return GPS定位记录
     */
    GpsLocation getLatestLocationByMacid(String macid);

    /**
     * 根据老人ID和时间范围查询GPS定位记录
     * @param elderlyId 老人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return GPS定位记录列表
     */
    List<GpsLocation> getLocationsByElderlyIdAndTimeRange(Integer elderlyId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据设备编号查询对应的老人ID
     * @param macid 设备编号
     * @return 老人ID
     */
    Integer getElderlyIdByMacid(String macid);

    /**
     * 数据清理：删除指定时间之前的GPS记录
     * @param beforeTime 时间点
     * @return 删除的记录数
     */
    int cleanOldLocationData(LocalDateTime beforeTime);
}