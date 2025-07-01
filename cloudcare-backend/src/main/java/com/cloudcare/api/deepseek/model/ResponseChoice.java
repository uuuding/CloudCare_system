package com.cloudcare.api.deepseek.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a choice in the DeepSeek API response.
 */
@Setter
@Getter
public class ResponseChoice {

    private int index;
    private ChatMessage message;
    private Object logprobs;

    @JsonProperty("finish_reason")
    private String finishReason;

    // Getters and Setters

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public Object getLogprobs() {
        return logprobs;
    }

    public void setLogprobs(Object logprobs) {
        this.logprobs = logprobs;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}