package com.demo.pteam.trainer.profile.exception;

import com.demo.pteam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TrainerProfileErrorCode implements ErrorCode {

    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "P_001", "존재하지 않는 트레이너 프로필입니다."),
    PROFILE_USER_ID_NULL(HttpStatus.BAD_REQUEST, "P_002", "User ID는 필수입니다."),
    PROFILE_NAME_PUBLIC_NULL(HttpStatus.BAD_REQUEST, "P_003", "이름 공개 여부(isNamePublic)는 필수입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    TrainerProfileErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}