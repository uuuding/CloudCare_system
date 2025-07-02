package com.cloudcare.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.GeoFence;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.mapper.GeoFenceMapper;
import com.cloudcare.service.GeoFenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 电子围栏服务实现类
 *
 * @author CloudCare
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeoFenceServiceImpl extends ServiceImpl<GeoFenceMapper, GeoFence> implements GeoFenceService {

    private final GeoFenceMapper geoFenceMapper;

    @Override
    public IPage<GeoFence> getGeoFencePage(Page<GeoFence> page, Long elderlyId, Integer status) {
        LambdaQueryWrapper<GeoFence> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(elderlyId != null, GeoFence::getElderlyId, elderlyId)
               .eq(status != null, GeoFence::getStatus, status)
               .orderByDesc(GeoFence::getCreateTime);
        return geoFenceMapper.selectPage(page, wrapper);
    }

    @Override
    public List<GeoFence> getGeoFencesByElderlyId(Long elderlyId) {
        return geoFenceMapper.selectList(
            new LambdaQueryWrapper<GeoFence>()
                .eq(GeoFence::getElderlyId, elderlyId)
                .eq(GeoFence::getStatus, 1)
                .orderByDesc(GeoFence::getCreateTime)
        );
    }

    @Override
    @Transactional
    public boolean createGeoFence(GeoFence geoFence) {
        try {
            geoFence.setCreateTime(LocalDateTime.now());
            geoFence.setUpdateTime(LocalDateTime.now());
            return geoFenceMapper.insert(geoFence) > 0;
        } catch (Exception e) {
            log.error("创建围栏失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateGeoFence(GeoFence geoFence) {
        try {
            geoFence.setUpdateTime(LocalDateTime.now());
            return geoFenceMapper.updateById(geoFence) > 0;
        } catch (Exception e) {
            log.error("更新围栏失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteGeoFence(Long id) {
        try {
            return geoFenceMapper.deleteById(id) > 0;
        } catch (Exception e) {
            log.error("删除围栏失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateGeoFenceStatus(Long id, Integer status) {
        try {
            GeoFence geoFence = new GeoFence();
            geoFence.setId(id);
            geoFence.setStatus(status);
            geoFence.setUpdateTime(LocalDateTime.now());
            return geoFenceMapper.updateById(geoFence) > 0;
        } catch (Exception e) {
            log.error("更新围栏状态失败", e);
            return false;
        }
    }

    @Override
    public boolean isLocationInFence(GeoFence geoFence, Double lat, Double lon) {
        if (geoFence == null || lat == null || lon == null) {
            return false;
        }

        try {
            if (geoFence.getFenceType() == 1) {
                // 圆形围栏
                double distance = calculateDistance(
                    geoFence.getCenterLat(), geoFence.getCenterLon(),
                    lat, lon
                );
                return distance <= geoFence.getRadius();
            } else if (geoFence.getFenceType() == 2) {
                // 多边形围栏
                return isPointInPolygon(lat, lon, geoFence.getPolygonPoints());
            }
        } catch (Exception e) {
            log.error("检查位置是否在围栏内失败", e);
        }
        
        return false;
    }

    @Override
    public List<GeoFence> checkFenceAlarms(Long elderlyId, GpsLocation location) {
        List<GeoFence> triggeredFences = new ArrayList<>();
        
        try {
            // 获取该老人的所有启用的围栏
            List<GeoFence> fences = getGeoFencesByElderlyId(elderlyId);
            
            for (GeoFence fence : fences) {
                // 检查时间范围
                if (!isInTimeRange(fence)) {
                    continue;
                }
                
                boolean isInFence = isLocationInFence(fence, location.getMapLat(), location.getMapLon());
                
                // 根据报警类型判断是否触发报警
                if ((fence.getAlertType() == 1 && !isInFence) ||  // 离开围栏报警
                    (fence.getAlertType() == 2 && isInFence)) {    // 进入围栏报警
                    triggeredFences.add(fence);
                }
            }
        } catch (Exception e) {
            log.error("检查围栏报警失败", e);
        }
        
        return triggeredFences;
    }

    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 6371000; // 地球半径，单位：米
        
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(radLat1) * Math.cos(radLat2) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }

    @Override
    public boolean isPointInPolygon(double lat, double lon, String polygonPoints) {
        try {
            JSONArray points = JSON.parseArray(polygonPoints);
            if (points == null || points.size() < 3) {
                return false;
            }
            
            int intersectCount = 0;
            for (int i = 0; i < points.size(); i++) {
                JSONObject point1 = points.getJSONObject(i);
                JSONObject point2 = points.getJSONObject((i + 1) % points.size());
                
                double lat1 = point1.getDouble("lat");
                double lon1 = point1.getDouble("lon");
                double lat2 = point2.getDouble("lat");
                double lon2 = point2.getDouble("lon");
                
                // 射线法判断点是否在多边形内
                if (((lat1 <= lat && lat < lat2) || (lat2 <= lat && lat < lat1)) &&
                    (lon < (lon2 - lon1) * (lat - lat1) / (lat2 - lat1) + lon1)) {
                    intersectCount++;
                }
            }
            
            return intersectCount % 2 == 1;
        } catch (Exception e) {
            log.error("检查点是否在多边形内失败", e);
            return false;
        }
    }

    /**
     * 检查当前时间是否在围栏生效时间范围内
     */
    private boolean isInTimeRange(GeoFence fence) {
        if (fence.getEffectiveTime() == null) {
            return true; // 没有时间限制，全天生效
        }
        
        try {
            // 解析effectiveTime JSON字符串获取时间范围
             // 这里需要根据实际的JSON格式来解析
             // 暂时返回true，表示全天生效
             return true;
        } catch (Exception e) {
            log.error("检查时间范围失败", e);
            return true;
        }
    }
}