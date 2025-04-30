package com.demo.pteam.global.exception;

import com.demo.pteam.global.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(ApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("CustomException: {}", errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode));
    }

    // ValidationException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(
            MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        log.error("Validation 예외 발생: {}", bindingResult.getAllErrors());
        return ResponseEntity
                .status(GlobalErrorCode.VALIDATION_EXCEPTION.getStatus())
                .body(ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION,
                        bindingResult.getAllErrors().get(0).getDefaultMessage()));
    }

    // ValidationException 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(
        ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
            .map(this::formatViolationMessage)
            .collect(Collectors.joining(", "));
        log.error("Validation 예외 발생: {}", errorMessage);
        return ResponseEntity.badRequest().body(
            ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION, errorMessage));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatchException(
        MethodArgumentTypeMismatchException ex) {
        log.error("입력 형식 예외 : {}", ex.getMessage());
        return ResponseEntity.status(GlobalErrorCode.VALIDATION_EXCEPTION.getStatus())
            .body(ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.error("요청 파싱 예외 발생: {}", ex.getMessage());


        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatEx &&
            invalidFormatEx.getTargetType().isEnum()) {

            Object[] enumConstants = invalidFormatEx.getTargetType().getEnumConstants();
            String allowedValues = Arrays.stream(enumConstants)
                .map(Object::toString)
                .collect(Collectors.joining(", "));

            // enum 변환 실패
            String message = String.format(GlobalErrorCode.VALIDATION_EXCEPTION.getMessage(), allowedValues);

            return ResponseEntity
                .status(GlobalErrorCode.VALIDATION_EXCEPTION.getStatus())
                .body(ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION, message));
        }
        return ResponseEntity
            .status(GlobalErrorCode.VALIDATION_EXCEPTION.getStatus())
            .body(ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION));
    }

    private String formatViolationMessage(ConstraintViolation<?> violation) {
        String fullPath = violation.getPropertyPath().toString();
        String field = fullPath.substring(fullPath.lastIndexOf(".") + 1); // 필드명만 추출
        return field + ": " + violation.getMessage();
    }
}

