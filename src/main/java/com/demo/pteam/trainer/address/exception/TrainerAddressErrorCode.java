package com.demo.pteam.trainer.address.exception;

import com.demo.pteam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TrainerAddressErrorCode implements ErrorCode {

    // 주소
    COORDINATES_NULL(HttpStatus.BAD_REQUEST, "L_001", "위도, 경도 값은 null일 수 없습니다."),
    INVALID_LATITUDE(HttpStatus.BAD_REQUEST, "L_002", "위도 값은 -90 ~ 90 사이여야 합니다."),
    INVALID_LONGITUDE(HttpStatus.BAD_REQUEST, "L_003", "경도 값은 -180 ~ 180 사이여야 합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    TrainerAddressErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
