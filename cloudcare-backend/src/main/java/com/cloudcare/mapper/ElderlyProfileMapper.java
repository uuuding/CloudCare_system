package com.cloudcare.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloudcare.entity.ElderlyChronicDisease;
import com.cloudcare.entity.ElderlyProfile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ElderlyProfileMapper extends BaseMapper<ElderlyProfile> {

    // 插入记录
    @Insert("INSERT INTO elderly_profile(name, age, gender, create_time, update_time) " +
            "VALUES(#{name}, #{age}, #{gender}, NOW(), NOW())")
    int insertProfile(ElderlyProfile elderlyProfile);


    // 查询所有老人信息
    @Select("SELECT * FROM elderly_profile")
    List<ElderlyProfile> selectAllProfiles();

    // 根据 ID 查询
    @Select("SELECT * FROM elderly_profile WHERE id = #{id}")
    ElderlyProfile selectById(int id);

    // 通过 name 和 age 联合查询（支持单独或组合查询）
    @Select({"<script>",
        "SELECT * FROM elderly_profile WHERE 1=1",
        "<if test='name != null and name != \"\"'>",
        "AND name LIKE CONCAT('%', #{name}, '%')",
        "</if>",
        "<if test='age > 0'>",
        "AND age = #{age}",
        "</if>",
        "</script>"})
    List<ElderlyProfile> searchByNameAndAge(@Param("name") String name, @Param("age") int age);

    // 更新档案（包括既往病史）

    // 插入病例录入
    @Insert("INSERT INTO elderly_chronic_disease (elderly_id, disease_name, disease_category, diagnosis_date) VALUES (#{elderlyId}, #{diseaseName}, #{diseaseCategory}, #{diagnosisDate})")
    int insertCaseEntry(ElderlyChronicDisease chronicDisease);

    @Update("UPDATE elderly_profile SET name = #{name}, age = #{age}, gender = #{gender}, update_time = NOW() WHERE id = #{id}")
    boolean updateProfile(ElderlyProfile profile);

    // 删除老人既往病史
    @Delete("DELETE FROM elderly_chronic_disease WHERE elderly_id = #{elderlyId}")
    void deleteChronicDiseasesByElderlyId(Integer elderlyId);

    // 批量插入既往病史
    @Insert({"<script>",
        "INSERT INTO elderly_chronic_disease(elderly_id, disease_name, disease_category, diagnosis_date) VALUES ",
        "<foreach collection='chronicDiseases' item='item' separator=','>",
        "(#{elderlyId}, #{item.diseaseName}, #{item.diseaseCategory}, #{item.diagnosisDate})",
        "</foreach>",
        "</script>"})
    void insertChronicDiseases(@Param("elderlyId") Integer elderlyId, @Param("chronicDiseases") List<ElderlyChronicDisease> chronicDiseases);

    // 删除档案
    @Delete("DELETE FROM elderly_profile WHERE id = #{id}")
    boolean deleteProfile(@Param("id") int id);

    // 查询老人既往病史
    @Select("SELECT * FROM elderly_chronic_disease WHERE elderly_id = #{elderlyId}")
    List<ElderlyChronicDisease> selectChronicDiseasesByElderlyId(@Param("elderlyId") Integer elderlyId);

}
