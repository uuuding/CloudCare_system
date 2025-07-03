package com.cloudcare.service;

import com.cloudcare.entity.DeviceBinding;

import java.util.List;
import java.util.Map;

/**
 * 设备绑定服务接口
 */
public interface DeviceBindingService {

    /**
     * 绑定设备到老人
     * @param macid 设备编号
     * @param elderlyId 老人ID
     * @param createBy 创建者
     * @return 绑定结果
     */
    boolean bindDevice(String macid, Integer elderlyId, String createBy);

    /**
     * 解绑设备
     * @param macid 设备编号
     * @param elderlyId 老人ID
     * @param updateBy 更新者
     * @return 解绑结果
     */
    boolean unbindDevice(String macid, Integer elderlyId, String updateBy);

    /**
     * 获取所有设备绑定关系
     * @return 绑定关系列表
     */
    List<Map<String, Object>> getAllDeviceBindings();

    /**
     * 根据设备编号查询绑定关系
     * @param macid 设备编号
     * @return 绑定关系
     */
    DeviceBinding getBindingByMacid(String macid);

    /**
     * 根据老人ID查询绑定关系
     * @param elderlyId 老人ID
     * @return 绑定关系列表
     */
    List<DeviceBinding> getBindingsByElderlyId(Integer elderlyId);

    /**
     * 检查设备是否已绑定
     * @param macid 设备编号
     * @param elderlyId 老人ID
     * @return 是否已绑定
     */
    boolean isDeviceBound(String macid, Integer elderlyId);

    /**
     * 根据设备编号获取绑定的老人ID
     * @param macid 设备编号
     * @return 老人ID，如果未绑定返回null
     */
    Integer getElderlyIdByMacid(String macid);
}