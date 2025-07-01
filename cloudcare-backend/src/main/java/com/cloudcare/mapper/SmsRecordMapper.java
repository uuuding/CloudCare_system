package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.SmsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 短信记录Mapper接口
 *
 * @author cloudcare
 */
@Mapper
public interface SmsRecordMapper extends BaseMapper<SmsRecord> {

    /**
     * 获取今日发送统计
     */
    @Select("SELECT " +
            "COUNT(*) as todaySent, " +
            "SUM(CASE WHEN status = 'success' THEN 1 ELSE 0 END) as todaySuccess, " +
            "SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END) as todayFailed " +
            "FROM sms_record " +
            "WHERE DATE(send_time) = CURDATE()")
    Map<String, Object> getTodayStats();

    /**
     * 获取指定时间范围内的统计
     */
    @Select("SELECT " +
            "COUNT(*) as totalSent, " +
            "SUM(CASE WHEN status = 'success' THEN 1 ELSE 0 END) as totalSuccess, " +
            "SUM(CASE WHEN status = 'failed' THEN 1 ELSE 0 END) as totalFailed " +
            "FROM sms_record " +
            "WHERE send_time BETWEEN #{startTime} AND #{endTime}")
    Map<String, Object> getStatsInRange(@Param("startTime") LocalDateTime startTime, 
                                       @Param("endTime") LocalDateTime endTime);
}