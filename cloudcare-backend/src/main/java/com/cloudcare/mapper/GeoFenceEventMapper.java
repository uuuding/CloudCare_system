package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.GeoFenceEvent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 电子围栏事件记录Mapper接口
 * 提供围栏事件数据的数据库访问操作，包括事件记录的增删改查
 * 支持按老人ID、围栏ID、时间范围等条件查询事件记录
 * 提供事件统计、状态管理和提醒发送状态跟踪功能
 *
 * @author CloudCare Team
 * @version 1.0
 * @since 2024-01-01
 */
@Mapper
public interface GeoFenceEventMapper extends BaseMapper<GeoFenceEvent> {

    /**
     * 根据老人ID查询围栏事件记录
     * 查询指定老人的所有围栏事件，用于事件历史查看
     *
     * @param elderlyId 老人ID
     * @return 围栏事件记录列表，按事件时间倒序排列
     */
    @Select("SELECT * FROM geo_fence_event WHERE elderly_id = #{elderlyId} ORDER BY event_time DESC")
    List<GeoFenceEvent> getEventsByElderlyId(@Param("elderlyId") Integer elderlyId);

    /**
     * 根据老人ID和时间范围查询围栏事件记录
     * 用于统计分析和历史数据查询
     *
     * @param elderlyId 老人ID
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return 指定时间范围内的围栏事件记录列表
     */
    @Select("SELECT * FROM geo_fence_event WHERE elderly_id = #{elderlyId} " +
            "AND event_time BETWEEN #{startTime} AND #{endTime} " +
            "ORDER BY event_time DESC")
    List<GeoFenceEvent> getEventsByElderlyIdAndTimeRange(
            @Param("elderlyId") Integer elderlyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据围栏ID查询事件记录
     */
    @Select("SELECT * FROM geo_fence_event WHERE fence_id = #{fenceId} ORDER BY event_time DESC")
    List<GeoFenceEvent> getEventsByFenceId(@Param("fenceId") Long fenceId);

    /**
     * 查询未发送提醒的事件记录
     */
    @Select("SELECT * FROM geo_fence_event WHERE alert_sent = 0 ORDER BY event_time ASC")
    List<GeoFenceEvent> getUnsentAlertEvents();

    /**
     * 更新事件提醒发送状态
     */
    @Update("UPDATE geo_fence_event SET alert_sent = 1, alert_sent_time = NOW() WHERE id = #{eventId}")
    int updateAlertSentStatus(@Param("eventId") Long eventId);

    /**
     * 查询最近的围栏事件（用于仪表盘显示）
     */
    @Select("SELECT * FROM geo_fence_event ORDER BY event_time DESC LIMIT #{limit}")
    List<GeoFenceEvent> getRecentEvents(@Param("limit") Integer limit);

    /**
     * 统计指定时间范围内的事件数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event WHERE event_time BETWEEN #{startTime} AND #{endTime}")
    Long countEventsByTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据事件类型统计数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event WHERE event_type = #{eventType} " +
            "AND event_time BETWEEN #{startTime} AND #{endTime}")
    Long countEventsByTypeAndTimeRange(
            @Param("eventType") String eventType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查询未读的围栏事件
     */
    @Select("SELECT * FROM geo_fence_event WHERE is_read = 0 OR is_read IS NULL ORDER BY event_time DESC LIMIT #{limit}")
    List<GeoFenceEvent> getUnreadEvents(@Param("limit") Integer limit);
    
    /**
     * 标记事件为已读
     */
    @Update("UPDATE geo_fence_event SET is_read = 1, read_time = NOW() WHERE id = #{eventId}")
    int markEventAsRead(@Param("eventId") Long eventId);
    
    /**
     * 分页查询所有围栏事件
     */
    @Select("SELECT * FROM geo_fence_event ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<GeoFenceEvent> getAllEventsWithPagination(
            @Param("offset") Integer offset,
            @Param("size") Integer size);
    
    /**
     * 根据老人ID分页查询围栏事件
     */
    @Select("SELECT * FROM geo_fence_event WHERE elderly_id = #{elderlyId} ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<GeoFenceEvent> getEventsByElderlyIdWithPagination(
            @Param("elderlyId") Integer elderlyId,
            @Param("offset") Integer offset,
            @Param("size") Integer size);
    
    /**
     * 根据事件类型分页查询围栏事件
     */
    @Select("SELECT * FROM geo_fence_event WHERE event_type = #{eventType} ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<GeoFenceEvent> getEventsByTypeWithPagination(
            @Param("eventType") String eventType,
            @Param("offset") Integer offset,
            @Param("size") Integer size);
    
    /**
     * 根据时间范围分页查询围栏事件
     */
    @Select("SELECT * FROM geo_fence_event WHERE event_time >= #{startTime} AND event_time <= #{endTime} ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<GeoFenceEvent> getEventsByTimeRangeWithPagination(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") Integer offset,
            @Param("size") Integer size);
    
