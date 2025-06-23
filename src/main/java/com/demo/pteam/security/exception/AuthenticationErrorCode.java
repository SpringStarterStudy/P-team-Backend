package com.demo.pteam.security.exception;

import com.demo.pteam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthenticationErrorCode implements ErrorCode {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A_005", "아이디 또는 비밀번호가 잘못되었습니다."),
    ACCOUNT_SUSPENDED(HttpStatus.FORBIDDEN, "A_006", "계정이 정지된 상태입니다."),
    NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "A_008", "로그인이 필요합니다."),
    INVALID_ACCESS(HttpStatus.FORBIDDEN, "A_009", "접근 권한이 없습니다."),
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "A_011", "인증 정보가 유효하지 않습니다. 다시 로그인해 주세요."),
    AUTHENTICATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A_012", "사용자 인증에 실패했습니다."),
    AUTHORIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A_014", "접근 권한 확인에 실패했습니다."),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A_017", "로그인에 실패했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "A_018", "POST 요청만 허용됩니다."),
    INVALID_JSON_PROPERTY(HttpStatus.BAD_REQUEST, "A_019", "입력한 '%s' 속성이 유효하지 않습니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "A_020", "요청한 형식이 올바르지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AuthenticationErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
