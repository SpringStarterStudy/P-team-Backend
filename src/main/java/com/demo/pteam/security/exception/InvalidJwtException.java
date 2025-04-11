package com.demo.pteam.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtException extends AuthenticationException {
    public InvalidJwtException(String message) {
        super(message);
    }

    public InvalidJwtException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
