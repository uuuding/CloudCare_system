package com.cloudcare.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloudcare.entity.ElderlyProfile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ElderlyProfileMapper extends BaseMapper<ElderlyProfile> {

    // 插入记录
    @Insert("INSERT INTO elderly_profile(name, age, gender, create_time, update_time) " +
            "VALUES(#{name}, #{age}, #{gender}, #{createTime}, #{updateTime})")
    int insertProfile(ElderlyProfile elderlyProfile);

    // 查询所有老人信息
    @Select("SELECT * FROM elderly_profile")
    List<ElderlyProfile> selectAllProfiles();

    // 根据 ID 查询
    @Select("SELECT * FROM elderly_profile WHERE id = #{id}")
    ElderlyProfile selectById(int id);

    // 通过 name 和 age 联合查询
    @Select("SELECT * FROM elderly_profile WHERE name = #{name} AND age = #{age}")
    List<ElderlyProfile> searchByNameAndAge(@Param("name") String name, @Param("age") int age);

    // 更新档案（假设只更新 name、age、gender）
    @Update("UPDATE elderly_profile SET name = #{name}, age = #{age}, gender = #{gender}, update_time = NOW() WHERE id = #{id}")
    boolean updateProfile(ElderlyProfile profile);

    // 删除档案
    @Delete("DELETE FROM elderly_profile WHERE id = #{id}")
    boolean deleteProfile(@Param("id") int id);


}
