package com.demo.pteam.workout.exception;

import com.demo.pteam.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum WorkoutErrorCode implements ErrorCode {
    WORKOUT_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "W_001", "존재하지 않는 신청 ID입니다."),
    WORKOUT_REQUEST_ALREADY_PROCESSED(HttpStatus.CONFLICT, "W_002", "이미 처리된 신청입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    WorkoutErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
