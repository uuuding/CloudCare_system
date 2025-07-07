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

    java.util.Map<String, Object> getFullGraph();
}