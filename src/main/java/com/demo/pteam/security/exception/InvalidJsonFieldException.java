package com.demo.pteam.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJsonFieldException extends AuthenticationException {
    private String propertyName;

    public InvalidJsonFieldException(String msg) {
        super(msg);
    }

    public InvalidJsonFieldException(String msg, Throwable cause, String propertyName) {
        super(msg, cause);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
