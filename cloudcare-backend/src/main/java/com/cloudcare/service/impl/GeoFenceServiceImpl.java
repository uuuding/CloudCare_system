package com.cloudcare.service.impl;

import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GeoFenceEvent;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.mapper.GeoFenceMapper;
import com.cloudcare.service.GeoFenceEventService;
import com.cloudcare.service.GeoFenceService;
import com.cloudcare.service.ElderlyProfileService;
import com.cloudcare.service.DeviceBindingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 电子围栏服务实现类
 * 实现电子围栏的创建、管理和事件检测功能
 * 支持圆形和多边形围栏，提供进入/离开事件检测和提醒发送
 * 使用地理坐标计算算法判断位置与围栏的关系
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeoFenceServiceImpl implements GeoFenceService {

    private final GeoFenceMapper geoFenceMapper;
    private final GeoFenceEventService geoFenceEventService;
    private final ObjectMapper objectMapper;
    private final ElderlyProfileService elderlyProfileService;
    private final DeviceBindingService deviceBindingService;
    
    // 缓存老人的上一次围栏状态，用于判断进入/离开事件
    // Key: elderlyId + "-" + fenceId, Value: 是否在围栏内
    private final Map<String, Boolean> lastFenceStatus = new ConcurrentHashMap<>();

    @Override
    public boolean createGeoFence(GeoFence geoFence) {
        try {
            geoFence.setCreateTime(LocalDateTime.now());
            geoFence.setUpdateTime(LocalDateTime.now());
            return geoFenceMapper.insert(geoFence) > 0;
        } catch (Exception e) {
            log.error("创建电子围栏失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateGeoFence(GeoFence geoFence) {
        try {
            geoFence.setUpdateTime(LocalDateTime.now());
            return geoFenceMapper.updateById(geoFence) > 0;
        } catch (Exception e) {
            log.error("更新电子围栏失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteGeoFence(Long fenceId) {
        try {
            return geoFenceMapper.deleteById(fenceId) > 0;
        } catch (Exception e) {
            log.error("删除电子围栏失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<GeoFence> getActiveFencesByElderlyId(Integer elderlyId) {
        return geoFenceMapper.getActiveFencesByElderlyId(elderlyId);
    }

    @Override
    public List<GeoFence> getAllFences() {
        return geoFenceMapper.getAllFences();
    }

    @Override
    public List<GeoFence> getAllFencesByElderlyId(Integer elderlyId) {
        return geoFenceMapper.getAllFencesByElderlyId(elderlyId);
    }

    @Override
    public GeoFence getFenceById(Long fenceId) {
        return geoFenceMapper.getFenceById(fenceId);
    }

    @Override
    public boolean updateFenceStatus(Long fenceId, Integer status) {
        try {
            return geoFenceMapper.updateFenceStatus(fenceId, status) > 0;
        } catch (Exception e) {
            log.error("更新围栏状态失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 判断位置是否在围栏内
     * 根据围栏类型（圆形或多边形）使用不同的算法进行判断
     * 
     * @param gpsLocation GPS位置信息
     * @param geoFence 围栏信息
     * @return true-位置在围栏内，false-位置在围栏外或围栏不存在
     */
    @Override
    public boolean isLocationInFence(GpsLocation gpsLocation, GeoFence geoFence) {
        // 优先使用地图坐标，如果不可用则使用GPS坐标
        Double lat = gpsLocation.getMapLat() != null ? gpsLocation.getMapLat() : gpsLocation.getLat();
        Double lon = gpsLocation.getMapLon() != null ? gpsLocation.getMapLon() : gpsLocation.getLon();
        
        if (lat == null || lon == null) {
            return false;
        }
        
        if ("circle".equals(geoFence.getFenceType())) {
            // 圆形围栏检测
            if (geoFence.getCenterLat() == null || geoFence.getCenterLon() == null || geoFence.getRadius() == null) {
                log.warn("圆形围栏参数不完整: centerLat={}, centerLon={}, radius={}", 
                        geoFence.getCenterLat(), geoFence.getCenterLon(), geoFence.getRadius());
                return false;
            }
            
            double distance = calculateDistance(
                    lat, lon,
                    geoFence.getCenterLat(), geoFence.getCenterLon()
            );
            
            boolean inFence = distance <= geoFence.getRadius();
            log.info("圆形围栏距离计算: GPS位置({}, {}), 围栏中心({}, {}), 距离={}米, 半径={}米, 在围栏内={}", 
                    lat, lon, geoFence.getCenterLat(), geoFence.getCenterLon(), 
                    Math.round(distance), Math.round(geoFence.getRadius()), inFence);
            
            return inFence;
            
        } else if ("polygon".equals(geoFence.getFenceType())) {
            // 多边形围栏检测
            return isPointInPolygon(lat, lon, geoFence.getCoordinates());
        }
        
        return false;
    }

    /**
     * 检查GPS位置并触发围栏事件
     * 根据老人当前位置检查所有激活的围栏，判断进入/离开事件并创建事件记录
     * 自动发送围栏事件提醒给相关联系人
     * 
     * @param gpsLocation GPS位置信息，包含老人ID和坐标信息
     */
    @Override
    @Transactional
    public void checkFenceEvents(GpsLocation gpsLocation) {
        if (gpsLocation.getElderlyId() == null) {
            log.debug("跳过围栏检查：GPS记录中elderly_id为空");
            return;
        }
        
        try {
            log.info("开始检查围栏事件，老人ID: {}, GPS位置: ({}, {}), 设备: {}", 
                    gpsLocation.getElderlyId(), 
                    gpsLocation.getMapLat() != null ? gpsLocation.getMapLat() : gpsLocation.getLat(),
                    gpsLocation.getMapLon() != null ? gpsLocation.getMapLon() : gpsLocation.getLon(),
                    gpsLocation.getMacid());
            
            // 获取该老人的所有启用围栏
            List<GeoFence> activeFences = getActiveFencesByElderlyId(gpsLocation.getElderlyId());
            log.info("找到启用围栏数量: {}", activeFences.size());
            
            for (GeoFence fence : activeFences) {
                log.info("检查围栏: ID={}, 名称={}, 类型={}, 进入提醒={}, 离开提醒={}", 
                        fence.getId(), fence.getFenceName(), fence.getFenceType(), 
                        fence.getEnterAlert(), fence.getExitAlert());
                
                String statusKey = gpsLocation.getElderlyId() + "-" + fence.getId();
                Boolean lastInFence = lastFenceStatus.get(statusKey);
                boolean currentInFence = isLocationInFence(gpsLocation, fence);
                
                log.info("围栏状态检查: 围栏={}, 上次状态={}, 当前状态={}", 
                        fence.getFenceName(), lastInFence, currentInFence);
                
                // 检查是否有状态变化
                if (lastInFence == null) {
                    // 首次检测，只记录状态，不触发事件
                    log.info("首次检测围栏状态，记录当前状态: {} (不触发事件)", currentInFence);
                    lastFenceStatus.put(statusKey, currentInFence);
                    continue;
                }
                
                if (lastInFence != currentInFence) {
                    // 状态发生变化，触发事件
                    String eventType = currentInFence ? "enter" : "exit";
                    log.info("围栏状态发生变化: {} -> {}, 触发{}事件", 
                            lastInFence, currentInFence, eventType);
                    
                    // 创建围栏事件记录（无论是否需要提醒都要记录事件）
                    GeoFenceEvent event = new GeoFenceEvent();
                    event.setElderlyId(gpsLocation.getElderlyId());
                    event.setFenceId(fence.getId());
                    event.setFenceName(fence.getFenceName());
                    event.setEventType(eventType);
                    // 优先使用地图坐标记录事件位置
                    Double eventLat = gpsLocation.getMapLat() != null ? gpsLocation.getMapLat() : gpsLocation.getLat();
                    Double eventLon = gpsLocation.getMapLon() != null ? gpsLocation.getMapLon() : gpsLocation.getLon();
                    event.setLat(eventLat);
                    event.setLon(eventLon);
                    event.setLocationId(gpsLocation.getId());
                    event.setMacid(gpsLocation.getMacid());
                    event.setEventTime(LocalDateTime.now());
                    event.setAlertSent(0);
                    event.setAlertType(fence.getAlertType());
                    
                    // 生成提醒内容
                    String alertContent = generateAlertContent(gpsLocation.getElderlyId(), fence.getFenceName(), eventType, eventLat, eventLon);
                    event.setAlertContent(alertContent);
                    
                    // 保存事件记录
                    boolean eventSaved = geoFenceEventService.saveGeoFenceEvent(event);
                    log.info("围栏事件记录保存{}: 事件类型={}, 围栏={}", 
                            eventSaved ? "成功" : "失败", eventType, fence.getFenceName());
                    
                    // 检查是否需要发送提醒
                    boolean shouldAlert = (currentInFence && fence.getEnterAlert() == 1) || 
                                        (!currentInFence && fence.getExitAlert() == 1);
                    
                    log.info("是否需要发送提醒: {}, 进入提醒={}, 离开提醒={}", 
                            shouldAlert, fence.getEnterAlert(), fence.getExitAlert());
                    
                    if (shouldAlert) {
                        // 发送提醒
                        geoFenceEventService.sendFenceEventAlert(event);
                    }
                    
                    // 获取老人姓名用于日志记录
                    String elderlyName = "未知老人";
                    try {
                        ElderlyProfile elderlyProfile = elderlyProfileService.getElderlyProfileById(gpsLocation.getElderlyId());
                        if (elderlyProfile != null) {
                            elderlyName = elderlyProfile.getName();
                        }
                    } catch (Exception e) {
                        log.warn("获取老人信息失败: elderlyId={}", gpsLocation.getElderlyId(), e);
                    }
                    
                    log.info("围栏事件处理完成: 老人{}(ID={}), 围栏={}, 事件类型={}, 位置=({}, {}), 设备MAC={}, 提醒发送={}", 
                            elderlyName, gpsLocation.getElderlyId(), fence.getFenceName(), eventType, 
                            eventLat, eventLon, gpsLocation.getMacid(), shouldAlert ? "是" : "否");
                    
                    // 更新状态缓存
                    lastFenceStatus.put(statusKey, currentInFence);
                }
            }
            
            log.info("围栏事件检查完成，老人ID: {}", gpsLocation.getElderlyId());
            
        } catch (Exception e) {
            log.error("检查围栏事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 计算两个地理坐标点之间的距离
     * 使用Haversine公式计算球面距离
     * 
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 两点之间的距离（米）
     */
    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371000; // 地球半径（米）
        
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLatRad = Math.toRadians(lat2 - lat1);
        double deltaLonRad = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }

    /**
     * 判断点是否在多边形内
     * 使用射线法（Ray Casting Algorithm）进行判断
     * 
     * @param lat 点的纬度
     * @param lon 点的经度
     * @param coordinates 多边形顶点坐标JSON字符串
     * @return true-点在多边形内，false-点在多边形外
     */
    @Override
    public boolean isPointInPolygon(double lat, double lon, String coordinates) {
        try {
            // 解析坐标点JSON
            List<Map<String, Double>> points = objectMapper.readValue(
                    coordinates, new TypeReference<List<Map<String, Double>>>() {});
            
            if (points.size() < 3) {
                return false; // 多边形至少需要3个点
            }
            
            // 使用射线法判断点是否在多边形内
            boolean inside = false;
            int j = points.size() - 1;
            
            for (int i = 0; i < points.size(); i++) {
                double xi = points.get(i).get("lat");
                double yi = points.get(i).get("lon");
                double xj = points.get(j).get("lat");
                double yj = points.get(j).get("lon");
                
                if (((yi > lon) != (yj > lon)) && (lat < (xj - xi) * (lon - yi) / (yj - yi) + xi)) {
                    inside = !inside;
                }
                j = i;
            }
            
            return inside;
            
        } catch (Exception e) {
            log.error("解析多边形坐标失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 生成提醒内容
     */
    private String generateAlertContent(Integer elderlyId, String fenceName, String eventType, Double lat, Double lon) {
        try {
            // 获取老人信息
            ElderlyProfile elderlyProfile = elderlyProfileService.getElderlyProfileById(elderlyId);
            String elderlyName = elderlyProfile != null ? elderlyProfile.getName() : "未知老人";
            
            String action = "enter".equals(eventType) ? "进入" : "离开";
            String locationInfo = "";
            if (lat != null && lon != null) {
                locationInfo = String.format("，当前位置：纬度%.6f，经度%.6f", lat, lon);
            }
            
            // 获取当前时间
            String currentTime = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            return String.format("【云护CloudCare】老人%s已%s电子围栏\"%s\"%s，事件时间：%s，请及时关注。", 
                    elderlyName, action, fenceName, locationInfo, currentTime);
        } catch (Exception e) {
            log.error("生成提醒内容失败: elderlyId={}, fenceName={}, eventType={}", elderlyId, fenceName, eventType, e);
            String action = "enter".equals(eventType) ? "进入" : "离开";
            return String.format("【云护CloudCare】老人（ID:%d）已%s电子围栏\"%s\"，请及时关注。", 
                    elderlyId, action, fenceName);
        }
    }
}