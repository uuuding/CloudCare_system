package com.cloudcare.api.deepseek.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a message in the chat conversation for the DeepSeek API.
 */
@Setter
@Getter
public class ChatMessage {

    private String content;
    private String role;

    public ChatMessage() {
    }

    public ChatMessage(String role, String content) {
        this.content = content;
        this.role = role;
    }


}