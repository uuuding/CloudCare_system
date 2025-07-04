package com.cloudcare.service;

import com.cloudcare.dto.GpsDataDTO;
import com.cloudcare.entity.GpsLocation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GPS定位服务接口
 * 负责处理GPS设备推送的定位数据，包括数据解析、存储、设备绑定查询等功能
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
public interface GpsLocationService {

    /**
     * 处理GPS设备推送的定位数据
     * 解析GPS数据，查询设备绑定关系，保存定位记录，并触发围栏事件检查
     * 
     * @param gpsDataDTO GPS数据传输对象，包含设备序列号、方法类型和定位数据数组
     * @return 处理成功时返回设备序列号，失败时抛出异常
     * @throws RuntimeException 当数据处理失败时抛出
     * @see GpsDataDTO
     * @see #saveGpsLocation(GpsLocation)
     */
    String processGpsData(GpsDataDTO gpsDataDTO);

    /**
     * 保存GPS定位记录到数据库
     * 自动设置创建时间和更新时间
     * 
     * @param gpsLocation GPS定位数据实体，包含设备编号、坐标、时间戳等信息
     * @return true-保存成功，false-保存失败
     * @see GpsLocation
     */
    boolean saveGpsLocation(GpsLocation gpsLocation);

    /**
     * 根据老人ID查询最新的GPS定位记录
     * 按创建时间倒序排列，返回最新的一条记录
     * 
     * @param elderlyId 老人ID，不能为null
     * @return 最新的GPS定位记录，如果没有找到则返回null
     * @see GpsLocation
     */
    GpsLocation getLatestLocationByElderlyId(Integer elderlyId);

    /**
     * 根据设备编号查询最新的GPS定位记录
     * 按创建时间倒序排列，返回最新的一条记录
     * 
     * @param macid 设备编号（IMEI），不能为null或空字符串
     * @return 最新的GPS定位记录，如果没有找到则返回null
     * @see GpsLocation
     */
    GpsLocation getLatestLocationByMacid(String macid);

    /**
     * 根据老人ID和时间范围查询GPS定位记录
     * 按创建时间倒序排列，用于轨迹回放和历史位置查询
     * 
     * @param elderlyId 老人ID，不能为null
     * @param startTime 查询开始时间，包含该时间点
     * @param endTime 查询结束时间，包含该时间点
     * @return GPS定位记录列表，按时间倒序排列，如果没有数据则返回空列表
     * @see GpsLocation
     */
    List<GpsLocation> getLocationsByElderlyIdAndTimeRange(Integer elderlyId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据设备编号查询对应的老人ID
     * 通过设备绑定关系表查询设备与老人的绑定关系
     * 
     * @param macid 设备编号（IMEI），不能为null或空字符串
     * @return 绑定的老人ID，如果设备未绑定则返回null
     * @see com.cloudcare.service.DeviceBindingService#getElderlyIdByMacid(String)
     */
    Integer getElderlyIdByMacid(String macid);

    /**
     * 数据清理：删除指定时间之前的GPS记录
     * 用于定期清理历史数据，释放存储空间
     * 
     * @param beforeTime 时间分界点，删除此时间之前的所有GPS记录
     * @return 实际删除的记录数量
     * @throws RuntimeException 当删除操作失败时抛出
     */
    int cleanOldLocationData(LocalDateTime beforeTime);
}