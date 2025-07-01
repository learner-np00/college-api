package com.learner.collegeapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private List<ErrorDetail> errors;

    public ApiResponse(String status, String message, T data, List<ErrorDetail> errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data, null);
    }

    public static <T> ApiResponse<T> error(String message, List<ErrorDetail> errors) {
        return new ApiResponse<>("error", message, null, errors);
    }
}
