package com.cloudcare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.GpsLocation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * GPS位置服务接口
 */
public interface GpsLocationService extends IService<GpsLocation> {

    /**
     * 分页查询GPS位置列表
     * @param page 分页参数
     * @param deviceImei 设备IMEI
     * @param elderlyId 老人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    IPage<GpsLocation> getLocationList(Page<GpsLocation> page, String deviceImei, Long elderlyId, String startTime, String endTime);

    /**
     * 根据设备IMEI获取最新位置
     * @param deviceImei 设备IMEI
     * @return GPS位置
     */
    GpsLocation getLatestLocationByImei(String deviceImei);

    /**
     * 根据老人ID获取最新位置
     * @param elderlyId 老人ID
     * @return GPS位置
     */
    GpsLocation getLatestLocationByElderlyId(Long elderlyId);

    /**
     * 获取设备位置历史
     * @param deviceImei 设备IMEI
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 位置历史列表
     */
    List<GpsLocation> getLocationHistory(String deviceImei, String startTime, String endTime);

    /**
     * 获取老人位置历史
     * @param elderlyId 老人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 位置历史列表
     */
    List<GpsLocation> getElderlyLocationHistory(Long elderlyId, String startTime, String endTime);

    /**
     * 批量保存位置数据
     * @param locations 位置数据列表
     * @return 是否成功
     */
    boolean saveBatch(List<GpsLocation> locations);
}