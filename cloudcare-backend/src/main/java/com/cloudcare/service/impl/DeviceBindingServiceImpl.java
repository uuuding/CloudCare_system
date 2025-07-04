package com.cloudcare.service.impl;

import com.cloudcare.entity.DeviceBinding;
import com.cloudcare.mapper.DeviceBindingMapper;
import com.cloudcare.service.DeviceBindingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 设备绑定服务实现类
 * 管理GPS设备与老人的绑定关系，支持设备的绑定、解绑和查询操作
 * 确保每个设备只能绑定一个老人，每个老人可以绑定多个设备
 * 提供设备状态检查和绑定关系验证功能
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceBindingServiceImpl implements DeviceBindingService {

    private final DeviceBindingMapper deviceBindingMapper;

    @Override
    @Transactional
    public boolean bindDevice(String macid, Integer elderlyId, String createBy) {
        try {
            // 检查是否已经绑定
            DeviceBinding existingBinding = deviceBindingMapper.checkBinding(macid, elderlyId);
            if (existingBinding != null) {
                log.warn("设备 {} 已经绑定到老人 {}", macid, elderlyId);
                return false;
            }

            // 先解绑该设备的其他绑定关系
            deviceBindingMapper.unbindAllByMacid(macid);

            // 创建新的绑定关系
            DeviceBinding deviceBinding = new DeviceBinding();
            deviceBinding.setMacid(macid);
            deviceBinding.setElderlyId(elderlyId);
            deviceBinding.setBindTime(LocalDateTime.now());
            deviceBinding.setStatus(1);
            deviceBinding.setCreateTime(LocalDateTime.now());
            deviceBinding.setUpdateTime(LocalDateTime.now());
            deviceBinding.setCreateBy(createBy);

            int result = deviceBindingMapper.insert(deviceBinding);
            if (result > 0) {
                log.info("设备 {} 成功绑定到老人 {}", macid, elderlyId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("绑定设备失败: macid={}, elderlyId={}", macid, elderlyId, e);
            throw new RuntimeException("绑定设备失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean unbindDevice(String macid, Integer elderlyId, String updateBy) {
        try {
            int result = deviceBindingMapper.unbindDevice(macid, elderlyId);
            if (result > 0) {
                log.info("设备 {} 成功解绑老人 {}", macid, elderlyId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("解绑设备失败: macid={}, elderlyId={}", macid, elderlyId, e);
            throw new RuntimeException("解绑设备失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAllDeviceBindings() {
        try {
            return deviceBindingMapper.getAllDeviceBindings();
        } catch (Exception e) {
            log.error("获取设备绑定列表失败", e);
            throw new RuntimeException("获取设备绑定列表失败: " + e.getMessage());
        }
    }

    @Override
    public DeviceBinding getBindingByMacid(String macid) {
        try {
            return deviceBindingMapper.getBindingByMacid(macid);
        } catch (Exception e) {
            log.error("根据设备编号查询绑定关系失败: macid={}", macid, e);
            return null;
        }
    }

    @Override
    public List<DeviceBinding> getBindingsByElderlyId(Integer elderlyId) {
        try {
            return deviceBindingMapper.getBindingsByElderlyId(elderlyId);
        } catch (Exception e) {
            log.error("根据老人ID查询绑定关系失败: elderlyId={}", elderlyId, e);
            throw new RuntimeException("查询绑定关系失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isDeviceBound(String macid, Integer elderlyId) {
        try {
            DeviceBinding binding = deviceBindingMapper.checkBinding(macid, elderlyId);
            return binding != null;
        } catch (Exception e) {
            log.error("检查设备绑定状态失败: macid={}, elderlyId={}", macid, elderlyId, e);
            return false;
        }
    }

    @Override
    public Integer getElderlyIdByMacid(String macid) {
        try {
            log.debug("查询设备绑定关系，macid: [{}]", macid);
            DeviceBinding binding = deviceBindingMapper.getBindingByMacid(macid);
            if (binding != null) {
                log.debug("找到绑定关系，macid: [{}] -> elderlyId: {}, status: {}", 
                        macid, binding.getElderlyId(), binding.getStatus());
                return binding.getElderlyId();
            } else {
                log.warn("未找到设备 [{}] 的有效绑定关系", macid);
                return null;
            }
        } catch (Exception e) {
            log.error("根据设备编号获取老人ID失败: macid=[{}]", macid, e);
            return null;
        }
    }
}