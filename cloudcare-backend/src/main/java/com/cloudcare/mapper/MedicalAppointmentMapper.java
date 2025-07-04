package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.dto.MedicalAppointmentQueryDTO;
import com.cloudcare.entity.MedicalAppointment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 查询即将开始的预约（用于短信提醒）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 预约列表
     */
    @Select("SELECT ma.*, ep.name as elderly_name " +
            "FROM medical_appointment ma " +
            "LEFT JOIN elderly_profile ep ON ma.elder_id = ep.elder_id " +
            "WHERE ma.appointment_time >= #{startTime} " +
            "AND ma.appointment_time < #{endTime} " +
            "AND ma.status = 1 " +
            "AND ma.deleted = 0 " +
            "ORDER BY ma.appointment_time ASC")
    List<MedicalAppointment> findUpcomingAppointments(@Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据老人ID删除所有医疗预约记录
     */
    @Delete("DELETE FROM medical_appointment WHERE elder_id = #{elderId}")
    int deleteAppointmentsByElderId(@Param("elderId") int elderId);
}