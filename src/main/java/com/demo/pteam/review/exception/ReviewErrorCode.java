package com.demo.pteam.review.exception;

import com.demo.pteam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReviewErrorCode implements ErrorCode {

    REVIEW_CONTENT_TOO_SHORT(HttpStatus.BAD_REQUEST, "R_001", "리뷰 내용이 최소 글자수를 미달했습니다."),
    REVIEW_CONTENT_INAPPROPRIATE(HttpStatus.BAD_REQUEST, "R_002", "리뷰 내용이 부적절합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ReviewErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
