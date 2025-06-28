package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.DeviceInfo;
import com.cloudcare.mapper.DeviceInfoMapper;
import com.cloudcare.service.DeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备信息服务实现类
 *
 * @author cloudcare
 */
@Slf4j
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements DeviceInfoService {

    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    @Override
    public Page<DeviceInfo> selectDeviceInfoPage(Page<DeviceInfo> page, DeviceInfo deviceInfo) {
        LambdaQueryWrapper<DeviceInfo> queryWrapper = new LambdaQueryWrapper<>();
        
        // 设备编号
        if (StringUtils.isNotBlank(deviceInfo.getDeviceCode())) {
            queryWrapper.like(DeviceInfo::getDeviceCode, deviceInfo.getDeviceCode());
        }
        
        // 设备名称
        if (StringUtils.isNotBlank(deviceInfo.getDeviceName())) {
            queryWrapper.like(DeviceInfo::getDeviceName, deviceInfo.getDeviceName());
        }
        
        // 设备类型
        if (deviceInfo.getDeviceType() != null) {
            queryWrapper.eq(DeviceInfo::getDeviceType, deviceInfo.getDeviceType());
        }
        
        // 设备状态
        if (deviceInfo.getDeviceStatus() != null) {
            queryWrapper.eq(DeviceInfo::getDeviceStatus, deviceInfo.getDeviceStatus());
        }
        
        // 负责人姓名
        if (StringUtils.isNotBlank(deviceInfo.getManagerName())) {
            queryWrapper.like(DeviceInfo::getManagerName, deviceInfo.getManagerName());
        }
        
        // 使用位置
        if (StringUtils.isNotBlank(deviceInfo.getLocation())) {
            queryWrapper.like(DeviceInfo::getLocation, deviceInfo.getLocation());
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc(DeviceInfo::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    public DeviceInfo selectDeviceInfoById(Long deviceId) {
        return this.getById(deviceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDeviceInfo(DeviceInfo deviceInfo) {
        // 生成设备编号
        if (StringUtils.isBlank(deviceInfo.getDeviceCode())) {
            deviceInfo.setDeviceCode(generateDeviceCode());
        }
        
        // 设置默认状态
        if (deviceInfo.getDeviceStatus() == null) {
            deviceInfo.setDeviceStatus(1); // 默认正常状态
        }
        
        return this.save(deviceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeviceInfo(DeviceInfo deviceInfo) {
        return this.updateById(deviceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDeviceInfoByIds(Long[] deviceIds) {
        return this.removeByIds(Arrays.asList(deviceIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDeviceInfoById(Long deviceId) {
        return this.removeById(deviceId);
    }

    @Override
    public Map<String, Object> getDeviceStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 设备总数
        long totalCount = this.count();
        result.put("totalCount", totalCount);
        
        // 正常设备数
        long normalCount = this.count(new LambdaQueryWrapper<DeviceInfo>()
                .eq(DeviceInfo::getDeviceStatus, 1));
        result.put("normalCount", normalCount);
        
        // 维修中设备数
        long maintenanceCount = this.count(new LambdaQueryWrapper<DeviceInfo>()
                .eq(DeviceInfo::getDeviceStatus, 2));
        result.put("maintenanceCount", maintenanceCount);
        
        // 停用设备数
        long disabledCount = this.count(new LambdaQueryWrapper<DeviceInfo>()
                .eq(DeviceInfo::getDeviceStatus, 0));
        result.put("disabledCount", disabledCount);
        
        // 设备类型统计
        List<Map<String, Object>> typeStatistics = deviceInfoMapper.getDeviceStatistics();
        result.put("typeStatistics", typeStatistics);
        
        // 设备状态统计
        List<Map<String, Object>> statusStatistics = deviceInfoMapper.getDeviceStatusStatistics();
        result.put("statusStatistics", statusStatistics);
        
        return result;
    }

    @Override
    public List<DeviceInfo> getMaintenanceRequiredDevices() {
        return deviceInfoMapper.getMaintenanceRequiredDevices();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeviceStatus(Long deviceId, Integer status) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setDeviceStatus(status);
        
        // 如果是维修状态，更新最后检修时间
        if (status == 2) {
            deviceInfo.setLastMaintenanceTime(LocalDateTime.now());
        }
        
        return this.updateById(deviceInfo);
    }

    /**
     * 生成设备编号
     */
    private String generateDeviceCode() {
        String prefix = "DEV";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }
}