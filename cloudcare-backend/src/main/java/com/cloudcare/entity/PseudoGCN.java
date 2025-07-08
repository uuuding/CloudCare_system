package com.cloudcare.entity;
import java.util.*;

public class PseudoGCN {

    static class Node {
        String id;
        String label; // 如 "症状"、"疾病"、"药物"
        double[] features;
        List<Node> neighbors = new ArrayList<>();

        public Node(String id, String label, double[] features) {
            this.id = id;
            this.label = label;
            this.features = features;
        }

        public void addNeighbor(Node node) {
            neighbors.add(node);
        }
    }

    public static double[] average(List<double[]> vectors) {
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

    public static void propagate(Map<String, Node> graph, double alpha) {
        Map<String, double[]> updated = new HashMap<>();

        for (Node node : graph.values()) {
            if (node.neighbors.isEmpty()) {
                updated.put(node.id, node.features);
                continue;
            }

            List<double[]> neighborFeatures = new ArrayList<>();
            for (Node neighbor : node.neighbors) {
                neighborFeatures.add(neighbor.features);
            }

            double[] mean = average(neighborFeatures);
            double[] newFeat = new double[node.features.length];

            for (int i = 0; i < newFeat.length; i++) {
                newFeat[i] = alpha * node.features[i] + (1 - alpha) * mean[i];
            }

            updated.put(node.id, newFeat);
        }

        for (Map.Entry<String, double[]> entry : updated.entrySet()) {
            graph.get(entry.getKey()).features = entry.getValue();
        }
    }

    public static void printFeatures(Map<String, Node> graph) {
        for (Node node : graph.values()) {
            System.out.printf("%s (%s): %s\n", node.id, node.label, Arrays.toString(node.features));
        }
    }

    public static void main(String[] args) {
        // 简化例子：症状 cough → 疾病 flu → 药物 ibuprofen
        String desc1 = "咳嗽，伴随喉咙发痒";
        Node cough = new Node("1", "症状", NodeFeatureEncoder.encodeAll("症状", desc1));

        String desc2 = "流感，可引起发烧和乏力";
        Node flu = new Node("2", "疾病", NodeFeatureEncoder.encodeAll("疾病", desc2));

        String desc3 = "布洛芬，用于缓解发烧和疼痛，副作用包括嗜睡";
        Node ibuprofen = new Node("3", "药物", NodeFeatureEncoder.encodeAll("药物", desc3));

        // 构建图结构（cough → flu → ibuprofen）
        cough.addNeighbor(flu);
        flu.addNeighbor(ibuprofen);

        // 添加到图中
        Map<String, Node> graph = new HashMap<>();
        graph.put(cough.id, cough);
        graph.put(flu.id, flu);
        graph.put(ibuprofen.id, ibuprofen);

        System.out.println("传播前:");
        printFeatures(graph);

        propagate(graph, 0.6);  // 一轮传播，alpha 控制原始特征占比

        System.out.println("\n传播后:");
        printFeatures(graph);
    }
}
