package com.demo.pteam.global.exception;

import com.demo.pteam.global.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
            .body(ApiResponse.error((GlobalErrorCode) e.getErrorCode()));
    }

    // ValidationException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(
        BindingResult bindingResult) {
        log.error("Validation 예외 발생: {}", bindingResult.getAllErrors());
        return ResponseEntity
            .status(GlobalErrorCode.VALIDATION_EXCEPTION.getStatus())
            .body(ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION,
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
                ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION, errorMessage));
    }

    private String formatViolationMessage(ConstraintViolation<?> violation) {
        String fullPath = violation.getPropertyPath().toString();
        String field = fullPath.substring(fullPath.lastIndexOf(".") + 1); // 필드명만 추출
        return field + ": " + violation.getMessage();
    }
}

