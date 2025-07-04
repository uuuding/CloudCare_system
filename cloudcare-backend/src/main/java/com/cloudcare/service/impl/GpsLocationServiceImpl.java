package com.cloudcare.service.impl;

import com.cloudcare.dto.GpsDataDTO;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.mapper.GpsLocationMapper;
import com.cloudcare.service.DeviceBindingService;
import com.cloudcare.service.GeoFenceService;
import com.cloudcare.service.GpsLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * GPS定位服务实现类
 * 实现GPS设备数据的接收、解析、存储和围栏事件触发等核心功能
 * 支持多种GPS设备协议，自动处理设备绑定关系查询
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GpsLocationServiceImpl implements GpsLocationService {

    private final GpsLocationMapper gpsLocationMapper;
    private final GeoFenceService geoFenceService;
    private final DeviceBindingService deviceBindingService;

    @Override
    @Transactional
    public String processGpsData(GpsDataDTO gpsDataDTO) {
        // 输入验证
        if (gpsDataDTO == null) {
            log.error("GPS数据为空，无法处理");
            throw new IllegalArgumentException("GPS数据不能为空");
        }
        
        if (gpsDataDTO.getSerialNumber() == null || gpsDataDTO.getSerialNumber().trim().isEmpty()) {
            log.error("GPS数据序列号为空，无法处理");
            throw new IllegalArgumentException("GPS数据序列号不能为空");
        }
        
        try {
            log.info("接收到GPS数据推送，method: {}, serialNumber: {}, 数据条数: {}", 
                    gpsDataDTO.getMethod(), gpsDataDTO.getSerialNumber(), 
                    gpsDataDTO.getData() != null ? gpsDataDTO.getData().size() : 0);

            if ("status".equals(gpsDataDTO.getMethod()) && gpsDataDTO.getData() != null) {
                for (GpsDataDTO.GpsLocationData locationData : gpsDataDTO.getData()) {
                    // 记录接收到的原始macid
                    log.info("处理GPS数据，原始macid: [{}], 长度: {}", 
                            locationData.getMacid(), 
                            locationData.getMacid() != null ? locationData.getMacid().length() : 0);
                    
                    // 转换并保存GPS数据
                    GpsLocation gpsLocation = convertToGpsLocation(locationData);
                    if (gpsLocation != null) {
                        // 查询或获取老人ID
                        Integer elderlyId = getElderlyIdByMacid(locationData.getMacid());
                        log.info("设备绑定查询结果，macid: [{}], elderlyId: {}", 
                                locationData.getMacid(), elderlyId);
                        
                        if (elderlyId != null) {
                            gpsLocation.setElderlyId(elderlyId);
                            log.info("成功设置elderly_id: {} 到GPS记录", elderlyId);
                        } else {
                            log.warn("未找到设备 [{}] 的绑定关系，elderly_id将为null", locationData.getMacid());
                        }
                        
                        // 保存GPS记录
                        boolean saved = saveGpsLocation(gpsLocation);
                        if (saved) {
                            // 优先显示地图坐标
                            Double displayLat = gpsLocation.getMapLat() != null ? gpsLocation.getMapLat() : gpsLocation.getLat();
                            Double displayLon = gpsLocation.getMapLon() != null ? gpsLocation.getMapLon() : gpsLocation.getLon();
                            log.info("GPS数据保存成功，设备: [{}], 老人ID: {}, 位置: ({}, {})", 
                                    gpsLocation.getMacid(), gpsLocation.getElderlyId(), 
                                    displayLat, displayLon);
                            
                            // 检查围栏事件
                            if (elderlyId != null) {
                                geoFenceService.checkFenceEvents(gpsLocation);
                            } else {
                                log.warn("跳过围栏检查，因为设备 [{}] 未绑定老人", gpsLocation.getMacid());
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
        // 输入验证
        if (gpsLocation == null) {
            log.error("GPS定位记录为空，无法保存");
            return false;
        }
        
        if (gpsLocation.getMacid() == null || gpsLocation.getMacid().trim().isEmpty()) {
            log.error("GPS定位记录缺少设备ID，无法保存");
            return false;
        }
        
        try {
            LocalDateTime now = LocalDateTime.now();
            gpsLocation.setCreateTime(now);
            gpsLocation.setUpdateTime(now);
            
            int result = gpsLocationMapper.insert(gpsLocation);
            if (result > 0) {
                log.debug("GPS定位记录保存成功，设备ID: {}", gpsLocation.getMacid());
                return true;
            } else {
                log.warn("GPS定位记录保存失败，插入结果为0，设备ID: {}", gpsLocation.getMacid());
                return false;
            }
        } catch (Exception e) {
            log.error("保存GPS定位记录失败，设备ID: {}, 错误: {}", 
                    gpsLocation.getMacid(), e.getMessage(), e);
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
        // 输入验证
        if (elderlyId == null || elderlyId <= 0) {
            log.warn("老人ID无效: {}", elderlyId);
            return Collections.emptyList(); // 返回空列表而不是null
        }
        
        if (startTime == null || endTime == null) {
            log.warn("时间范围参数无效，startTime: {}, endTime: {}", startTime, endTime);
            return Collections.emptyList();
        }
        
        if (startTime.isAfter(endTime)) {
            log.warn("开始时间不能晚于结束时间，startTime: {}, endTime: {}", startTime, endTime);
            return Collections.emptyList();
        }
        
        try {
            List<GpsLocation> locations = gpsLocationMapper.getLocationsByElderlyIdAndTimeRange(elderlyId, startTime, endTime);
            log.debug("查询到{}条GPS轨迹记录，老人ID: {}, 时间范围: {} - {}", 
                    locations != null ? locations.size() : 0, elderlyId, startTime, endTime);
            return locations != null ? locations : Collections.emptyList();
        } catch (Exception e) {
            log.error("查询GPS轨迹记录失败，老人ID: {}, 时间范围: {} - {}, 错误: {}", 
                    elderlyId, startTime, endTime, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public Integer getElderlyIdByMacid(String macid) {
        // 使用新的设备绑定服务查询
        return deviceBindingService.getElderlyIdByMacid(macid);
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
     * 将接收到的GPS数据转换为数据库实体对象，处理坐标、速度、方向等字段的类型转换
     * 
     * @param locationData GPS位置数据DTO，包含字符串格式的坐标和其他属性
     * @return 转换后的GPS位置实体对象，转换失败时返回null
     */
    private GpsLocation convertToGpsLocation(GpsDataDTO.GpsLocationData locationData) {
        // 输入验证
        if (locationData == null) {
            log.warn("GPS位置数据为空，跳过转换");
            return null;
        }
        
        if (locationData.getMacid() == null || locationData.getMacid().trim().isEmpty()) {
            log.warn("GPS数据缺少设备ID(macid)，跳过转换");
            return null;
        }
        
        try {
            GpsLocation gpsLocation = new GpsLocation();
            gpsLocation.setMacid(locationData.getMacid().trim());
            gpsLocation.setGpsTime(locationData.getGpsTime());
            // 转换heartTime从String到Long
            if (locationData.getHeartTime() != null && !locationData.getHeartTime().isEmpty()) {
                try {
                    gpsLocation.setHeartTime(Long.parseLong(locationData.getHeartTime()));
                } catch (NumberFormatException e) {
                    log.warn("HeartTime格式错误，无法转换为Long: {}", locationData.getHeartTime());
                    gpsLocation.setHeartTime(null);
                }
            }
            gpsLocation.setUpdTime(locationData.getUpdTime());
            
            // 转换坐标（安全解析）
            gpsLocation.setLat(parseDoubleValue(locationData.getLat(), "纬度"));
            gpsLocation.setLon(parseDoubleValue(locationData.getLon(), "经度"));
            
            // 转换地图坐标（安全解析）
            gpsLocation.setMapLat(parseDoubleValue(locationData.getMapLat(), "地图纬度"));
            gpsLocation.setMapLon(parseDoubleValue(locationData.getMapLon(), "地图经度"));
            
            // 转换速度和方向（安全解析）
            gpsLocation.setSpeed(parseDoubleValue(locationData.getSpeed(), "速度"));
            gpsLocation.setDir(parseDoubleValue(locationData.getDir(), "方向"));
            
            gpsLocation.setStats(locationData.getStats());
            gpsLocation.setValue(locationData.getValue());
            
            return gpsLocation;
        } catch (Exception e) {
            log.error("转换GPS数据失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 安全解析Double值
     * 提供统一的数值解析逻辑，包含空值检查和异常处理
     * 
     * @param value 待解析的字符串值
     * @param fieldName 字段名称，用于日志记录
     * @return 解析后的Double值，解析失败时返回null
     */
    private Double parseDoubleValue(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            log.warn("{}格式错误，无法转换为Double: {}", fieldName, value);
            return null;
        }
    }
}