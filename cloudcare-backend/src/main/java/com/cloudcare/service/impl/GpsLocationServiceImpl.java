package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.GpsDevice;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.mapper.GpsDeviceMapper;
import com.cloudcare.mapper.GpsLocationMapper;
import com.cloudcare.service.GpsLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GPS位置服务实现类
 *
 * @author CloudCare
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GpsLocationServiceImpl extends ServiceImpl<GpsLocationMapper, GpsLocation> implements GpsLocationService {

    private final GpsLocationMapper gpsLocationMapper;
    
    @Autowired
    private GpsDeviceMapper gpsDeviceMapper;

    @Override
    public IPage<GpsLocation> getLocationList(Page<GpsLocation> page, String deviceImei, Long elderlyId, String startTime, String endTime) {
        QueryWrapper<GpsLocation> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(deviceImei)) {
            queryWrapper.eq("device_imei", deviceImei);
        }
        
        if (elderlyId != null) {
            // 根据老人ID查找设备IMEI
            QueryWrapper<GpsDevice> deviceQuery = new QueryWrapper<>();
            deviceQuery.eq("elderly_id", elderlyId);
            List<GpsDevice> devices = gpsDeviceMapper.selectList(deviceQuery);
            if (!devices.isEmpty()) {
                List<String> imeiList = devices.stream().map(GpsDevice::getMacid).toList();
                queryWrapper.in("device_imei", imeiList);
            } else {
                // 如果没有找到设备，返回空结果
                queryWrapper.eq("device_imei", "");
            }
        }
        
        if (StringUtils.hasText(startTime)) {
            queryWrapper.ge("location_time", LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.hasText(endTime)) {
            queryWrapper.le("location_time", LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        queryWrapper.orderByDesc("location_time");
        return this.page(page, queryWrapper);
    }

    @Override
    public GpsLocation getLatestLocationByImei(String deviceImei) {
        QueryWrapper<GpsLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_imei", deviceImei);
        queryWrapper.orderByDesc("location_time");
        queryWrapper.last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public GpsLocation getLatestLocationByElderlyId(Long elderlyId) {
        // 根据老人ID查找设备
        QueryWrapper<GpsDevice> deviceQuery = new QueryWrapper<>();
        deviceQuery.eq("elderly_id", elderlyId);
        GpsDevice device = gpsDeviceMapper.selectOne(deviceQuery);
        
        if (device != null) {
            return getLatestLocationByImei(device.getMacid());
        }
        return null;
    }

    @Override
    public List<GpsLocation> getLocationHistory(String deviceImei, String startTime, String endTime) {
        QueryWrapper<GpsLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_imei", deviceImei);
        
        if (StringUtils.hasText(startTime)) {
            queryWrapper.ge("location_time", LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.hasText(endTime)) {
            queryWrapper.le("location_time", LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        queryWrapper.orderByDesc("location_time");
        return this.list(queryWrapper);
    }

    @Override
    public List<GpsLocation> getElderlyLocationHistory(Long elderlyId, String startTime, String endTime) {
        // 根据老人ID查找设备
        QueryWrapper<GpsDevice> deviceQuery = new QueryWrapper<>();
        deviceQuery.eq("elderly_id", elderlyId);
        List<GpsDevice> devices = gpsDeviceMapper.selectList(deviceQuery);
        
        if (!devices.isEmpty()) {
            QueryWrapper<GpsLocation> locationQuery = new QueryWrapper<>();
            List<String> imeiList = devices.stream().map(GpsDevice::getMacid).toList();
            locationQuery.in("device_imei", imeiList);
            
            if (StringUtils.hasText(startTime)) {
                locationQuery.ge("location_time", LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (StringUtils.hasText(endTime)) {
                locationQuery.le("location_time", LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            
            locationQuery.orderByDesc("location_time");
            return this.list(locationQuery);
        }
        return null;
    }

    @Override
    public boolean saveBatch(List<GpsLocation> locations) {
        try {
            return this.saveBatch(locations, 1000); // 批量保存，每批1000条
        } catch (Exception e) {
            log.error("批量保存位置数据失败", e);
            return false;
        }
    }
}