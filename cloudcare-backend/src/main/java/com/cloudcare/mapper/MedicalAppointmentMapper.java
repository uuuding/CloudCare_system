package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.dto.MedicalAppointmentQueryDTO;
import com.cloudcare.entity.MedicalAppointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 医疗预约Mapper接口
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Mapper
public interface MedicalAppointmentMapper extends BaseMapper<MedicalAppointment> {

    /**
     * 分页查询医疗预约列表（包含老人和医生信息）
     *
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<MedicalAppointment> selectAppointmentPage(Page<MedicalAppointment> page, @Param("query") MedicalAppointmentQueryDTO queryDTO);

    /**
     * 根据ID查询医疗预约详情（包含老人和医生信息）
     *
     * @param appointmentId 预约ID
     * @return 预约详情
     */
    MedicalAppointment selectAppointmentById(@Param("appointmentId") Long appointmentId);
}