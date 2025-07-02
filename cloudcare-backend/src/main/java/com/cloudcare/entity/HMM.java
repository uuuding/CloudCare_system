package com.cloudcare.entity;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class HMM {

    // 状态名
    static final String[] STATES = {"健康", "注意", "异常"};


    // 初始状态概率
    static double[] pi = {0.40, 0.45, 0.15};

    // 发射概率矩阵 emission[state][feature][level]
    static double[][][] emission = new double[4][5][3];

    static double biasWeight = 0.6;// 门控融合(a,1-a)

    static double[] biasA = {0.0, 0.5, 1.2}; // A类：重病 → 偏向高风险
    static double[] biasB = {0.0, 0.35, 0.6}; // B类：中度病
    static double[] biasC = {0.0, 0.15, 0.27}; // C类：轻病

    // 状态转移概率矩阵 transition[from][to]
    static double[][] transition = {
            // 健康    注意    异常
            {0.80,    0.18,    0.02},    // 健康
            {0.10,    0.70,    0.20},    // 注意
            {0.02,    0.28,    0.70}     // 异常
    };


    // 初始化发射概率
    static {
        // 健康
        emission[0][0] = new double[]{0.90, 0.09, 0.01};
        emission[0][1] = new double[]{0.85, 0.10, 0.05};
        emission[0][2] = new double[]{0.88, 0.10, 0.02};
        emission[0][3] = new double[]{0.80, 0.15, 0.05};
        emission[0][4] = new double[]{0.85, 0.10, 0.05};

        // 注意 = 平均(亚健康, 预警)
        emission[1][0] = new double[]{(0.60 + 0.30) / 2, (0.30 + 0.50) / 2, (0.10 + 0.20) / 2};  // 体温
        emission[1][1] = new double[]{(0.50 + 0.20) / 2, (0.30 + 0.50) / 2, (0.20 + 0.30) / 2};  // 压力
        emission[1][2] = new double[]{(0.60 + 0.25) / 2, (0.30 + 0.50) / 2, (0.10 + 0.25) / 2};  // BIM
        emission[1][3] = new double[]{(0.55 + 0.25) / 2, (0.35 + 0.45) / 2, (0.10 + 0.30) / 2};  // 心率
        emission[1][4] = new double[]{(0.60 + 0.20) / 2, (0.30 + 0.60) / 2, (0.10 + 0.20) / 2};  // 睡眠

        // 异常
        emission[2][0] = new double[]{0.10, 0.40, 0.50};
        emission[2][1] = new double[]{0.05, 0.30, 0.65};
        emission[2][2] = new double[]{0.05, 0.20, 0.75};
        emission[2][3] = new double[]{0.10, 0.30, 0.60};
        emission[2][4] = new double[]{0.05, 0.30, 0.65};
    }


    // 计算某个状态下某个观测序列的发射概率
    public static double computeEmissionProb(int state, int[] observation) {
        double prob = 1.0;
        for (int i = 0; i < observation.length; i++) {
            int level = observation[i]; // 离散值，如 0、1、2
            prob *= emission[state][i][level];
        }
        return prob;
    }

    // 综合初始状态概率 + 发射概率，返回每个状态下的得分
    public static Map<String, Double> evaluateNextStateScores(int[] observation, int[] count, String currentState) {
        Map<String, Double> scores = new LinkedHashMap<>();
        System.out.println(Arrays.toString(observation));
        int currentIdx = Arrays.asList(STATES).indexOf(currentState);
        if (currentIdx == -1) throw new IllegalArgumentException("未知状态: " + currentState);

        for (int next = 0; next < STATES.length; next++) {
            double transitionProb = transition[currentIdx][next];
            double emissionProb = computeEmissionProb(next, observation);
            double score = transitionProb * emissionProb;
            score = Math.log(score);
            scores.put(STATES[next], score);
        }

        return computBias(scores, count); // 应用门控偏置
    }


    public static double[] softmax(double[] input) {
        double max = Arrays.stream(input).max().orElse(0); // 防止指数爆炸
        double sum = 0.0;
        double[] exp = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            exp[i] = Math.exp(input[i] - max); // 减最大值提高数值稳定性
            sum += exp[i];
        }

        for (int i = 0; i < exp.length; i++) {
            exp[i] /= sum;
        }

        return exp;
    }

    // 找出最大得分对应的状态
    public static String predictNextState(int[] observation, int[] count, String currentState) {
        Map<String, Double> scores = evaluateNextStateScores(observation, count, currentState);
        return scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("未知");
    }

    public static Map<String, Double> computBias(Map<String, Double>scores,int[] count){

        // count统计A,B,C类疾病的数量
        double[] biasTensor = new double[3];
        for (int i = 0; i < 3; i++) {
            biasTensor[i] = biasA[i]*count[0] + biasB[i]*count[1] + biasC[i]*count[2];
        } //总偏置向量

        double[] weighted = new double[3];

        for (int s = 0; s < STATES.length; s++){
            weighted[s] = scores.get(STATES[s]) * biasWeight + biasTensor[s] * (1-biasWeight);
        }
        weighted = softmax(weighted);

        for (int s = 0; s < STATES.length; s++){
            scores.put(STATES[s], weighted[s]);
        }

        return scores;
    }

    // 示例入口
    public static void main(String[] args) {
        int[] observation = {1, 0, 0, 1, 1};
        int[] count = {1, 0, 1};
        String currentState = "注意";

        System.out.println("当前状态为: " + currentState);
        System.out.println("预测下一状态为: " + predictNextState(observation, count, currentState));
        System.out.println("各状态评分：");
        evaluateNextStateScores(observation, count, currentState)
                .forEach((k, v) -> System.out.printf("%s: %.5f\n", k, v));
    }


}
