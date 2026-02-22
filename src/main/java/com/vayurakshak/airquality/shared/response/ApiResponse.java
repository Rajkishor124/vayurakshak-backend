package com.vayurakshak.airquality.shared.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;

    // ✅ success with data
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(null)
                .build();
    }

    // ✅ success with message + data
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // ✅ error with message
    public static ApiResponse<?> error(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }

    // ✅ error with details (validation errors etc.)
    public static ApiResponse<?> error(String message, Object errors) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .data(errors)
                .build();
    }
}
