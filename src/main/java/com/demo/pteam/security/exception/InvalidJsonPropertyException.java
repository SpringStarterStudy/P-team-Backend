package com.demo.pteam.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJsonPropertyException extends AuthenticationException {
    private String propertyName;

    public InvalidJsonPropertyException(String msg) {
        super(msg);
    }

    public InvalidJsonPropertyException(String msg, Throwable cause, String propertyName) {
        super(msg, cause);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
