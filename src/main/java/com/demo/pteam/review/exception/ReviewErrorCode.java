package com.demo.pteam.review.exception;

import com.demo.pteam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReviewErrorCode implements ErrorCode {

    REVIEW_CONTENT_TOO_SHORT(HttpStatus.BAD_REQUEST, "R_001", "리뷰 내용이 최소 글자수를 미달했습니다."),
    REVIEW_CONTENT_INAPPROPRIATE(HttpStatus.BAD_REQUEST, "R_002", "리뷰 내용이 부적절합니다."),
    IMAGE_ID_NULL(HttpStatus.BAD_REQUEST, "R_003", "이미지 ID에 null 값이 포함되어 있습니다."),
    IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "R_004", "이미지는 최대 3개까지만 등록 가능합니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "R_005", "해당 일정에 이미 리뷰를 작성했습니다."),
    PT_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "R_006", "완료되지 않은 PT에 대해 리뷰를 작성할 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "R_007", "이미지를 찾을 수 없습니다."),
    IMAGE_ALREADY_LINKED(HttpStatus.CONFLICT, "R_008", "이미지가 이미 다른 리뷰에 연결되어 있습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R_009", "리뷰를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ReviewErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
