package com.demo.pteam.global.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final GlobalErrorCode errorCode;

    public ApiException(GlobalErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}