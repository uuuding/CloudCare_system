package com.cloudcare.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.*;
import com.cloudcare.mapper.GpsAlarmMapper;
import com.cloudcare.mapper.GpsDeviceMapper;
import com.cloudcare.mapper.GpsLocationMapper;
import com.cloudcare.service.GeoFenceService;
import com.cloudcare.service.GpsDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GPS数据服务实现类
 *
 * @author CloudCare
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GpsDataServiceImpl extends ServiceImpl<GpsLocationMapper, GpsLocation> implements GpsDataService {

    private final GpsLocationMapper gpsLocationMapper;
    private final GpsAlarmMapper gpsAlarmMapper;
    private final GpsDeviceMapper gpsDeviceMapper;
    private final GeoFenceService geoFenceService;

    @Override
    @Transactional
    public void saveAlarmData(GpsAlarm alarm) {
        try {
            // 根据设备IMEI查找对应的老人ID
            GpsDevice device = gpsDeviceMapper.selectOne(
                new LambdaQueryWrapper<GpsDevice>()
                    .eq(GpsDevice::getMacid, alarm.getMacid())
                    .eq(GpsDevice::getStatus, 1)
            );
            
            if (device != null) {
                alarm.setElderlyId(device.getElderlyId());
            }
            
            alarm.setCreateTime(LocalDateTime.now());
            gpsAlarmMapper.insert(alarm);
            
            log.info("保存GPS报警数据成功: {}", alarm.getAlarmId());
        } catch (Exception e) {
            log.error("保存GPS报警数据失败", e);
            throw new RuntimeException("保存GPS报警数据失败", e);
        }
    }

    @Override
    @Transactional
    public void saveLocationData(GpsLocation location) {
        try {
            // 根据设备IMEI查找对应的老人ID
            GpsDevice device = gpsDeviceMapper.selectOne(
                new LambdaQueryWrapper<GpsDevice>()
                    .eq(GpsDevice::getMacid, location.getMacid())
                    .eq(GpsDevice::getStatus, 1)
            );
            
            if (device != null) {
                location.setElderlyId(device.getElderlyId());
                
                // 更新设备状态
                updateDeviceStatus(location.getMacid(), location);
                
                // 检查围栏报警
                checkGeoFenceAlarm(location);
            }
            
            location.setCreateTime(LocalDateTime.now());
            gpsLocationMapper.insert(location);
            
            log.info("保存GPS位置数据成功: {}", location.getMacid());
        } catch (Exception e) {
            log.error("保存GPS位置数据失败", e);
            throw new RuntimeException("保存GPS位置数据失败", e);
        }
    }

    @Override
    public void saveMediaData(String type, JSONObject data) {
        try {
            // 这里可以根据需要保存多媒体数据到数据库或文件系统
            log.info("接收到多媒体数据: type={}, data={}", type, data.toJSONString());
            
            // 可以扩展保存到专门的多媒体表
            // 暂时只记录日志
        } catch (Exception e) {
            log.error("保存多媒体数据失败", e);
        }
    }

    @Override
    public GpsLocation getLatestLocationByMacid(String macid) {
        return gpsLocationMapper.selectOne(
            new LambdaQueryWrapper<GpsLocation>()
                .eq(GpsLocation::getMacid, macid)
                .orderByDesc(GpsLocation::getGpsTime)
                .last("LIMIT 1")
        );
    }

    @Override
    public GpsLocation getLatestLocationByElderlyId(Long elderlyId) {
        return gpsLocationMapper.selectOne(
            new LambdaQueryWrapper<GpsLocation>()
                .eq(GpsLocation::getElderlyId, elderlyId)
                .orderByDesc(GpsLocation::getGpsTime)
                .last("LIMIT 1")
        );
    }

    @Override
    public List<GpsLocation> getLocationHistory(Long elderlyId, Long startTime, Long endTime) {
        return gpsLocationMapper.selectList(
            new LambdaQueryWrapper<GpsLocation>()
                .eq(GpsLocation::getElderlyId, elderlyId)
                .ge(startTime != null, GpsLocation::getGpsTime, startTime)
                .le(endTime != null, GpsLocation::getGpsTime, endTime)
                .orderByAsc(GpsLocation::getGpsTime)
        );
    }

    @Override
    public void checkGeoFenceAlarm(GpsLocation location) {
        if (location.getElderlyId() == null) {
            return;
        }
        
        try {
            // 检查该老人的所有围栏
            List<GeoFence> triggeredFences = geoFenceService.checkFenceAlarms(location.getElderlyId(), location);
            
            // 为每个触发的围栏创建报警记录
            for (GeoFence fence : triggeredFences) {
                GpsAlarm alarm = new GpsAlarm();
                alarm.setMacid(location.getMacid());
                alarm.setElderlyId(location.getElderlyId());
                alarm.setAlarmTime(location.getGpsTime());
                alarm.setAddTime(System.currentTimeMillis());
                alarm.setLat(location.getLat());
                alarm.setLon(location.getLon());
                alarm.setMapLat(location.getMapLat());
                alarm.setMapLon(location.getMapLon());
                alarm.setSpeed(location.getSpeed());
                alarm.setDirection(location.getDirection().intValue());
                alarm.setClassify(fence.getAlertType());
                alarm.setDescribeInfo("电子围栏报警: " + fence.getFenceName());
                alarm.setHandleStatus(0);
                alarm.setCreateTime(LocalDateTime.now());
                
                gpsAlarmMapper.insert(alarm);
                
                log.warn("触发电子围栏报警: 老人ID={}, 围栏={}, 位置=({}, {})", 
                    location.getElderlyId(), fence.getFenceName(), location.getLat(), location.getLon());
            }
        } catch (Exception e) {
            log.error("检查围栏报警失败", e);
        }
    }

    @Override
    public void updateDeviceStatus(String macid, GpsLocation location) {
        try {
            GpsDevice device = gpsDeviceMapper.selectOne(
                new LambdaQueryWrapper<GpsDevice>()
                    .eq(GpsDevice::getMacid, macid)
            );
            
            if (device != null) {
                if (location.getGpsTime() != null) {
                    device.setLastGpsTime(LocalDateTime.ofEpochSecond(location.getGpsTime() / 1000, 0, java.time.ZoneOffset.UTC));
                }
                if (location.getHeartTime() != null) {
                    device.setLastHeartTime(LocalDateTime.ofEpochSecond(location.getHeartTime() / 1000, 0, java.time.ZoneOffset.UTC));
                }
                device.setUpdateTime(LocalDateTime.now());
                gpsDeviceMapper.updateById(device);
            }
        } catch (Exception e) {
            log.error("更新设备状态失败", e);
        }
    }
}