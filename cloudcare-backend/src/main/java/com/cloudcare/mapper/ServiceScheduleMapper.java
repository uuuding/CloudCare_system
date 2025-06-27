package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.ServiceSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务调度Mapper接口
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Mapper
public interface ServiceScheduleMapper extends BaseMapper<ServiceSchedule> {

    /**
     * 根据老人ID查询服务调度列表
     *
     * @param elderId 老人ID
     * @return 服务调度列表
     */
    @Select("SELECT ss.*, u1.real_name as elder_name, u2.real_name as staff_name " +
            "FROM service_schedule ss " +
            "LEFT JOIN sys_user u1 ON ss.elder_id = u1.user_id " +
            "LEFT JOIN sys_user u2 ON ss.staff_id = u2.user_id " +
            "WHERE ss.elder_id = #{elderId} AND ss.deleted = 0 " +
            "ORDER BY ss.scheduled_start_time DESC")
    List<ServiceSchedule> selectByElderId(@Param("elderId") Long elderId);

    /**
     * 根据服务人员ID查询服务调度列表
     *
     * @param staffId 服务人员ID
     * @return 服务调度列表
     */
    @Select("SELECT ss.*, u1.real_name as elder_name, u2.real_name as staff_name " +
            "FROM service_schedule ss " +
            "LEFT JOIN sys_user u1 ON ss.elder_id = u1.user_id " +
            "LEFT JOIN sys_user u2 ON ss.staff_id = u2.user_id " +
            "WHERE ss.staff_id = #{staffId} AND ss.deleted = 0 " +
            "ORDER BY ss.scheduled_start_time DESC")
    List<ServiceSchedule> selectByStaffId(@Param("staffId") Long staffId);

    /**
     * 根据时间范围查询服务调度列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 服务调度列表
     */
    @Select("SELECT ss.*, u1.real_name as elder_name, u2.real_name as staff_name " +
            "FROM service_schedule ss " +
            "LEFT JOIN sys_user u1 ON ss.elder_id = u1.user_id " +
            "LEFT JOIN sys_user u2 ON ss.staff_id = u2.user_id " +
            "WHERE ss.scheduled_start_time >= #{startTime} " +
            "AND ss.scheduled_end_time <= #{endTime} " +
            "AND ss.deleted = 0 " +
            "ORDER BY ss.scheduled_start_time ASC")
    List<ServiceSchedule> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    /**
     * 根据状态查询服务调度列表
     *
     * @param status 状态
     * @return 服务调度列表
     */
    @Select("SELECT ss.*, u1.real_name as elder_name, u2.real_name as staff_name " +
            "FROM service_schedule ss " +
            "LEFT JOIN sys_user u1 ON ss.elder_id = u1.user_id " +
            "LEFT JOIN sys_user u2 ON ss.staff_id = u2.user_id " +
            "WHERE ss.status = #{status} AND ss.deleted = 0 " +
            "ORDER BY ss.priority DESC, ss.scheduled_start_time ASC")
    List<ServiceSchedule> selectByStatus(@Param("status") Integer status);
}