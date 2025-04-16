package com.demo.pteam.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalErrorCode implements ErrorCode {

    // ValidationException
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "V_001", "잘못된 요청 형식입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    GlobalErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

