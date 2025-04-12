package com.demo.pteam.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // ValidationException
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "V_001", "잘못된 입력 형식입니다."),

    // Payment
    // 400
    PAYMENT_EXCEPTION(HttpStatus.BAD_REQUEST,"P_001","유효하지 않은 주문 상태입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

