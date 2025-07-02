package com.cloudcare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.GpsDevice;
import com.cloudcare.entity.GpsLocation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * GPS设备服务接口
 */
public interface GpsDeviceService extends IService<GpsDevice> {

    /**
     * 分页查询GPS设备列表
     * @param page 分页参数
     * @param deviceName 设备名称
     * @param imei IMEI号
     * @param elderlyId 老人ID
     * @param deviceStatus 设备状态
     * @return 分页结果
     */
    IPage<GpsDevice> getDeviceList(Page<GpsDevice> page, String deviceName, String imei, Long elderlyId, Integer deviceStatus);

    /**
     * 根据IMEI号获取设备
     * @param imei IMEI号
     * @return GPS设备
     */
    GpsDevice getByImei(String imei);

    /**
     * 绑定设备到老人
     * @param deviceId 设备ID
     * @param elderlyId 老人ID
     * @return 是否成功
     */
    boolean bindDevice(Long deviceId, Long elderlyId);

    /**
     * 解绑设备
     * @param deviceId 设备ID
     * @return 是否成功
     */
    boolean unbindDevice(Long deviceId);

    /**
     * 获取设备最新位置
     * @param deviceId 设备ID
     * @return GPS位置
     */
    GpsLocation getDeviceLatestLocation(Long deviceId);

    /**
     * 根据老人ID获取最新位置
     * @param elderlyId 老人ID
     * @return GPS位置
     */
    GpsLocation getElderlyLatestLocation(Long elderlyId);

    /**
     * 获取老人位置历史
     * @param elderlyId 老人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 位置历史列表
     */
    List<GpsLocation> getElderlyLocationHistory(Long elderlyId, String startTime, String endTime);
}