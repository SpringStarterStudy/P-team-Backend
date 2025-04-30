package com.demo.pteam.workout.exception;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.global.exception.ErrorCode;

public class WorkoutException extends ApiException {

    public WorkoutException(ErrorCode errorCode) {
        super(errorCode);
    }
}
