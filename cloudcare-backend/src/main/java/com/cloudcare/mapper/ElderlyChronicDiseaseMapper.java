package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.ElderlyChronicDisease;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface ElderlyChronicDiseaseMapper extends BaseMapper<ElderlyChronicDisease> {

    // 插入慢性病记录
    @Insert("INSERT INTO elderly_chronic_disease(elderly_id, disease_name, disease_category, diagnosis_date) " +
            "VALUES(#{elderlyId}, #{diseaseName}, #{diseaseCategory}, #{diagnosisDate})")
    int insertChronicDisease(ElderlyChronicDisease disease);

    // 查询所有慢性病记录
    @Select("SELECT * FROM elderly_chronic_disease WHERE elderly_id = #{elderlyId}")
    List<ElderlyChronicDisease> selectDiseasesByElderlyId(int elderlyId);

    // 根据慢病ID查询
    @Select("SELECT * FROM elderly_chronic_disease WHERE id = #{id}")
    ElderlyChronicDisease selectDiseaseById(int id);

    // 更新慢性病记录
    @Update("UPDATE elderly_chronic_disease SET disease_name = #{diseaseName}, disease_category = #{diseaseCategory}, " +
            "diagnosis_date = #{diagnosisDate} WHERE id = #{id}")
    int updateDisease(ElderlyChronicDisease disease);

    // 删除慢性病记录
    @Delete("DELETE FROM elderly_chronic_disease WHERE id = #{id}")
    int deleteDisease(int id);
}
