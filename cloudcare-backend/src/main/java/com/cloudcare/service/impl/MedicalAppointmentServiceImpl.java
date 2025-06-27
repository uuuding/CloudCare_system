package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.dto.MedicalAppointmentDTO;
import com.cloudcare.dto.MedicalAppointmentQueryDTO;
import com.cloudcare.entity.MedicalAppointment;
import com.cloudcare.mapper.MedicalAppointmentMapper;
import com.cloudcare.service.MedicalAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 医疗预约服务实现类
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Slf4j
@Service
public class MedicalAppointmentServiceImpl extends ServiceImpl<MedicalAppointmentMapper, MedicalAppointment> implements MedicalAppointmentService {

    // 预约类型映射
    private static final Map<Integer, String> APPOINTMENT_TYPE_MAP = new HashMap<>();
    // 预约状态映射
    private static final Map<Integer, String> STATUS_MAP = new HashMap<>();

    static {
        APPOINTMENT_TYPE_MAP.put(1, "体检");
        APPOINTMENT_TYPE_MAP.put(2, "门诊");
        APPOINTMENT_TYPE_MAP.put(3, "专科");
        APPOINTMENT_TYPE_MAP.put(4, "急诊");

        STATUS_MAP.put(1, "待确认");
        STATUS_MAP.put(2, "已确认");
        STATUS_MAP.put(3, "已完成");
        STATUS_MAP.put(4, "已取消");
    }

    @Override
    public IPage<MedicalAppointment> getAppointmentPage(MedicalAppointmentQueryDTO queryDTO) {
        Page<MedicalAppointment> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        IPage<MedicalAppointment> result = baseMapper.selectAppointmentPage(page, queryDTO);
        
        // 设置显示文本
        result.getRecords().forEach(this::setDisplayText);
        
        return result;
    }

    @Override
    public MedicalAppointment getAppointmentById(Long appointmentId) {
        MedicalAppointment appointment = baseMapper.selectAppointmentById(appointmentId);
        if (appointment != null) {
            setDisplayText(appointment);
        }
        return appointment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createAppointment(MedicalAppointmentDTO appointmentDTO) {
        MedicalAppointment appointment = new MedicalAppointment();
        BeanUtils.copyProperties(appointmentDTO, appointment);
        
        // 设置默认状态为待确认
        if (appointment.getStatus() == null) {
            appointment.setStatus(1);
        }
        
        appointment.setCreateTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());
        
        return save(appointment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAppointment(MedicalAppointmentDTO appointmentDTO) {
        MedicalAppointment appointment = new MedicalAppointment();
        BeanUtils.copyProperties(appointmentDTO, appointment);
        
        appointment.setUpdateTime(LocalDateTime.now());
        
        return updateById(appointment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAppointment(Long appointmentId) {
        return removeById(appointmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAppointmentStatus(Long appointmentId, Integer status) {
        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setAppointmentId(appointmentId);
        appointment.setStatus(status);
        appointment.setUpdateTime(LocalDateTime.now());
        
        return updateById(appointment);
    }

    /**
     * 设置显示文本
     */
    private void setDisplayText(MedicalAppointment appointment) {
        appointment.setAppointmentTypeText(APPOINTMENT_TYPE_MAP.get(appointment.getAppointmentType()));
        appointment.setStatusText(STATUS_MAP.get(appointment.getStatus()));
    }
}