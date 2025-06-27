package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.ServiceSchedule;
import com.cloudcare.mapper.ServiceScheduleMapper;
import com.cloudcare.service.ServiceScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 服务调度Service实现类
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceScheduleServiceImpl extends ServiceImpl<ServiceScheduleMapper, ServiceSchedule>
        implements ServiceScheduleService {

    private final ServiceScheduleMapper serviceScheduleMapper;

    @Override
    public Page<ServiceSchedule> getSchedulePage(Long current, Long size, String serviceName,
                                               Integer serviceType, Long elderId, Long staffId,
                                               Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        Page<ServiceSchedule> page = new Page<>(current, size);
        LambdaQueryWrapper<ServiceSchedule> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.like(StringUtils.hasText(serviceName), ServiceSchedule::getServiceName, serviceName)
               .eq(serviceType != null, ServiceSchedule::getServiceType, serviceType)
               .eq(elderId != null, ServiceSchedule::getElderId, elderId)
               .eq(staffId != null, ServiceSchedule::getStaffId, staffId)
               .eq(status != null, ServiceSchedule::getStatus, status)
               .ge(startTime != null, ServiceSchedule::getScheduledStartTime, startTime)
               .le(endTime != null, ServiceSchedule::getScheduledEndTime, endTime)
               .eq(ServiceSchedule::getDeleted, 0)
               .orderByDesc(ServiceSchedule::getPriority)
               .orderByAsc(ServiceSchedule::getScheduledStartTime);
        
        return this.page(page, wrapper);
    }

    @Override
    public List<ServiceSchedule> getSchedulesByElderId(Long elderId) {
        return serviceScheduleMapper.selectByElderId(elderId);
    }

    @Override
    public List<ServiceSchedule> getSchedulesByStaffId(Long staffId) {
        return serviceScheduleMapper.selectByStaffId(staffId);
    }

    @Override
    public List<ServiceSchedule> getSchedulesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return serviceScheduleMapper.selectByTimeRange(startTime, endTime);
    }

    @Override
    public List<ServiceSchedule> getSchedulesByStatus(Integer status) {
        return serviceScheduleMapper.selectByStatus(status);
    }

    @Override
    public boolean createSchedule(ServiceSchedule serviceSchedule) {
        try {
            // 设置默认状态为待执行
            if (serviceSchedule.getStatus() == null) {
                serviceSchedule.setStatus(1);
            }
            // 设置默认优先级为中等
            if (serviceSchedule.getPriority() == null) {
                serviceSchedule.setPriority(2);
            }
            return this.save(serviceSchedule);
        } catch (Exception e) {
            log.error("创建服务调度失败", e);
            return false;
        }
    }

    @Override
    public boolean updateSchedule(ServiceSchedule serviceSchedule) {
        try {
            return this.updateById(serviceSchedule);
        } catch (Exception e) {
            log.error("更新服务调度失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteSchedule(Long scheduleId) {
        try {
            return this.removeById(scheduleId);
        } catch (Exception e) {
            log.error("删除服务调度失败", e);
            return false;
        }
    }

    @Override
    public boolean startService(Long scheduleId) {
        try {
            ServiceSchedule schedule = this.getById(scheduleId);
            if (schedule == null) {
                log.warn("服务调度不存在: {}", scheduleId);
                return false;
            }
            
            schedule.setStatus(2); // 进行中
            schedule.setActualStartTime(LocalDateTime.now());
            return this.updateById(schedule);
        } catch (Exception e) {
            log.error("开始执行服务失败", e);
            return false;
        }
    }

    @Override
    public boolean completeService(Long scheduleId, String remark) {
        try {
            ServiceSchedule schedule = this.getById(scheduleId);
            if (schedule == null) {
                log.warn("服务调度不存在: {}", scheduleId);
                return false;
            }
            
            schedule.setStatus(3); // 已完成
            schedule.setActualEndTime(LocalDateTime.now());
            if (StringUtils.hasText(remark)) {
                schedule.setRemark(remark);
            }
            return this.updateById(schedule);
        } catch (Exception e) {
            log.error("完成服务失败", e);
            return false;
        }
    }

    @Override
    public boolean cancelService(Long scheduleId, String remark) {
        try {
            ServiceSchedule schedule = this.getById(scheduleId);
            if (schedule == null) {
                log.warn("服务调度不存在: {}", scheduleId);
                return false;
            }
            
            schedule.setStatus(4); // 已取消
            if (StringUtils.hasText(remark)) {
                schedule.setRemark(remark);
            }
            return this.updateById(schedule);
        } catch (Exception e) {
            log.error("取消服务失败", e);
            return false;
        }
    }

    @Override
    public List<ServiceSchedule> getTodayPendingSchedules() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        
        LambdaQueryWrapper<ServiceSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceSchedule::getStatus, 1) // 待执行
               .ge(ServiceSchedule::getScheduledStartTime, startOfDay)
               .le(ServiceSchedule::getScheduledStartTime, endOfDay)
               .eq(ServiceSchedule::getDeleted, 0)
               .orderByDesc(ServiceSchedule::getPriority)
               .orderByAsc(ServiceSchedule::getScheduledStartTime);
        
        return this.list(wrapper);
    }

    @Override
    public List<ServiceSchedule> getUrgentSchedules() {
        LambdaQueryWrapper<ServiceSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceSchedule::getPriority, 4) // 紧急
               .in(ServiceSchedule::getStatus, 1, 2) // 待执行或进行中
               .eq(ServiceSchedule::getDeleted, 0)
               .orderByAsc(ServiceSchedule::getScheduledStartTime);
        
        return this.list(wrapper);
    }
}