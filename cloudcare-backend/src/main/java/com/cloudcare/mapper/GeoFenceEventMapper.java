package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.GeoFenceEvent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 电子围栏事件记录Mapper接口
 */
@Mapper
public interface GeoFenceEventMapper extends BaseMapper<GeoFenceEvent> {

    /**
     * 根据老人ID查询围栏事件记录
     */
    @Select("SELECT * FROM geo_fence_event WHERE elderly_id = #{elderlyId} ORDER BY event_time DESC")
    List<GeoFenceEvent> getEventsByElderlyId(@Param("elderlyId") Integer elderlyId);

    /**
     * 根据老人ID和时间范围查询围栏事件记录
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
    @Select("UPDATE geo_fence_event SET alert_sent = 1, alert_sent_time = NOW() WHERE id = #{eventId}")
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
    @Select("UPDATE geo_fence_event SET is_read = 1, read_time = NOW() WHERE id = #{eventId}")
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
}