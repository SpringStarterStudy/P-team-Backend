package com.demo.pteam.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalErrorCode implements ErrorCode {

    // ValidationException
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "V_001", "잘못된 입력 형식입니다."),
    DB_CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "V_002", "무결성 제약 조건이 위반된 요청입니다."),
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "V_003", "데이터베이스 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    GlobalErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

