package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.GpsDevice;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.mapper.GpsDeviceMapper;
import com.cloudcare.mapper.GpsLocationMapper;
import com.cloudcare.service.GpsDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GPS设备服务实现类
 *
 * @author CloudCare
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GpsDeviceServiceImpl extends ServiceImpl<GpsDeviceMapper, GpsDevice> implements GpsDeviceService {

    private final GpsDeviceMapper gpsDeviceMapper;
    
    @Autowired
    private GpsLocationMapper gpsLocationMapper;

    @Override
    public IPage<GpsDevice> getDeviceList(Page<GpsDevice> page, String deviceName, String imei, Long elderlyId, Integer deviceStatus) {
        QueryWrapper<GpsDevice> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(deviceName)) {
            queryWrapper.like("device_name", deviceName);
        }
        if (StringUtils.hasText(imei)) {
            queryWrapper.like("imei", imei);
        }
        if (elderlyId != null) {
            queryWrapper.eq("elderly_id", elderlyId);
        }
        if (deviceStatus != null) {
            queryWrapper.eq("device_status", deviceStatus);
        }
        
        queryWrapper.orderByDesc("create_time");
        return this.page(page, queryWrapper);
    }

    @Override
    public GpsDevice getByImei(String imei) {
        QueryWrapper<GpsDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("imei", imei);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean bindDevice(Long deviceId, Long elderlyId) {
        try {
            GpsDevice device = this.getById(deviceId);
            if (device != null) {
                device.setElderlyId(elderlyId);
                device.setUpdateTime(LocalDateTime.now());
                return this.updateById(device);
            }
            return false;
        } catch (Exception e) {
            log.error("绑定设备失败", e);
            return false;
        }
    }

    @Override
    public boolean unbindDevice(Long deviceId) {
        try {
            GpsDevice device = this.getById(deviceId);
            if (device != null) {
                device.setElderlyId(null);
                device.setUpdateTime(LocalDateTime.now());
                return this.updateById(device);
            }
            return false;
        } catch (Exception e) {
            log.error("解绑设备失败", e);
            return false;
        }
    }

    @Override
    public GpsLocation getDeviceLatestLocation(Long deviceId) {
        GpsDevice device = this.getById(deviceId);
        if (device != null) {
            return getLatestLocationByImei(device.getMacid());
        }
        return null;
    }

    @Override
    public GpsLocation getElderlyLatestLocation(Long elderlyId) {
        QueryWrapper<GpsDevice> deviceQuery = new QueryWrapper<>();
        deviceQuery.eq("elderly_id", elderlyId);
        GpsDevice device = this.getOne(deviceQuery);
        
        if (device != null) {
            return getLatestLocationByImei(device.getMacid());
        }
        return null;
    }

    @Override
    public List<GpsLocation> getElderlyLocationHistory(Long elderlyId, String startTime, String endTime) {
        QueryWrapper<GpsDevice> deviceQuery = new QueryWrapper<>();
        deviceQuery.eq("elderly_id", elderlyId);
        GpsDevice device = this.getOne(deviceQuery);
        
        if (device != null) {
            QueryWrapper<GpsLocation> locationQuery = new QueryWrapper<>();
            locationQuery.eq("device_imei", device.getMacid());
            
            if (StringUtils.hasText(startTime)) {
                locationQuery.ge("location_time", LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (StringUtils.hasText(endTime)) {
                locationQuery.le("location_time", LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            
            locationQuery.orderByDesc("location_time");
            return gpsLocationMapper.selectList(locationQuery);
        }
        return null;
    }

    private GpsLocation getLatestLocationByImei(String imei) {
        QueryWrapper<GpsLocation> locationQuery = new QueryWrapper<>();
        locationQuery.eq("device_imei", imei);
        locationQuery.orderByDesc("location_time");
        locationQuery.last("LIMIT 1");
        return gpsLocationMapper.selectOne(locationQuery);
    }
}