package com.ziyun.dataapi.bean;

import java.util.Map;

public class DataRequest {

    private String type;
    private Map<String, Object> parameters;

    // Constructors
    public DataRequest() {
    }

    public DataRequest(String type, Map<String, Object> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    // Optional: Add methods for validation or preprocessing
}

