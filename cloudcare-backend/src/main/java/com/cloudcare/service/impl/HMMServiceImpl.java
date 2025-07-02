package com.cloudcare.service.impl;

import com.cloudcare.entity.HMM;
import com.cloudcare.service.HMMService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HMMServiceImpl implements HMMService {
    /**
     * 预测下一状态
     *
     * @param observation 身体指标（长度为 5 的数组，值为 0/1/2）
     * @param diseaseCount 三类慢性病数量（长度为 3 的数组）
     * @param currentState 当前状态（健康/注意/异常）
     * @return 下一状态名称
     */
    public String predictNextState(int[] observation, int[] diseaseCount, String currentState) {
        return HMM.predictNextState(observation, diseaseCount, currentState);
    }

    /**
     * 获取各状态得分（Softmax后概率）
     *
     * @param observation 身体指标
     * @param diseaseCount 慢性病统计
     * @param currentState 当前状态
     * @return 状态名称与评分的映射
     */
    public Map<String, Double> getStateScores(int[] observation, int[] diseaseCount, String currentState) {
        return HMM.evaluateNextStateScores(observation, diseaseCount, currentState);
    }
}
