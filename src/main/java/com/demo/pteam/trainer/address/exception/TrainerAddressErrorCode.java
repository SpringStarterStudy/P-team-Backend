package com.demo.pteam.trainer.address.exception;

import com.demo.pteam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TrainerAddressErrorCode implements ErrorCode {

    // 주소
    COORDINATES_NULL(HttpStatus.BAD_REQUEST, "L_001", "위도, 경도 값은 null일 수 없습니다."),
    INVALID_LATITUDE(HttpStatus.BAD_REQUEST, "L_002", "위도 값은 -90 ~ 90 사이여야 합니다."),
    INVALID_LONGITUDE(HttpStatus.BAD_REQUEST, "L_003", "경도 값은 -180 ~ 180 사이여야 합니다."),
    ADDRESS_COORDINATE_MISMATCH(HttpStatus.BAD_REQUEST, "L_004", "위도/경도와 도로명 주소가 일치하지 않습니다."),
    KAKAO_API_EMPTY_RESPONSE(HttpStatus.BAD_GATEWAY, "L_005", "카카오 지도 API 응답이 비어있습니다."),
    ROAD_ADDRESS_NOT_FOUND(HttpStatus.BAD_REQUEST, "L_006", "도로명 주소를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    TrainerAddressErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
