package com.cloudcare.service.impl;

import com.cloudcare.dto.GpsDataDTO;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.mapper.GpsLocationMapper;
import com.cloudcare.service.GeoFenceService;
import com.cloudcare.service.GpsLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GPS定位服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GpsLocationServiceImpl implements GpsLocationService {

    private final GpsLocationMapper gpsLocationMapper;
    private final GeoFenceService geoFenceService;
    
    // 设备与老人的绑定关系缓存
    private final Map<String, Integer> deviceElderlyMapping = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public String processGpsData(GpsDataDTO gpsDataDTO) {
        try {
            log.info("接收到GPS数据推送，method: {}, serialNumber: {}, 数据条数: {}", 
                    gpsDataDTO.getMethod(), gpsDataDTO.getSerialNumber(), 
                    gpsDataDTO.getData() != null ? gpsDataDTO.getData().size() : 0);

            if ("status".equals(gpsDataDTO.getMethod()) && gpsDataDTO.getData() != null) {
                for (GpsDataDTO.GpsLocationData locationData : gpsDataDTO.getData()) {
                    // 转换并保存GPS数据
                    GpsLocation gpsLocation = convertToGpsLocation(locationData);
                    if (gpsLocation != null) {
                        // 查询或获取老人ID
                        Integer elderlyId = getElderlyIdByMacid(locationData.getMacid());
                        if (elderlyId != null) {
                            gpsLocation.setElderlyId(elderlyId);
                        }
                        
                        // 保存GPS记录
                        boolean saved = saveGpsLocation(gpsLocation);
                        if (saved) {
                            log.info("GPS数据保存成功，设备: {}, 老人ID: {}, 位置: ({}, {})", 
                                    gpsLocation.getMacid(), gpsLocation.getElderlyId(), 
                                    gpsLocation.getLat(), gpsLocation.getLon());
                            
                            // 检查围栏事件
                            if (elderlyId != null) {
                                geoFenceService.checkFenceEvents(gpsLocation);
                            }
                        }
                    }
                }
            }
            
            // 返回序列号表示处理成功
            return gpsDataDTO.getSerialNumber();
            
        } catch (Exception e) {
            log.error("处理GPS数据失败: {}", e.getMessage(), e);
            throw new RuntimeException("处理GPS数据失败: " + e.getMessage());
        }
    }

    @Override
    public boolean saveGpsLocation(GpsLocation gpsLocation) {
        try {
            gpsLocation.setCreateTime(LocalDateTime.now());
            gpsLocation.setUpdateTime(LocalDateTime.now());
            return gpsLocationMapper.insert(gpsLocation) > 0;
        } catch (Exception e) {
            log.error("保存GPS定位记录失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public GpsLocation getLatestLocationByElderlyId(Integer elderlyId) {
        return gpsLocationMapper.getLatestLocationByElderlyId(elderlyId);
    }

    @Override
    public GpsLocation getLatestLocationByMacid(String macid) {
        return gpsLocationMapper.getLatestLocationByMacid(macid);
    }

    @Override
    public List<GpsLocation> getLocationsByElderlyIdAndTimeRange(Integer elderlyId, LocalDateTime startTime, LocalDateTime endTime) {
        return gpsLocationMapper.getLocationsByElderlyIdAndTimeRange(elderlyId, startTime, endTime);
    }

    @Override
    public Integer getElderlyIdByMacid(String macid) {
        // 先从缓存中查找
        Integer elderlyId = deviceElderlyMapping.get(macid);
        if (elderlyId != null) {
            return elderlyId;
        }
        
        // 从数据库查找
        elderlyId = gpsLocationMapper.getElderlyIdByMacid(macid);
        if (elderlyId != null) {
            // 缓存绑定关系
            deviceElderlyMapping.put(macid, elderlyId);
        }
        
        return elderlyId;
    }

    @Override
    public boolean bindDeviceToElderly(String macid, Integer elderlyId) {
        try {
            // 更新缓存
            deviceElderlyMapping.put(macid, elderlyId);
            log.info("设备绑定成功，设备: {}, 老人ID: {}", macid, elderlyId);
            return true;
        } catch (Exception e) {
            log.error("设备绑定失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public int cleanOldLocationData(LocalDateTime beforeTime) {
        try {
            return gpsLocationMapper.deleteLocationsBefore(beforeTime);
        } catch (Exception e) {
            log.error("清理旧GPS数据失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 转换GPS数据DTO为实体对象
     */
    private GpsLocation convertToGpsLocation(GpsDataDTO.GpsLocationData locationData) {
        try {
            GpsLocation gpsLocation = new GpsLocation();
            gpsLocation.setMacid(locationData.getMacid());
            gpsLocation.setGpsTime(locationData.getGpsTime());
            gpsLocation.setHeartTime(locationData.getHeartTime());
            gpsLocation.setUpdTime(locationData.getUpdTime());
            
            // 转换坐标
            if (locationData.getLat() != null && !locationData.getLat().isEmpty()) {
                gpsLocation.setLat(Double.parseDouble(locationData.getLat()));
            }
            if (locationData.getLon() != null && !locationData.getLon().isEmpty()) {
                gpsLocation.setLon(Double.parseDouble(locationData.getLon()));
            }
            
            // 转换速度和方向
            if (locationData.getSpeed() != null && !locationData.getSpeed().isEmpty()) {
                gpsLocation.setSpeed(Double.parseDouble(locationData.getSpeed()));
            }
            if (locationData.getDir() != null && !locationData.getDir().isEmpty()) {
                gpsLocation.setDir(Double.parseDouble(locationData.getDir()));
            }
            
            gpsLocation.setStats(locationData.getStats());
            gpsLocation.setValue(locationData.getValue());
            
            return gpsLocation;
        } catch (Exception e) {
            log.error("转换GPS数据失败: {}", e.getMessage(), e);
            return null;
        }
    }
}