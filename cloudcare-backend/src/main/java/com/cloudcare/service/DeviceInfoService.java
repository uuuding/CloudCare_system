package com.cloudcare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.DeviceInfo;

import java.util.List;
import java.util.Map;

/**
 * 设备信息服务接口
 *
 * @author cloudcare
 */
public interface DeviceInfoService extends IService<DeviceInfo> {

    /**
     * 分页查询设备信息
     *
     * @param page 分页参数
     * @param deviceInfo 查询条件
     * @return 分页结果
     */
    Page<DeviceInfo> selectDeviceInfoPage(Page<DeviceInfo> page, DeviceInfo deviceInfo);

    /**
     * 根据ID查询设备信息
     *
     * @param deviceId 设备ID
     * @return 设备信息
     */
    DeviceInfo selectDeviceInfoById(Long deviceId);

    /**
     * 新增设备信息
     *
     * @param deviceInfo 设备信息
     * @return 结果
     */
    boolean insertDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 修改设备信息
     *
     * @param deviceInfo 设备信息
     * @return 结果
     */
    boolean updateDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 批量删除设备信息
     *
     * @param deviceIds 需要删除的设备ID
     * @return 结果
     */
    boolean deleteDeviceInfoByIds(Long[] deviceIds);

    /**
     * 删除设备信息
     *
     * @param deviceId 设备ID
     * @return 结果
     */
    boolean deleteDeviceInfoById(Long deviceId);

    /**
     * 获取设备统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getDeviceStatistics();

    /**
     * 获取需要维护的设备列表
     *
     * @return 设备列表
     */
    List<DeviceInfo> getMaintenanceRequiredDevices();

    /**
     * 更新设备状态
     *
     * @param deviceId 设备ID
     * @param status 状态
     * @return 结果
     */
    boolean updateDeviceStatus(Long deviceId, Integer status);
}