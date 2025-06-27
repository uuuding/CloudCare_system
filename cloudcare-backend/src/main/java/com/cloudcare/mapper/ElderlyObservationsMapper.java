package com.cloudcare.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.ElderlyObservations;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface ElderlyObservationsMapper extends BaseMapper<ElderlyObservations> {

    // 插入体检记录
    @Insert("INSERT INTO elderly_observations(elderly_id, observation_time, body_temperature, systolic_bp, " +
            "cough, heart_rate, sleep_hours) VALUES(#{elderlyId}, #{observationTime}, #{bodyTemperature}, " +
            "#{systolicBp}, #{cough}, #{heartRate}, #{sleepHours})")
    int insertObservation(ElderlyObservations observation);

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
}