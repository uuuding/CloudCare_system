package com.cloudcare.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.GpsAlarm;
import com.cloudcare.entity.GpsDevice;
import com.cloudcare.entity.GpsLocation;

import java.util.List;

/**
 * GPS数据服务接口
 *
 * @author CloudCare
 */
public interface GpsDataService extends IService<GpsLocation> {

    /**
     * 保存报警数据
     *
     * @param alarm 报警数据
     */
    void saveAlarmData(GpsAlarm alarm);

    /**
     * 保存位置数据
     *
     * @param location 位置数据
     */
    void saveLocationData(GpsLocation location);

    /**
     * 保存多媒体数据
     *
     * @param type 类型（pic/voice/video）
     * @param data 数据
     */
    void saveMediaData(String type, JSONObject data);

    /**
     * 根据设备IMEI获取最新位置
     *
     * @param macid 设备IMEI
     * @return 最新位置
     */
    GpsLocation getLatestLocationByMacid(String macid);

    /**
     * 根据老人ID获取最新位置
     *
     * @param elderlyId 老人ID
     * @return 最新位置
     */
    GpsLocation getLatestLocationByElderlyId(Long elderlyId);

    /**
     * 获取老人的历史轨迹
     *
     * @param elderlyId 老人ID
     * @param startTime 开始时间（毫秒时间戳）
     * @param endTime   结束时间（毫秒时间戳）
     * @return 轨迹列表
     */
    List<GpsLocation> getLocationHistory(Long elderlyId, Long startTime, Long endTime);

    /**
     * 检查围栏报警
     *
     * @param location 位置数据
     */
    void checkGeoFenceAlarm(GpsLocation location);

    /**
     * 更新设备状态
     *
     * @param macid    设备IMEI
     * @param location 位置数据
     */
    void updateDeviceStatus(String macid, GpsLocation location);
}