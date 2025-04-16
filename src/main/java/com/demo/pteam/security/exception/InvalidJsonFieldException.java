package com.demo.pteam.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJsonFieldException extends AuthenticationException {
    public InvalidJsonFieldException(String msg) {
        super(msg);
    }

    public InvalidJsonFieldException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
