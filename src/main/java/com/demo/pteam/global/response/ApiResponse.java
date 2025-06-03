package com.demo.pteam.global.response;

import com.demo.pteam.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private final int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    private ApiResponse(HttpStatus status, String code, String message, T data) {
        this.status = status.value();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private ApiResponse(HttpStatus status, String code, String message) {
        this(status, code, message, null);
    }

    private ApiResponse(HttpStatus status, String message, T data) {
        this(status, null, message, data);
    }

    private ApiResponse(HttpStatus status, String message) {
        this(status, null, message, null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(HttpStatus.OK,"success");
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(HttpStatus.OK, message);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK, message, data);
    }

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static ApiResponse<String> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }

    // MethodArgumentNotValidException 발생 시 사용됨
    public static ApiResponse<String> error(ErrorCode errorCode, String customMessage) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getCode(), customMessage);
    }
}
