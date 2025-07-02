package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.service.HMMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@RestController
@RequestMapping("/api/hmm")
public class HMMController {

    @Autowired
    private HMMService hmmService;

    /**
     * 预测下一状态
     */
    @PostMapping("/predict")
    public Result<Map<String, Object>> predictNextState(@RequestBody HMMRequest request) {
        try {
            String nextState = hmmService.predictNextState(request.getObservation(), request.getDiseaseCount(), request.getCurrentState());
            Map<String, Object> result = new HashMap<>();
            result.put("currentState", request.getCurrentState());
            result.put("nextState", nextState);
            return Result.success(result, "预测成功");
        } catch (IllegalArgumentException e) {
            return Result.error("无效的状态：" + e.getMessage());
        } catch (Exception e) {
            return Result.error("预测过程中发生错误: " + e.getMessage());
        }
    }

    /**
     * 获取各状态评分接口
     */
    @PostMapping("/scores")
    public Result<Map<String, Double>> getStateScores(@RequestBody HMMRequest request) {
        try {
            Map<String, Double> scores = hmmService.getStateScores(request.getObservation(), request.getDiseaseCount(), request.getCurrentState());
            return Result.success(scores, "状态评分获取成功");
        } catch (IllegalArgumentException e) {
            return Result.error("无效的状态：" + e.getMessage());
        } catch (Exception e) {
            return Result.error("评分计算错误: " + e.getMessage());
        }
    }

    /**
     * 请求体封装类
     */
    @Data
    public static class HMMRequest {
        private int[] observation;      // 身体指标：体温、压、BMI、心率、睡眠（离散化 0/1/2）
        private int[] diseaseCount;     // 三类疾病数量：A/B/C类
        private String currentState;    // 当前状态：健康/注意/异常
    }
}

