package com.cloudcare.api.deepseek.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the token usage statistics for the DeepSeek API call.
 */
@Setter
@Getter
public class UsageStats {

    @JsonProperty("prompt_tokens")
    private int promptTokens;

    @JsonProperty("completion_tokens")
    private int completionTokens;

    @JsonProperty("total_tokens")
    private int totalTokens;

    @JsonProperty("prompt_tokens_details")
    private PromptTokensDetails promptTokensDetails;

    @JsonProperty("prompt_cache_hit_tokens")
    private int promptCacheHitTokens;

    @JsonProperty("prompt_cache_miss_tokens")
    private int promptCacheMissTokens;

    // Getters and Setters

    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public PromptTokensDetails getPromptTokensDetails() {
        return promptTokensDetails;
    }

    public void setPromptTokensDetails(PromptTokensDetails promptTokensDetails) {
        this.promptTokensDetails = promptTokensDetails;
    }

    public int getPromptCacheHitTokens() {
        return promptCacheHitTokens;
    }

    public void setPromptCacheHitTokens(int promptCacheHitTokens) {
        this.promptCacheHitTokens = promptCacheHitTokens;
    }

    public int getPromptCacheMissTokens() {
        return promptCacheMissTokens;
    }

    public void setPromptCacheMissTokens(int promptCacheMissTokens) {
        this.promptCacheMissTokens = promptCacheMissTokens;
    }

    /**
     * Inner class for prompt_tokens_details.
     */
    public static class PromptTokensDetails {
        @JsonProperty("cached_tokens")
        private int cachedTokens;

        // Getters and Setters
        public int getCachedTokens() {
            return cachedTokens;
        }

        public void setCachedTokens(int cachedTokens) {
            this.cachedTokens = cachedTokens;
        }
    }
}