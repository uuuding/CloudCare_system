package com.cloudcare.service;

import com.cloudcare.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GCNService {

    @Autowired
    private KnowledgeGraphService knowledgeGraphService;

    // GCN节点类
    static class GCNNode {
        String id;
        String type; // Medicine, Disease, Symptom
        Object entity; // 原始实体对象
        double[] features;
        List<GCNNode> neighbors = new ArrayList<>();

        public GCNNode(String id, String type, Object entity, double[] features) {
            this.id = id;
            this.type = type;
            this.entity = entity;
            this.features = features;
        }

        public void addNeighbor(GCNNode node) {
            if (!neighbors.contains(node)) {
                neighbors.add(node);
            }
        }
    }

    // 特征传播结果类
    public static class PropagationResult {
        private Map<String, double[]> nodeFeatures;
        private Map<String, String> nodeTypes;
        private Map<String, String> nodeNames;

        public PropagationResult() {
            this.nodeFeatures = new HashMap<>();
            this.nodeTypes = new HashMap<>();
            this.nodeNames = new HashMap<>();
        }

        // Getters and Setters
        public Map<String, double[]> getNodeFeatures() { return nodeFeatures; }
        public void setNodeFeatures(Map<String, double[]> nodeFeatures) { this.nodeFeatures = nodeFeatures; }
        public Map<String, String> getNodeTypes() { return nodeTypes; }
        public void setNodeTypes(Map<String, String> nodeTypes) { this.nodeTypes = nodeTypes; }
        public Map<String, String> getNodeNames() { return nodeNames; }
        public void setNodeNames(Map<String, String> nodeNames) { this.nodeNames = nodeNames; }
    }

    /**
     * 构建知识图谱的GCN表示
     */
    public Map<String, GCNNode> buildGCNGraph() {
        Map<String, GCNNode> gcnGraph = new HashMap<>();

        // 获取所有实体
        List<Medicine> medicines = knowledgeGraphService.getAllMedicines();
        List<Disease> diseases = knowledgeGraphService.getAllDiseases();
        List<Symptom> symptoms = knowledgeGraphService.getAllSymptoms();

        // 创建药物节点
        for (Medicine medicine : medicines) {
            String nodeId = "medicine_" + medicine.getId();
            double[] features = NodeFeatureEncoder.encodeEntity(medicine);
            GCNNode node = new GCNNode(nodeId, "Medicine", medicine, features);
            gcnGraph.put(nodeId, node);
        }

        // 创建疾病节点
        for (Disease disease : diseases) {
            String nodeId = "disease_" + disease.getId();
            double[] features = NodeFeatureEncoder.encodeEntity(disease);
            GCNNode node = new GCNNode(nodeId, "Disease", disease, features);
            gcnGraph.put(nodeId, node);
        }

        // 创建症状节点
        for (Symptom symptom : symptoms) {
            String nodeId = "symptom_" + symptom.getId();
            double[] features = NodeFeatureEncoder.encodeEntity(symptom);
            GCNNode node = new GCNNode(nodeId, "Symptom", symptom, features);
            gcnGraph.put(nodeId, node);
        }

        // 建立关系连接
        buildRelationships(gcnGraph, medicines, diseases);

        return gcnGraph;
    }

    /**
     * 建立节点之间的关系
     */
    private void buildRelationships(Map<String, GCNNode> gcnGraph, List<Medicine> medicines, List<Disease> diseases) {
        // 药物-疾病关系
        for (Medicine medicine : medicines) {
            String medicineNodeId = "medicine_" + medicine.getId();
            GCNNode medicineNode = gcnGraph.get(medicineNodeId);
            
            if (medicine.getTreatedDiseases() != null) {
                for (Disease disease : medicine.getTreatedDiseases()) {
                    String diseaseNodeId = "disease_" + disease.getId();
                    GCNNode diseaseNode = gcnGraph.get(diseaseNodeId);
                    if (diseaseNode != null) {
                        medicineNode.addNeighbor(diseaseNode);
                        diseaseNode.addNeighbor(medicineNode);
                    }
                }
            }
        }

        // 疾病-症状关系
        for (Disease disease : diseases) {
            String diseaseNodeId = "disease_" + disease.getId();
            GCNNode diseaseNode = gcnGraph.get(diseaseNodeId);
            
            if (disease.getSymptoms() != null) {
                for (Symptom symptom : disease.getSymptoms()) {
                    String symptomNodeId = "symptom_" + symptom.getId();
                    GCNNode symptomNode = gcnGraph.get(symptomNodeId);
                    if (symptomNode != null) {
                        diseaseNode.addNeighbor(symptomNode);
                        symptomNode.addNeighbor(diseaseNode);
                    }
                }
            }
        }
    }

    /**
     * 执行特征传播
     */
    public PropagationResult performPropagation(double alpha, int iterations) {
        Map<String, GCNNode> gcnGraph = buildGCNGraph();
        
        // 执行多轮传播
        for (int i = 0; i < iterations; i++) {
            propagateFeatures(gcnGraph, alpha);
        }

        // 构建结果
        PropagationResult result = new PropagationResult();
        Map<String, double[]> nodeFeatures = new HashMap<>();
        Map<String, String> nodeTypes = new HashMap<>();
        Map<String, String> nodeNames = new HashMap<>();

        for (Map.Entry<String, GCNNode> entry : gcnGraph.entrySet()) {
            String nodeId = entry.getKey();
            GCNNode node = entry.getValue();
            
            nodeFeatures.put(nodeId, node.features);
            nodeTypes.put(nodeId, node.type);
            
            // 获取节点名称
            String nodeName = "";
            if (node.entity instanceof Medicine) {
                nodeName = ((Medicine) node.entity).getName();
            } else if (node.entity instanceof Disease) {
                nodeName = ((Disease) node.entity).getName();
            } else if (node.entity instanceof Symptom) {
                nodeName = ((Symptom) node.entity).getName();
            }
            nodeNames.put(nodeId, nodeName);
        }

        result.setNodeFeatures(nodeFeatures);
        result.setNodeTypes(nodeTypes);
        result.setNodeNames(nodeNames);

        return result;
    }

    /**
     * 单轮特征传播
     */
    private void propagateFeatures(Map<String, GCNNode> graph, double alpha) {
        Map<String, double[]> updated = new HashMap<>();

        for (GCNNode node : graph.values()) {
            if (node.neighbors.isEmpty()) {
                updated.put(node.id, node.features.clone());
                continue;
            }

            List<double[]> neighborFeatures = new ArrayList<>();
            for (GCNNode neighbor : node.neighbors) {
                neighborFeatures.add(neighbor.features);
            }

            double[] mean = average(neighborFeatures);
            double[] newFeat = new double[node.features.length];

            for (int i = 0; i < newFeat.length; i++) {
                newFeat[i] = alpha * node.features[i] + (1 - alpha) * mean[i];
            }

            updated.put(node.id, newFeat);
        }

        // 更新所有节点的特征
        for (Map.Entry<String, double[]> entry : updated.entrySet()) {
            graph.get(entry.getKey()).features = entry.getValue();
        }
    }

    /**
     * 计算特征向量的平均值
     */
    private double[] average(List<double[]> vectors) {
        if (vectors.isEmpty()) {
            return new double[0];
        }
        
        int dim = vectors.get(0).length;
        double[] avg = new double[dim];
        
        for (double[] v : vectors) {
            for (int i = 0; i < dim; i++) {
                avg[i] += v[i];
            }
        }
        
        for (int i = 0; i < dim; i++) {
            avg[i] /= vectors.size();
        }
        
        return avg;
    }

    /**
     * 获取节点相似度（基于特征向量余弦相似度）
     */
    public double calculateSimilarity(double[] features1, double[] features2) {
        if (features1.length != features2.length) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < features1.length; i++) {
            dotProduct += features1[i] * features2[i];
            norm1 += features1[i] * features1[i];
            norm2 += features2[i] * features2[i];
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // 药物推荐结果类
    public static class MedicineRecommendation {
        private String medicineId;
        private String medicineName;
        private double similarity;
        private Medicine medicine;

        public MedicineRecommendation(String medicineId, String medicineName, double similarity, Medicine medicine) {
            this.medicineId = medicineId;
            this.medicineName = medicineName;
            this.similarity = similarity;
            this.medicine = medicine;
        }

        // Getters and Setters
        public String getMedicineId() { return medicineId; }
        public void setMedicineId(String medicineId) { this.medicineId = medicineId; }
        public String getMedicineName() { return medicineName; }
        public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
        public double getSimilarity() { return similarity; }
        public void setSimilarity(double similarity) { this.similarity = similarity; }
        public Medicine getMedicine() { return medicine; }
        public void setMedicine(Medicine medicine) { this.medicine = medicine; }
    }

    /**
     * 根据症状节点推荐相关药物
     * @param symptomId 症状ID
     * @param topK 返回前K个最相关的药物
     * @param alpha 特征传播参数
     * @param iterations 传播迭代次数
     * @return 推荐的药物列表，按相似度降序排列
     */
    public List<MedicineRecommendation> recommendMedicinesBySymptom(Long symptomId, int topK, double alpha, int iterations) {
        // 构建症状节点ID
        String symptomNodeId = "symptom_" + symptomId;
        
        // 执行特征传播
        PropagationResult result = performPropagation(alpha, iterations);
        
        // 获取症状节点的特征
        double[] symptomFeatures = result.getNodeFeatures().get(symptomNodeId);
        if (symptomFeatures == null) {
            throw new RuntimeException("症状节点不存在: " + symptomNodeId);
        }
        
        // 计算与所有药物节点的相似度
        List<MedicineRecommendation> recommendations = new ArrayList<>();
        
        for (Map.Entry<String, double[]> entry : result.getNodeFeatures().entrySet()) {
            String nodeId = entry.getKey();
            String nodeType = result.getNodeTypes().get(nodeId);
            
            // 只处理药物节点
            if ("Medicine".equals(nodeType)) {
                double[] medicineFeatures = entry.getValue();
                double similarity = calculateSimilarity(symptomFeatures, medicineFeatures);
                
                // 获取药物实体信息
                String medicineIdStr = nodeId.replace("medicine_", "");
                Long medicineId = Long.parseLong(medicineIdStr);
                Medicine medicine = knowledgeGraphService.findMedicineById(medicineId);
                
                if (medicine != null) {
                    recommendations.add(new MedicineRecommendation(
                        nodeId, 
                        medicine.getName(), 
                        similarity, 
                        medicine
                    ));
                }
            }
        }
        
        // 按相似度降序排序，取前K个
        return recommendations.stream()
                .sorted((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()))
                .limit(topK)
                .collect(Collectors.toList());
    }
}