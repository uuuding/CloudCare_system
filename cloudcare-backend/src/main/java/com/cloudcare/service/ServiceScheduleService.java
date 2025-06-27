package com.cloudcare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.ServiceSchedule;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务调度Service接口
 *
 * @author CloudCare
 * @since 2024-01-01
 */
public interface ServiceScheduleService extends IService<ServiceSchedule> {

    /**
     * 分页查询服务调度列表
     *
     * @param current     当前页
     * @param size        每页大小
     * @param serviceName 服务名称
     * @param serviceType 服务类型
     * @param elderId     老人ID
     * @param staffId     服务人员ID
     * @param status      状态
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 分页结果
     */
    Page<ServiceSchedule> getSchedulePage(Long current, Long size, String serviceName,
                                        Integer serviceType, Long elderId, Long staffId,
                                        Integer status, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据老人ID查询服务调度列表
     *
     * @param elderId 老人ID
     * @return 服务调度列表
     */
    List<ServiceSchedule> getSchedulesByElderId(Long elderId);

    /**
     * 根据服务人员ID查询服务调度列表
     *
     * @param staffId 服务人员ID
     * @return 服务调度列表
     */
    List<ServiceSchedule> getSchedulesByStaffId(Long staffId);

    /**
     * 根据时间范围查询服务调度列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 服务调度列表
     */
    List<ServiceSchedule> getSchedulesByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据状态查询服务调度列表
     *
     * @param status 状态
     * @return 服务调度列表
     */
    List<ServiceSchedule> getSchedulesByStatus(Integer status);

    /**
     * 创建服务调度
     *
     * @param serviceSchedule 服务调度信息
     * @return 是否成功
     */
    boolean createSchedule(ServiceSchedule serviceSchedule);

    /**
     * 更新服务调度
     *
     * @param serviceSchedule 服务调度信息
     * @return 是否成功
     */
    boolean updateSchedule(ServiceSchedule serviceSchedule);

    /**
     * 删除服务调度
     *
     * @param scheduleId 调度ID
     * @return 是否成功
     */
    boolean deleteSchedule(Long scheduleId);

    /**
     * 开始执行服务
     *
     * @param scheduleId 调度ID
     * @return 是否成功
     */
    boolean startService(Long scheduleId);

    /**
     * 完成服务
     *
     * @param scheduleId 调度ID
     * @param remark     服务备注
     * @return 是否成功
     */
    boolean completeService(Long scheduleId, String remark);

    /**
     * 取消服务
     *
     * @param scheduleId 调度ID
     * @param remark     取消原因
     * @return 是否成功
     */
    boolean cancelService(Long scheduleId, String remark);

    /**
     * 获取今日待执行的服务调度列表
     *
     * @return 服务调度列表
     */
    List<ServiceSchedule> getTodayPendingSchedules();

    /**
     * 获取紧急服务调度列表
     *
     * @return 服务调度列表
     */
    List<ServiceSchedule> getUrgentSchedules();
}