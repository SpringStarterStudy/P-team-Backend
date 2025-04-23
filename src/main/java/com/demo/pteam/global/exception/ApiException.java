package com.demo.pteam.global.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final CommonErrorCode errorCode;

    public ApiException(CommonErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}