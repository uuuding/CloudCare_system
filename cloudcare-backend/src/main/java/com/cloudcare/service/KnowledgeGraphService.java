package com.cloudcare.service;

import com.cloudcare.entity.Disease;
import com.cloudcare.entity.Medicine;
import com.cloudcare.entity.Symptom;

import java.util.List;

public interface KnowledgeGraphService {

    Disease findDiseaseByName(String name);

    List<Disease> findDiseasesBySymptom(String symptomName);

    Medicine findMedicineByName(String name);

    List<Medicine> findMedicinesByDisease(String diseaseName);

    void addMedicine(Medicine medicine);

    void addMedicineToDisease(String medicineName, String diseaseName);

    void addDisease(Disease disease);

    void addSymptom(Symptom symptom);

    void addSymptomToDisease(String symptomName, String diseaseName);

    // 删除操作
    void deleteMedicine(String name);
    
    void deleteDisease(String name);
    
    void deleteSymptom(String name);
    
    void removeMedicineFromDisease(String medicineName, String diseaseName);
    
    void removeSymptomFromDisease(String symptomName, String diseaseName);
    
    // 更新操作
    void updateMedicine(String originalName, Medicine medicine);
    
    void updateDisease(String originalName, Disease disease);
    
    void updateSymptom(String originalName, Symptom symptom);
    
    // 获取所有节点
    List<Disease> getAllDiseases();
    
    List<Medicine> getAllMedicines();
    
    List<Symptom> getAllSymptoms();

    java.util.Map<String, Object> getFullGraph();
}