package com.cloudcare.dto;

import com.cloudcare.entity.ElderlyChronicDisease;
import lombok.Data;

/**
 * 批量新增老人档案DTO
 * 包含基本信息和可选的病例信息
 */
@Data
public class BatchElderlyProfileDTO {
    
    // 基本信息
    private String name;
    private Integer age;
    private String gender;
    
    // 可选的病例信息（支持多条）
    private java.util.List<ChronicDiseaseInfo> chronicDiseases;
    
    @Data
    public static class ChronicDiseaseInfo {
        private String diseaseName;
        private String diseaseCategory;
        private String diagnosisDate;
    }
}