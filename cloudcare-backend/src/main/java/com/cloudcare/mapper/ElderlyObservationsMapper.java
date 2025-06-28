package com.cloudcare.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.ElderlyObservations;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ElderlyObservationsMapper extends BaseMapper<ElderlyObservations> {

    // 查询所有体检记录
    @Select("SELECT * FROM elderly_observations WHERE elderly_id = #{elderlyId}")
    List<ElderlyObservations> selectObservationsByElderlyId(int elderlyId);

    // 根据体检ID查询
    @Select("SELECT * FROM elderly_observations WHERE id = #{id}")
    ElderlyObservations selectObservationById(int id);

    // 更新体检记录
    @Update("UPDATE elderly_observations SET observation_time = #{observationTime}, " +
            "body_temperature = #{bodyTemperature}, systolic_bp = #{systolicBp}, cough = #{cough}, " +
            "heart_rate = #{heartRate}, sleep_hours = #{sleepHours} WHERE id = #{id}")
    int updateObservation(ElderlyObservations observation);

    // 删除体检记录
    @Delete("DELETE FROM elderly_observations WHERE id = #{id}")
    int deleteObservation(int id);

    // 查询所有体检记录
    @Select("SELECT * FROM elderly_observations")
    List<ElderlyObservations> selectAllObservations();

    // 根据老人ID查询所有体检记录，按时间倒序
    @Select("SELECT * FROM elderly_observations WHERE elderly_id = #{elderlyId} ORDER BY observation_time DESC")
    List<ElderlyObservations> selectObservationsByElderlyIdOrderByTimeDesc(int elderlyId);

    // 根据时间范围查询老人体检记录
    @Select("SELECT * FROM elderly_observations WHERE elderly_id = #{elderlyId} AND observation_time BETWEEN #{startTime} AND #{endTime} ORDER BY observation_time DESC")
    List<ElderlyObservations> selectObservationsByElderlyIdAndTimeRange(@Param("elderlyId") int elderlyId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    // 插入体检记录，包含观察地点
    @Insert("INSERT INTO elderly_observations(elderly_id, observation_time, body_temperature, systolic_bp, cough, heart_rate, sleep_hours, observation_location, height, weight) VALUES(#{elderlyId}, #{observationTime}, #{bodyTemperature}, #{systolicBp}, #{cough}, #{heartRate}, #{sleep_hours}, #{observationLocation}, #{height}, #{weight})")
    int insertObservationWithLocation(ElderlyObservations observation);

    // 更新体检记录，包含观察地点
    @Update("UPDATE elderly_observations SET observation_time = #{observationTime}, body_temperature = #{bodyTemperature}, systolic_bp = #{systolicBp}, cough = #{cough}, heart_rate = #{heartRate}, sleep_hours = #{sleepHours}, observation_location = #{observationLocation}, height = #{height}, weight = #{weight} WHERE id = #{id}")
    int updateObservationWithLocation(ElderlyObservations observation);
}