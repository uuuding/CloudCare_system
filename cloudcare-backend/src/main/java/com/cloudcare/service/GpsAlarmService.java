package com.cloudcare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.GpsAlarm;

/**
 * GPS报警服务接口
 *
 * @author CloudCare
 */
public interface GpsAlarmService extends IService<GpsAlarm> {
    
    /**
     * 根据设备IMEI获取未处理的报警数量
     *
     * @param deviceImei 设备IMEI
     * @return 未处理报警数量
     */
    Long getUnhandledCountByImei(String deviceImei);
    
    /**
     * 根据老人ID获取未处理的报警数量
     *
     * @param elderlyId 老人ID
     * @return 未处理报警数量
     */
    Long getUnhandledCountByElderlyId(Long elderlyId);
    
    /**
     * 处理报警
     *
     * @param alarmId 报警ID
     * @param processNotes 处理备注
     * @return 是否处理成功
     */
    boolean processAlarm(Long alarmId, String processNotes);
    
    /**
     * 获取报警统计信息
     *
     * @return 报警统计
     */
    Object getAlarmStatistics();
}