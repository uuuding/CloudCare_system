package com.cloudcare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.dto.MedicalAppointmentDTO;
import com.cloudcare.dto.MedicalAppointmentQueryDTO;
import com.cloudcare.entity.MedicalAppointment;

/**
 * 医疗预约服务接口
 *
 * @author CloudCare
 * @since 2024-01-01
 */
public interface MedicalAppointmentService extends IService<MedicalAppointment> {

    /**
     * 分页查询医疗预约列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<MedicalAppointment> getAppointmentPage(MedicalAppointmentQueryDTO queryDTO);

    /**
     * 根据ID查询医疗预约详情
     *
     * @param appointmentId 预约ID
     * @return 预约详情
     */
    MedicalAppointment getAppointmentById(Long appointmentId);

    /**
     * 创建医疗预约
     *
     * @param appointmentDTO 预约信息
     * @return 是否成功
     */
    boolean createAppointment(MedicalAppointmentDTO appointmentDTO);

    /**
     * 更新医疗预约
     *
     * @param appointmentDTO 预约信息
     * @return 是否成功
     */
    boolean updateAppointment(MedicalAppointmentDTO appointmentDTO);

    /**
     * 删除医疗预约
     *
     * @param appointmentId 预约ID
     * @return 是否成功
     */
    boolean deleteAppointment(Long appointmentId);

    /**
     * 更新预约状态
     *
     * @param appointmentId 预约ID
     * @param status 新状态
     * @return 是否成功
     */
    boolean updateAppointmentStatus(Long appointmentId, Integer status);
}