    /**
     * 统计所有围栏事件数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event")
    Long countAllEvents();
    
    /**
     * 根据老人ID统计围栏事件数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event WHERE elderly_id = #{elderlyId}")
    Long countEventsByElderlyId(@Param("elderlyId") Integer elderlyId);
    
    /**
     * 根据事件类型统计围栏事件数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event WHERE event_type = #{eventType}")
    Long countEventsByType(@Param("eventType") String eventType);
    
    /**
     * 根据时间范围统计围栏事件数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event WHERE event_time >= #{startTime} AND event_time <= #{endTime}")
    Long countEventsByTimeRange2(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据老人ID删除所有围栏事件记录
     */
    @Delete("DELETE FROM geo_fence_event WHERE elderly_id = #{elderlyId}")
    int deleteEventsByElderlyId(@Param("elderlyId") int elderlyId);
    
    /**
     * 根据多个条件分页查询围栏事件
     */
    @Select("SELECT * FROM geo_fence_event WHERE elderly_id = #{elderlyId} AND event_type = #{eventType} " +
            "AND event_time >= #{startTime} AND event_time <= #{endTime} " +
            "ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<GeoFenceEvent> getEventsByMultipleConditionsWithPagination(
            @Param("elderlyId") Integer elderlyId,
            @Param("eventType") String eventType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") Integer offset,
            @Param("size") Integer size);
    
    /**
     * 根据多个条件统计围栏事件数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event WHERE elderly_id = #{elderlyId} AND event_type = #{eventType} " +
            "AND event_time >= #{startTime} AND event_time <= #{endTime}")
    Long countEventsByMultipleConditions(
            @Param("elderlyId") Integer elderlyId,
            @Param("eventType") String eventType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据老人ID和时间范围分页查询围栏事件
     */
    @Select("SELECT * FROM geo_fence_event WHERE elderly_id = #{elderlyId} " +
            "AND event_time >= #{startTime} AND event_time <= #{endTime} " +
            "ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<GeoFenceEvent> getEventsByElderlyIdAndTimeRangeWithPagination(
            @Param("elderlyId") Integer elderlyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") Integer offset,
            @Param("size") Integer size);
    
    /**
     * 根据老人ID和时间范围统计围栏事件数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event WHERE elderly_id = #{elderlyId} " +
            "AND event_time >= #{startTime} AND event_time <= #{endTime}")
    Long countEventsByElderlyIdAndTimeRange(
            @Param("elderlyId") Integer elderlyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据老人ID和事件类型分页查询围栏事件
     */
    @Select("SELECT * FROM geo_fence_event WHERE elderly_id = #{elderlyId} AND event_type = #{eventType} " +
            "ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<GeoFenceEvent> getEventsByElderlyIdAndTypeWithPagination(
            @Param("elderlyId") Integer elderlyId,
            @Param("eventType") String eventType,
            @Param("offset") Integer offset,
            @Param("size") Integer size);
    
    /**
     * 根据老人ID和事件类型统计围栏事件数量
     */
    @Select("SELECT COUNT(*) FROM geo_fence_event WHERE elderly_id = #{elderlyId} AND event_type = #{eventType}")
    Long countEventsByElderlyIdAndType(
            @Param("elderlyId") Integer elderlyId,
            @Param("eventType") String eventType);
    
    /**
     * 根据事件类型和时间范围分页查询围栏事件
     */
    @Select("SELECT * FROM geo_fence_event WHERE event_type = #{eventType} " +
            "AND event_time >= #{startTime} AND event_time <= #{endTime} " +
            "ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<GeoFenceEvent> getEventsByTypeAndTimeRangeWithPagination(
            @Param("eventType") String eventType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") Integer offset,
            @Param("size") Integer size);
}