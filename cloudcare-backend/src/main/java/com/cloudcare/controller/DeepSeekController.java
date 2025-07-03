package com.cloudcare.controller;

import com.cloudcare.api.deepseek.model.ChatMessage;
import com.cloudcare.api.deepseek.model.DeepSeekRequest;
import com.cloudcare.api.deepseek.model.DeepSeekResponse;
import com.cloudcare.common.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekController {

    @Value("${deepseek.api.key}")
    private String deepSeekApiKey;

    private final Gson gson = new GsonBuilder().serializeNulls().create();

    // 会话历史缓存，sessionId -> messages 列表
    private final Map<String, List<ChatMessage>> conversationHistory = new ConcurrentHashMap<>();

    @PostMapping("/chat")
    public Result<DeepSeekResponse> chat(@RequestBody DeepSeekRequest deepSeekRequest) {
        String sessionId = deepSeekRequest.getSessionId();
        if (sessionId == null || sessionId.isEmpty()) {
            return Result.error("Missing sessionId");
        }

        // 获取历史或初始化
        List<ChatMessage> history = conversationHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());

        // 获取新消息
        List<ChatMessage> newMessages = deepSeekRequest.getMessages();
        if (newMessages != null && !newMessages.isEmpty()) {
            history.addAll(newMessages);
        } else {
            return Result.error("Missing user message");
        }

        // 构建包含完整上下文的新请求
        DeepSeekRequest requestWithHistory = new DeepSeekRequest();
        requestWithHistory.setModel(deepSeekRequest.getModel());
        requestWithHistory.setMaxTokens(deepSeekRequest.getMaxTokens());
        requestWithHistory.setTemperature(deepSeekRequest.getTemperature());
        requestWithHistory.setTopP(deepSeekRequest.getTopP());
        requestWithHistory.setToolChoice(deepSeekRequest.getToolChoice());
        requestWithHistory.setMessages(history);

        // 调用 DeepSeek Chat API
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://api.deepseek.com/chat/completions");
            String requestBody = gson.toJson(requestWithHistory);
            httpPost.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + deepSeekApiKey);

            return httpClient.execute(httpPost, response -> {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                if (statusCode >= 200 && statusCode < 300) {
                    DeepSeekResponse deepSeekResponse = gson.fromJson(responseBody, DeepSeekResponse.class);

                    // 获取并追加 assistant 回复到上下文
                    if (deepSeekResponse.getChoices() != null && !deepSeekResponse.getChoices().isEmpty()) {
                        ChatMessage assistantReply = deepSeekResponse.getChoices().get(0).getMessage();
                        history.add(assistantReply);
                    }

                    return Result.success(deepSeekResponse);
                } else {
                    return Result.error("Failed to call DeepSeek API: " + responseBody);
                }
            });
        } catch (Exception e) {
            return Result.error("Failed to process request: " + e.getMessage());
        }
    }
}