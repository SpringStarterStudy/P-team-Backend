package com.demo.pteam.global.exception;

import com.demo.pteam.global.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(ApiException e) {
        log.error("CustomException: {}", e.getErrorCode().getMessage());
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(ApiResponse.error(e.getErrorCode()));
    }

    // ValidationException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(
        BindingResult bindingResult) {
        log.error("Validation 예외 발생: {}", bindingResult.getAllErrors());
        return ResponseEntity
            .status(ErrorCode.VALIDATION_EXCEPTION.getStatus())
            .body(ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION,
                bindingResult.getAllErrors().get(0).getDefaultMessage()));
    }

    // ValidationException 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(this::formatViolationMessage)
                .collect(Collectors.joining(", "));
        log.error("Validation 예외 발생: {}", errorMessage);
        return ResponseEntity.badRequest().body(
                ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION, errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.error("요청 파싱 예외 발생: {}", ex.getMessage());

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatEx &&
            invalidFormatEx.getTargetType().isEnum()) {

            // enum 변환 실패
            String message = "올바르지 않은 상태값입니다. [허용 값: PENDING, APPROVED, REJECTED]";
            return ResponseEntity
                .status(ErrorCode.VALIDATION_EXCEPTION.getStatus())
                .body(ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION, message));
        }
        return ResponseEntity
            .status(ErrorCode.VALIDATION_EXCEPTION.getStatus())
            .body(ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION));
    }

    private String formatViolationMessage(ConstraintViolation<?> violation) {
        String fullPath = violation.getPropertyPath().toString();
        String field = fullPath.substring(fullPath.lastIndexOf(".") + 1); // 필드명만 추출
        return field + ": " + violation.getMessage();
    }
}

