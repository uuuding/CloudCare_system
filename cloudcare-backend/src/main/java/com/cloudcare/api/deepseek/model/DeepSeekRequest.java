package com.cloudcare.api.deepseek.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Encapsulates the request payload for the DeepSeek Chat API.
 */
@Data
public class DeepSeekRequest {

    private String sessionId; // 唯一标识对话（建议由前端传 UUID）
    private String model;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    @JsonProperty("top_p")
    private Double topP;
    @JsonProperty("tool_choice")
    private String toolChoice;
    private List<ChatMessage> messages;
    private Object statistics; // Keep statistics flexible

}