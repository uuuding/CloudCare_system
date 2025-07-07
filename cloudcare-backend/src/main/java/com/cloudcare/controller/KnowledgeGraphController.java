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
    public void addMedicine(@RequestBody Medicine medicine) {
        knowledgeGraphService.addMedicine(medicine);
    }

    @PostMapping("/medicine/{medicineName}/disease/{diseaseName}")
    public void addMedicineToDisease(@PathVariable String medicineName, @PathVariable String diseaseName) {
        knowledgeGraphService.addMedicineToDisease(medicineName, diseaseName);
    }

    @PostMapping("/disease")
    public void addDisease(@RequestBody Disease disease) {
        knowledgeGraphService.addDisease(disease);
    }

    @PostMapping("/symptom")
    public void addSymptom(@RequestBody Symptom symptom) {
        knowledgeGraphService.addSymptom(symptom);
    }

    @PostMapping("/symptom/{symptomName}/disease/{diseaseName}")
    public void addSymptomToDisease(@PathVariable String symptomName, @PathVariable String diseaseName) {
        knowledgeGraphService.addSymptomToDisease(symptomName, diseaseName);
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
}
