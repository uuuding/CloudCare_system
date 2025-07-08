package com.cloudcare.entity;

import java.util.*;

public class NodeFeatureEncoder {

    // ===== 1. 类型 one-hot 编码 =====
    public static double[] encodeType(String label) {
        switch (label) {
            case "症状":
            case "Symptom": return new double[]{1, 0, 0};
            case "疾病":
            case "Disease": return new double[]{0, 1, 0};
            case "药物":
            case "Medicine": return new double[]{0, 0, 1};
            default:     return new double[]{0, 0, 0};
        }
    }
    
    // ===== 实体类型映射 =====
    public static String mapEntityType(Object entity) {
        if (entity instanceof Medicine) {
            return "Medicine";
        } else if (entity instanceof Disease) {
            return "Disease";
        } else if (entity instanceof Symptom) {
            return "Symptom";
        }
        return "Unknown";
    }
    
    // ===== 获取实体描述文本 =====
    public static String getEntityDescription(Object entity) {
        if (entity instanceof Medicine) {
            Medicine medicine = (Medicine) entity;
            return medicine.getDescription() != null ? medicine.getDescription() : medicine.getName();
        } else if (entity instanceof Disease) {
            Disease disease = (Disease) entity;
            return disease.getDescription() != null ? disease.getDescription() : disease.getName();
        } else if (entity instanceof Symptom) {
            Symptom symptom = (Symptom) entity;
            return symptom.getName(); // 症状没有描述属性，使用名称
        }
        return "empty";
    }
    
    // ===== 为实体编码 =====
    public static double[] encodeEntity(Object entity) {
        String type = mapEntityType(entity);
        String description = getEntityDescription(entity);
        return encodeAll(type, description);
    }

    // ===== 2. 文本哈希编码（稀疏） =====
    public static double[] encodeTextHash(String text, int dim) {
        double[] vec = new double[dim];
        if (text != null && !text.isEmpty()) {
            int index = Math.abs(text.hashCode()) % dim;
            vec[index] = 1.0;
        }
        return vec;
    }

    // ===== 3. 关键词 one-hot 编码 =====
    private static final String[] keywordDict = {
            "发烧", "头痛", "咳嗽", "嗜睡", "恶心", "止咳", "炎症", "便秘", "慢性", "呼吸道","体循环","预防","抑制","血管"
    };

    public static double[] encodeKeywords(String text) {
        double[] vec = new double[keywordDict.length];
        if (text == null) return vec;
        for (int i = 0; i < keywordDict.length; i++) {
            if (text.contains(keywordDict[i])) {
                vec[i] = 1.0;
            }
        }
        return vec;
    }

    // ===== 4. 拼接所有向量 =====
    public static double[] encodeAll(String label, String description) {
        double[] typeVec = encodeType(label);
        double[] hashVec = encodeTextHash(description, 64);
        double[] keywordVec = encodeKeywords(description);
        return concat(typeVec, hashVec, keywordVec);
    }

    // ===== 工具：拼接数组 =====
    public static double[] concat(double[]... arrays) {
        int totalLength = Arrays.stream(arrays).mapToInt(a -> a.length).sum();
        double[] result = new double[totalLength];
        int pos = 0;
        for (double[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }
}
