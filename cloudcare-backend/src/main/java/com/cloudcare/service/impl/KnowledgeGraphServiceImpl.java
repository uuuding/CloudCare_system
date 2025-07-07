package com.cloudcare.service.impl;

import com.cloudcare.entity.Disease;
import com.cloudcare.entity.Medicine;
import com.cloudcare.entity.Symptom;
import com.cloudcare.repository.DiseaseRepository;
import com.cloudcare.repository.MedicineRepository;
import com.cloudcare.repository.SymptomRepository;
import com.cloudcare.service.KnowledgeGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class KnowledgeGraphServiceImpl implements KnowledgeGraphService {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public Disease findDiseaseByName(String name) {
        return diseaseRepository.findByName(name);
    }

    @Override
    public List<Disease> findDiseasesBySymptom(String symptomName) {
        return diseaseRepository.findBySymptomName(symptomName);
    }

    @Override
    public Medicine findMedicineByName(String name) {
        return medicineRepository.findByName(name);
    }

    @Override
    public List<Medicine> findMedicinesByDisease(String diseaseName) {
        return medicineRepository.findByDiseaseName(diseaseName);
    }

    @Override
    @Transactional
    public void addMedicine(Medicine medicine) {
        medicineRepository.save(medicine);
    }

    @Override
    @Transactional
    public void addMedicineToDisease(String medicineName, String diseaseName) {
        Medicine medicine = medicineRepository.findByName(medicineName);
        Disease disease = diseaseRepository.findByName(diseaseName);
        if (medicine != null && disease != null) {
            medicine.getTreatedDiseases().add(disease);
            medicineRepository.save(medicine);
        }
    }

    @Override
    @Transactional
    public void addDisease(Disease disease) {
        diseaseRepository.save(disease);
    }

    @Override
    @Transactional
    public void addSymptom(Symptom symptom) {
        symptomRepository.save(symptom);
    }

    @Override
    @Transactional
    public void addSymptomToDisease(String symptomName, String diseaseName) {
        Symptom symptom = symptomRepository.findByName(symptomName);
        Disease disease = diseaseRepository.findByName(diseaseName);
        if (symptom != null && disease != null) {
            disease.getSymptoms().add(symptom);
            diseaseRepository.save(disease);
        }
    }

    @Override
    public Map<String, Object> getFullGraph() {
        Map<String, Object> graph = new HashMap<>();
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();

        // 获取所有疾病
        List<Disease> diseases = (List<Disease>) diseaseRepository.findAll();
        for (Disease disease : diseases) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", disease.getId());
            node.put("name", disease.getName());
            node.put("category", 0); // 疾病类别
            node.put("description", disease.getDescription());
            nodes.add(node);
        }

        // 获取所有症状
        List<Symptom> symptoms = (List<Symptom>) symptomRepository.findAll();
        for (Symptom symptom : symptoms) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", symptom.getId());
            node.put("name", symptom.getName());
            node.put("category", 1); // 症状类别
            nodes.add(node);
        }

        // 获取所有用药
        List<Medicine> medicines = (List<Medicine>) medicineRepository.findAll();
        for (Medicine medicine : medicines) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", medicine.getId());
            node.put("name", medicine.getName());
            node.put("category", 2); // 用药类别
            node.put("description", medicine.getDescription());
            node.put("dosage", medicine.getDosage());
            node.put("frequency", medicine.getFrequency());
            node.put("sideEffects", medicine.getSideEffects());
            nodes.add(node);
        }

        // 创建疾病-症状关系
        for (Disease disease : diseases) {
            for (Symptom symptom : disease.getSymptoms()) {
                Map<String, Object> link = new HashMap<>();
                link.put("source", disease.getId());
                link.put("target", symptom.getId());
                link.put("relationship", "有症状");
                links.add(link);
            }
        }

        // 创建用药-疾病关系
        for (Medicine medicine : medicines) {
            for (Disease disease : medicine.getTreatedDiseases()) {
                Map<String, Object> link = new HashMap<>();
                link.put("source", medicine.getId());
                link.put("target", disease.getId());
                link.put("relationship", "治疗");
                links.add(link);
            }
        }

        graph.put("nodes", nodes);
        graph.put("links", links);
        return graph;
    }

    // 删除操作实现
    @Override
    @Transactional
    public void deleteMedicine(String name) {
        Medicine medicine = medicineRepository.findByName(name);
        if (medicine != null) {
            medicineRepository.delete(medicine);
        }
    }

    @Override
    @Transactional
    public void deleteDisease(String name) {
        Disease disease = diseaseRepository.findByName(name);
        if (disease != null) {
            diseaseRepository.delete(disease);
        }
    }

    @Override
    @Transactional
    public void deleteSymptom(String name) {
        Symptom symptom = symptomRepository.findByName(name);
        if (symptom != null) {
            symptomRepository.delete(symptom);
        }
    }

    @Override
    @Transactional
    public void removeMedicineFromDisease(String medicineName, String diseaseName) {
        Medicine medicine = medicineRepository.findByName(medicineName);
        Disease disease = diseaseRepository.findByName(diseaseName);
        if (medicine != null && disease != null) {
            // 使用自定义查询直接删除Neo4j中的关系
            medicineRepository.removeTreatmentRelation(medicineName, diseaseName);
        }
    }

    @Override
    @Transactional
    public void removeSymptomFromDisease(String symptomName, String diseaseName) {
        Symptom symptom = symptomRepository.findByName(symptomName);
        Disease disease = diseaseRepository.findByName(diseaseName);
        if (symptom != null && disease != null) {
            // 使用自定义查询直接删除Neo4j中的关系
            diseaseRepository.removeSymptomRelation(diseaseName, symptomName);
        }
    }

    // 更新操作实现
    @Override
    @Transactional
    public void updateMedicine(String originalName, Medicine medicine) {
        Medicine existingMedicine = medicineRepository.findByName(originalName);
        if (existingMedicine != null) {
            existingMedicine.setName(medicine.getName());
            existingMedicine.setDescription(medicine.getDescription());
            existingMedicine.setDosage(medicine.getDosage());
            existingMedicine.setFrequency(medicine.getFrequency());
            existingMedicine.setSideEffects(medicine.getSideEffects());
            medicineRepository.save(existingMedicine);
        }
    }

    @Override
    @Transactional
    public void updateDisease(String originalName, Disease disease) {
        Disease existingDisease = diseaseRepository.findByName(originalName);
        if (existingDisease != null) {
            existingDisease.setName(disease.getName());
            existingDisease.setDescription(disease.getDescription());
            diseaseRepository.save(existingDisease);
        }
    }

    @Override
    @Transactional
    public void updateSymptom(String originalName, Symptom symptom) {
        Symptom existingSymptom = symptomRepository.findByName(originalName);
        if (existingSymptom != null) {
            existingSymptom.setName(symptom.getName());
            // 如果Symptom实体有其他字段，在这里添加更新逻辑
            symptomRepository.save(existingSymptom);
        }
    }

    // 获取所有节点实现
    @Override
    public List<Disease> getAllDiseases() {
        return (List<Disease>) diseaseRepository.findAll();
    }

    @Override
    public List<Medicine> getAllMedicines() {
        return (List<Medicine>) medicineRepository.findAll();
    }

    @Override
    public List<Symptom> getAllSymptoms() {
        return (List<Symptom>) symptomRepository.findAll();
    }
}