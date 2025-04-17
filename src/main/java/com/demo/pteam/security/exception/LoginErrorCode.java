package com.demo.pteam.security.exception;

import com.demo.pteam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LoginErrorCode implements ErrorCode {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A_005", "아이디 또는 비밀번호가 잘못되었습니다."),
    ACCOUNT_SUSPENDED(HttpStatus.FORBIDDEN, "A_006", "계정이 정지된 상태입니다."),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A_017", "로그인에 실패했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "A_018", "POST 요청만 허용됩니다."),
    INVALID_JSON_PROPERTY(HttpStatus.BAD_REQUEST, "A_019", "입력한 '%s' 속성이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    LoginErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
