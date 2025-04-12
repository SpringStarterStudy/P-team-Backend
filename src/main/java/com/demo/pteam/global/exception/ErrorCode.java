package com.demo.pteam.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // ValidationException
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "V_001", "잘못된 입력 형식입니다."),

    // login
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A_005", "아이디 또는 비밀번호가 잘못되었습니다."),
    ACCOUNT_SUSPENDED(HttpStatus.FORBIDDEN, "A_006", "계정이 정지된 상태입니다."),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A_017", "로그인에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

