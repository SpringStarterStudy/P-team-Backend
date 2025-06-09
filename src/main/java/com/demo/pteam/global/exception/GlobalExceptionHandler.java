package com.demo.pteam.global.exception;

import com.demo.pteam.global.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(ApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("CustomException: {}", errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode));
    }

    // ValidationException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String globalErrorMessage = ex.getBindingResult().getGlobalErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        String fieldErrorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        String errorMessage = mergeErrorMessage(globalErrorMessage, fieldErrorMessage);
        log.warn("Validation 예외 발생: {}", errorMessage);
        return ResponseEntity.badRequest().body(
                ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION, errorMessage));
    }

    private static String mergeErrorMessage(String globalErrorMessage, String fieldErrorMessage) {
        if (globalErrorMessage.isEmpty()) {
            return fieldErrorMessage;
        } else if (fieldErrorMessage.isEmpty()) {
            return globalErrorMessage;
        }
        return globalErrorMessage + ", " + fieldErrorMessage;
    }

    // ValidationException 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(this::formatViolationMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation 예외 발생: {}", errorMessage);
        return ResponseEntity.badRequest().body(
                ApiResponse.error(GlobalErrorCode.VALIDATION_EXCEPTION, errorMessage));
    }

    private String formatViolationMessage(ConstraintViolation<?> violation) {
        String fullPath = violation.getPropertyPath().toString();
        String field = fullPath.substring(fullPath.lastIndexOf(".") + 1); // 필드명만 추출
        return field + ": " + violation.getMessage();
    }

    // 무결성 제약조건 위반
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.error("SQLIntegrityConstraintViolationException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.error(GlobalErrorCode.DB_CONSTRAINT_VIOLATION)
        );
    }

    // DB 에러
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<String>> handleSQLException(SQLException ex) {
        log.error("SQLException: {}", ex.getMessage());
        return ResponseEntity.internalServerError().body(
                ApiResponse.error(GlobalErrorCode.DB_ERROR)
        );
    }
}

