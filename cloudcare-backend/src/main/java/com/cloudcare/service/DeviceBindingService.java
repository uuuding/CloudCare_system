package com.cloudcare.service;

import com.cloudcare.entity.DeviceBinding;

import java.util.List;
import java.util.Map;

/**
 * 设备绑定服务接口
 * 负责管理GPS设备与老人之间的绑定关系，包括绑定、解绑、查询等操作
 * 每个设备只能绑定一个老人，但一个老人可以绑定多个设备
 * 
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
public interface DeviceBindingService {

    /**
     * 绑定设备到老人
     * 如果设备已绑定其他老人，会先解绑原有关系再建立新的绑定关系
     * 如果设备已绑定同一老人，则返回false表示重复绑定
     * 
     * @param macid 设备编号（IMEI），不能为null或空字符串
     * @param elderlyId 老人ID，不能为null
     * @param createBy 创建者用户名，用于记录操作人员
     * @return true-绑定成功，false-绑定失败（如重复绑定）
     * @throws RuntimeException 当绑定操作失败时抛出
     * @see DeviceBinding
     */
    boolean bindDevice(String macid, Integer elderlyId, String createBy);

    /**
     * 解绑设备与老人的绑定关系
     * 将绑定状态设置为无效，但不删除记录，保留历史信息
     * 
     * @param macid 设备编号（IMEI），不能为null或空字符串
     * @param elderlyId 老人ID，不能为null
     * @param updateBy 更新者用户名，用于记录操作人员
     * @return true-解绑成功，false-解绑失败（如绑定关系不存在）
     * @throws RuntimeException 当解绑操作失败时抛出
     */
    boolean unbindDevice(String macid, Integer elderlyId, String updateBy);

    /**
     * 获取所有有效的设备绑定关系
     * 返回包含设备信息和老人信息的完整绑定关系列表
     * 
     * @return 绑定关系列表，每个Map包含设备编号、老人ID、老人姓名、绑定时间等信息
     * @throws RuntimeException 当查询操作失败时抛出
     */
    List<Map<String, Object>> getAllDeviceBindings();

    /**
     * 根据设备编号查询当前有效的绑定关系
     * 只返回状态为有效的绑定记录
     * 
     * @param macid 设备编号（IMEI），不能为null或空字符串
     * @return 设备绑定关系实体，如果设备未绑定或绑定无效则返回null
     * @see DeviceBinding
     */
    DeviceBinding getBindingByMacid(String macid);

    /**
     * 根据老人ID查询所有有效的设备绑定关系
     * 一个老人可能绑定多个设备（如手表、手机等）
     * 
     * @param elderlyId 老人ID，不能为null
     * @return 该老人的所有有效设备绑定关系列表，如果没有绑定设备则返回空列表
     * @throws RuntimeException 当查询操作失败时抛出
     * @see DeviceBinding
     */
    List<DeviceBinding> getBindingsByElderlyId(Integer elderlyId);

    /**
     * 检查指定设备是否已绑定到指定老人
     * 用于验证绑定关系的有效性
     * 
     * @param macid 设备编号（IMEI），不能为null或空字符串
     * @param elderlyId 老人ID，不能为null
     * @return true-设备已绑定到该老人，false-未绑定或绑定无效
     */
    boolean isDeviceBound(String macid, Integer elderlyId);

    /**
     * 根据设备编号获取绑定的老人ID
     * 这是GPS数据处理中的关键方法，用于将设备数据关联到具体老人
     * 
     * @param macid 设备编号（IMEI），不能为null或空字符串
     * @return 绑定的老人ID，如果设备未绑定或绑定无效则返回null
     */
    Integer getElderlyIdByMacid(String macid);
}