package com.cloudcare.api.deepseek.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the format of the response for the DeepSeek API.
 */
@Setter
@Getter
public class ResponseFormat {

    private String type;

    public ResponseFormat() {
        this.type = "text"; // Default value
    }

    public ResponseFormat(String type) {
        this.type = type;
    }

    // Getters and Setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}