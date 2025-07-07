package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.entity.Disease;
import com.cloudcare.entity.Medicine;
import com.cloudcare.entity.Symptom;
import com.cloudcare.service.KnowledgeGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/knowledge-graph")
@CrossOrigin(origins = "*")
public class KnowledgeGraphController {

    @Autowired
    private KnowledgeGraphService knowledgeGraphService;

    @GetMapping("/disease/{name}")
    public Disease getDiseaseByName(@PathVariable String name) {
        return knowledgeGraphService.findDiseaseByName(name);
    }

    @GetMapping("/diseases/by-symptom/{symptomName}")
    public List<Disease> getDiseasesBySymptom(@PathVariable String symptomName) {
        return knowledgeGraphService.findDiseasesBySymptom(symptomName);
    }

    @GetMapping("/medicine/{name}")
    public Medicine getMedicineByName(@PathVariable String name) {
        return knowledgeGraphService.findMedicineByName(name);
    }

    @GetMapping("/medicines/by-disease/{diseaseName}")
    public List<Medicine> getMedicinesByDisease(@PathVariable String diseaseName) {
        return knowledgeGraphService.findMedicinesByDisease(diseaseName);
    }

    @PostMapping("/medicine")
    public Result<String> addMedicine(@RequestBody Medicine medicine) {
        try {
            knowledgeGraphService.addMedicine(medicine);
            return Result.success("药物添加成功");
        } catch (Exception e) {
            return Result.error("添加药物失败: " + e.getMessage());
        }
    }

    @PostMapping("/medicine/{medicineName}/disease/{diseaseName}")
    public Result<String> addMedicineToDisease(@PathVariable String medicineName, @PathVariable String diseaseName) {
        try {
            knowledgeGraphService.addMedicineToDisease(medicineName, diseaseName);
            return Result.success("药物与疾病关系添加成功");
        } catch (Exception e) {
            return Result.error("添加药物与疾病关系失败: " + e.getMessage());
        }
    }

    @PostMapping("/disease")
    public Result<String> addDisease(@RequestBody Disease disease) {
        try {
            knowledgeGraphService.addDisease(disease);
            return Result.success("疾病添加成功");
        } catch (Exception e) {
            return Result.error("添加疾病失败: " + e.getMessage());
        }
    }

    @PostMapping("/symptom")
    public Result<String> addSymptom(@RequestBody Symptom symptom) {
        try {
            knowledgeGraphService.addSymptom(symptom);
            return Result.success("症状添加成功");
        } catch (Exception e) {
            return Result.error("添加症状失败: " + e.getMessage());
        }
    }

    @PostMapping("/symptom/{symptomName}/disease/{diseaseName}")
    public Result<String> addSymptomToDisease(@PathVariable String symptomName, @PathVariable String diseaseName) {
        try {
            knowledgeGraphService.addSymptomToDisease(symptomName, diseaseName);
            return Result.success("症状与疾病关系添加成功");
        } catch (Exception e) {
            return Result.error("添加症状与疾病关系失败: " + e.getMessage());
        }
    }

    @GetMapping("/full-graph")
    public Result<Map<String, Object>> getFullGraph() {
        try {
            Map<String, Object> graphData = knowledgeGraphService.getFullGraph();
            return Result.success(graphData, "知识图谱数据获取成功");
        } catch (Exception e) {
            return Result.error("获取知识图谱数据失败: " + e.getMessage());
        }
    }

    // 删除操作接口
    @DeleteMapping("/medicine/{name}")
    public Result<String> deleteMedicine(@PathVariable String name) {
        try {
            knowledgeGraphService.deleteMedicine(name);
            return Result.success("药物删除成功");
        } catch (Exception e) {
            return Result.error("删除药物失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/disease/{name}")
    public Result<String> deleteDisease(@PathVariable String name) {
        try {
            knowledgeGraphService.deleteDisease(name);
            return Result.success("疾病删除成功");
        } catch (Exception e) {
            return Result.error("删除疾病失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/symptom/{name}")
    public Result<String> deleteSymptom(@PathVariable String name) {
        try {
            knowledgeGraphService.deleteSymptom(name);
            return Result.success("症状删除成功");
        } catch (Exception e) {
            return Result.error("删除症状失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/medicine/{medicineName}/disease/{diseaseName}")
    public Result<String> removeMedicineFromDisease(@PathVariable String medicineName, @PathVariable String diseaseName) {
        try {
            knowledgeGraphService.removeMedicineFromDisease(medicineName, diseaseName);
            return Result.success("药物与疾病关系删除成功");
        } catch (Exception e) {
            return Result.error("删除药物与疾病关系失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/symptom/{symptomName}/disease/{diseaseName}")
    public Result<String> removeSymptomFromDisease(@PathVariable String symptomName, @PathVariable String diseaseName) {
        try {
            knowledgeGraphService.removeSymptomFromDisease(symptomName, diseaseName);
            return Result.success("症状与疾病关系删除成功");
        } catch (Exception e) {
            return Result.error("删除症状与疾病关系失败: " + e.getMessage());
        }
    }

    // 更新操作接口
    @PutMapping("/medicine/{name}")
    public Result<String> updateMedicine(@PathVariable String name, @RequestBody Medicine medicine) {
        try {
            knowledgeGraphService.updateMedicine(name, medicine);
            return Result.success("药物更新成功");
        } catch (Exception e) {
            return Result.error("更新药物失败: " + e.getMessage());
        }
    }

    @PutMapping("/disease/{name}")
    public Result<String> updateDisease(@PathVariable String name, @RequestBody Disease disease) {
        try {
            knowledgeGraphService.updateDisease(name, disease);
            return Result.success("疾病更新成功");
        } catch (Exception e) {
            return Result.error("更新疾病失败: " + e.getMessage());
        }
    }

    @PutMapping("/symptom/{name}")
    public Result<String> updateSymptom(@PathVariable String name, @RequestBody Symptom symptom) {
        try {
            knowledgeGraphService.updateSymptom(name, symptom);
            return Result.success("症状更新成功");
        } catch (Exception e) {
            return Result.error("更新症状失败: " + e.getMessage());
        }
    }

    // 获取所有节点接口
    @GetMapping("/diseases")
    public Result<List<Disease>> getAllDiseases() {
        try {
            List<Disease> diseases = knowledgeGraphService.getAllDiseases();
            return Result.success(diseases, "获取所有疾病成功");
        } catch (Exception e) {
            return Result.error("获取疾病列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/medicines")
    public Result<List<Medicine>> getAllMedicines() {
        try {
            List<Medicine> medicines = knowledgeGraphService.getAllMedicines();
            return Result.success(medicines, "获取所有药物成功");
        } catch (Exception e) {
            return Result.error("获取药物列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/symptoms")
    public Result<List<Symptom>> getAllSymptoms() {
        try {
            List<Symptom> symptoms = knowledgeGraphService.getAllSymptoms();
            return Result.success(symptoms, "获取所有症状成功");
        } catch (Exception e) {
            return Result.error("获取症状列表失败: " + e.getMessage());
        }
    }
}
