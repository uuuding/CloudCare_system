package com.cloudcare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudcare.common.Result;
import com.cloudcare.common.annotation.Log;
import com.cloudcare.common.enums.BusinessType;
import com.cloudcare.dto.MedicalAppointmentDTO;
import com.cloudcare.dto.MedicalAppointmentQueryDTO;
import com.cloudcare.entity.MedicalAppointment;
import com.cloudcare.service.MedicalAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 医疗预约控制器
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/medical/appointment")
public class MedicalAppointmentController {

    @Autowired
    private MedicalAppointmentService medicalAppointmentService;

    /**
     * 分页查询医疗预约列表
     */
    @GetMapping("/page")
    public Result<IPage<MedicalAppointment>> getAppointmentPage(MedicalAppointmentQueryDTO queryDTO) {
        try {
            IPage<MedicalAppointment> page = medicalAppointmentService.getAppointmentPage(queryDTO);
            return Result.success(page);
        } catch (Exception e) {
            log.error("查询医疗预约列表失败", e);
            return Result.error("查询医疗预约列表失败");
        }
    }

    /**
     * 根据ID查询医疗预约详情
     */
    @GetMapping("/{appointmentId}")
    public Result<MedicalAppointment> getAppointmentById(@PathVariable Long appointmentId) {
        try {
            MedicalAppointment appointment = medicalAppointmentService.getAppointmentById(appointmentId);
            if (appointment == null) {
                return Result.error("预约信息不存在");
            }
            return Result.success(appointment);
        } catch (Exception e) {
            log.error("查询医疗预约详情失败", e);
            return Result.error("查询医疗预约详情失败");
        }
    }

    /**
     * 创建医疗预约
     */
    @PostMapping
    @Log(title = "MEDICAL", businessType = BusinessType.INSERT, isSaveRequestData = true, isSaveResponseData = true)
    public Result<String> createAppointment(@Validated @RequestBody MedicalAppointmentDTO appointmentDTO) {
        try {
            boolean success = medicalAppointmentService.createAppointment(appointmentDTO);
            if (success) {
                return Result.success("创建预约成功");
            } else {
                return Result.error("创建预约失败");
            }
        } catch (Exception e) {
            log.error("创建医疗预约失败", e);
            return Result.error("创建预约失败：" + e.getMessage());
        }
    }

    /**
     * 更新医疗预约
     */
    @PutMapping("/{appointmentId}")
    @Log(title = "MEDICAL", businessType = BusinessType.UPDATE, isSaveRequestData = true, isSaveResponseData = true)
    public Result<String> updateAppointment(@PathVariable Long appointmentId, 
                                           @Validated @RequestBody MedicalAppointmentDTO appointmentDTO) {
        try {
            appointmentDTO.setAppointmentId(appointmentId);
            boolean success = medicalAppointmentService.updateAppointment(appointmentDTO);
            if (success) {
                return Result.success("更新预约成功");
            } else {
                return Result.error("更新预约失败");
            }
        } catch (Exception e) {
            log.error("更新医疗预约失败", e);
            return Result.error("更新预约失败：" + e.getMessage());
        }
    }

    /**
     * 删除医疗预约
     */
    @DeleteMapping("/{appointmentId}")
    public Result<String> deleteAppointment(@PathVariable Long appointmentId) {
        try {
            boolean success = medicalAppointmentService.deleteAppointment(appointmentId);
            if (success) {
                return Result.success("删除预约成功");
            } else {
                return Result.error("删除预约失败");
            }
        } catch (Exception e) {
            log.error("删除医疗预约失败", e);
            return Result.error("删除预约失败：" + e.getMessage());
        }
    }

    /**
     * 更新预约状态
     */
    @PatchMapping("/{appointmentId}/status")
    public Result<String> updateAppointmentStatus(@PathVariable Long appointmentId, 
                                                 @RequestParam Integer status) {
        try {
            boolean success = medicalAppointmentService.updateAppointmentStatus(appointmentId, status);
            if (success) {
                return Result.success("更新预约状态成功");
            } else {
                return Result.error("更新预约状态失败");
            }
        } catch (Exception e) {
            log.error("更新预约状态失败", e);
            return Result.error("更新预约状态失败：" + e.getMessage());
        }
    }
}