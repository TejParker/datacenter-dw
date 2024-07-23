package com.ziyun.dataapi.bean;

import java.util.List;
import java.util.Map;

public class DataResponse {

    private boolean success;
    private String message;
    private List<Map<String, Object>> data;

    public DataResponse() {
    }

    public DataResponse(boolean success, String message, List<Map<String, Object>> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    // Optional: Add methods to manipulate or format data
}
