package com.cloudcare.entity;

import ai.djl.Model;
import ai.djl.engine.Engine;
import ai.djl.ndarray.*;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.*;
import ai.djl.nn.core.Linear;
import ai.djl.training.*;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.dataset.Batch;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GCN {

    // 读取邻接矩阵文件，格式：每行“src tgt”，无向图，自动加自环
    public static NDArray loadAdjacency(String path, NDManager manager, int numNodes) throws IOException {
        float[][] adj = new float[numNodes][numNodes];
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sp = line.trim().split("\\s+");
                if (sp.length >= 2) {
                    int src = Integer.parseInt(sp[0]);
                    int tgt = Integer.parseInt(sp[1]);
                    adj[src][tgt] = 1f;
                    adj[tgt][src] = 1f;
                }
            }
        }
        // 自环
        for (int i = 0; i < numNodes; i++) {
            adj[i][i] = 1f;
        }
        return manager.create(adj);
    }

    // 读取id2type.json，返回 Map<Integer,String>
    public static Map<Integer, String> loadId2Type(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> rawMap = mapper.readValue(Files.readAllBytes(Paths.get(path)), Map.class);
        return rawMap.entrySet().stream()
                .collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), Map.Entry::getValue));
    }

    // 读取node2id.json，返回 Map<String,Integer>
    public static Map<String, Integer> loadNode2Id(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Files.readAllBytes(Paths.get(path)), Map.class);
    }


    // 读取特征文件，每行为一个节点特征向量，元素用空格分割
    public static NDArray loadFeatures(String path, NDManager manager, int numNodes, int featureDim) throws IOException {
        float[][] features = new float[numNodes][featureDim];
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int idx = 0;
            while ((line = br.readLine()) != null && idx < numNodes) {
                String[] sp = line.trim().split("\\s+");
                for (int i = 0; i < sp.length && i < featureDim; i++) {
                    features[idx][i] = Float.parseFloat(sp[i]);
                }
                idx++;
            }
        }
        return manager.create(features);
    }

    // 读取标签文件，每行格式：节点ID label，标签是long型整数
    public static NDArray loadLabels(String path, NDManager manager, int numNodes) throws IOException {
        long[] labels = new long[numNodes];
        // 初始化默认标签-1 (无标签)
        for (int i = 0; i < numNodes; i++) {
            labels[i] = -1;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sp = line.trim().split("\\s+");
                if (sp.length >= 2) {
                    int nodeId = Integer.parseInt(sp[0]);
                    long label = Long.parseLong(sp[1]);
                    if (nodeId >= 0 && nodeId < numNodes) {
                        labels[nodeId] = label;
                    }
                }
            }
        }
        return manager.create(labels);
    }

    public static void main(String[] args) throws Exception {
        try (NDManager manager = NDManager.newBaseManager()) {
            System.out.println("Engine: " + Engine.getInstance().getEngineName());

            // 1. 加载映射关系
            Map<Integer, String> id2type = loadId2Type("id2type.txt");
            Map<String, Integer> node2id = loadNode2Id("node2id.txt");

            // 反向建立 id -> name 映射
            Map<Integer, String> id2node = new HashMap<>();
            for (var e : node2id.entrySet()) {
                id2node.put(e.getValue(), e.getKey());
            }

            int numNodes = id2type.size();

            // 2. 构建节点特征（调用你之前写的 NodeFeatureEncoder）
            int featureDim = 3 + 64 + 14; // 你的特征维度
            float[][] features = new float[numNodes][featureDim];

            for (int i = 0; i < numNodes; i++) {
                String type = id2type.getOrDefault(i, "Unknown");
                String name = id2node.getOrDefault(i, "");
                double[] featD = NodeFeatureEncoder.encodeAll(type, name);
                for (int j = 0; j < featureDim; j++) {
                    features[i][j] = (float) featD[j];
                }
            }
            NDArray featuresND = manager.create(features);

            // 3. 读取邻接矩阵，文件格式仍旧是边列表 src tgt，构造邻接矩阵
            NDArray adj = loadAdjacency("edge.txt", manager, numNodes);

            // 4. 构造标签数组
            // 这里假设你有一个 train_label.txt，每行格式: nodeId label
            long[] labelsArr = new long[numNodes];
            Arrays.fill(labelsArr, -1);
            try (BufferedReader br = new BufferedReader(new FileReader("train_label.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] sp = line.trim().split("\\s+");
                    if (sp.length >= 2) {
                        int nodeId = Integer.parseInt(sp[0]);
                        long label = Long.parseLong(sp[1]);
                        if (nodeId >= 0 && nodeId < numNodes) {
                            labelsArr[nodeId] = label;
                        }
                    }
                }
            }
            NDArray labels = manager.create(labelsArr);

            // 5. 构造GCN模型，结构不变
            Block block = new SequentialBlock()
                    .add(new GCNLayer(featureDim, 32, manager, adj))
                    .add(Activation.reluBlock())
                    .add(new GCNLayer(32, 3, manager, adj));  // 3分类

            Model model = Model.newInstance("gcn");
            model.setBlock(block);

            Tracker lrt = Tracker.fixed(0.01f);
            Optimizer opt = Optimizer.adam().optLearningRateTracker(lrt).build();

            DefaultTrainingConfig config = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                    .optOptimizer(opt)
                    .addTrainingListeners(TrainingListener.Defaults.logging());

            try (Trainer trainer = model.newTrainer(config)) {
                trainer.initialize(new Shape(numNodes, featureDim));

                // 6. 只用有标签节点训练
                List<Integer> labeledNodes = new ArrayList<>();
                for (int i = 0; i < numNodes; i++) {
                    if (labelsArr[i] >= 0) {
                        labeledNodes.add(i);
                    }
                }

                float[][] trainFeatures = new float[labeledNodes.size()][featureDim];
                long[] trainLabels = new long[labeledNodes.size()];

                for (int i = 0; i < labeledNodes.size(); i++) {
                    int idx = labeledNodes.get(i);
                    NDArray row = featuresND.get(idx);
                    trainFeatures[i] = row.toFloatArray();
                    trainLabels[i] = labelsArr[idx];
                }

                ArrayDataset dataset = new ArrayDataset.Builder()
                        .setData(manager.create(trainFeatures))
                        .optLabels(manager.create(trainLabels))
                        .setSampling(labeledNodes.size(), false)
                        .build();

                // 7. 训练循环（不变）
                int epochs = 100;
                for (int epoch = 0; epoch < epochs; epoch++) {
                    for (Batch batch : trainer.iterateDataset(dataset)) {
                        try (GradientCollector gc = trainer.newGradientCollector()) {
                            NDArray X = batch.getData().head();
                            NDArray y = batch.getLabels().head();

                            NDArray logits = trainer.forward(new NDList(X)).singletonOrThrow();

                            NDArray lossValue = Loss.softmaxCrossEntropyLoss()
                                    .evaluate(new NDList(y), new NDList(logits));
                            gc.backward(lossValue);

                            if (epoch % 10 == 0) {
                                System.out.printf("Epoch %d, loss=%.4f%n", epoch, lossValue.getFloat());
                            }
                        }
                        trainer.step();
                        batch.close();
                    }
                }

                // 8. 预测
                NDArray logits = trainer.forward(new NDList(featuresND)).singletonOrThrow();
                NDArray pred = logits.argMax(1);
                System.out.println("Predicted labels: " + pred);
            }
        }
    }

}

