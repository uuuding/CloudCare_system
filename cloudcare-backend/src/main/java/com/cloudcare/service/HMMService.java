package com.cloudcare.service;

import java.util.Map;

public interface HMMService {

    public String predictNextState(int[] observation, int[] diseaseCount, String currentState);

    public Map<String, Double> getStateScores(int[] observation, int[] diseaseCount, String currentState);
}
