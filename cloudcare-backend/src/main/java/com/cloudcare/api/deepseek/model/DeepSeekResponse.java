package com.cloudcare.api.deepseek.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Encapsulates the response payload from the DeepSeek Chat API.
 */
@Setter
@Getter
public class DeepSeekResponse {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<ResponseChoice> choices;
    private UsageStats usage;

    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ResponseChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ResponseChoice> choices) {
        this.choices = choices;
    }

    public UsageStats getUsage() {
        return usage;
    }

    public void setUsage(UsageStats usage) {
        this.usage = usage;
    }

    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    public void setSystemFingerprint(String systemFingerprint) {
        this.systemFingerprint = systemFingerprint;
    }
}