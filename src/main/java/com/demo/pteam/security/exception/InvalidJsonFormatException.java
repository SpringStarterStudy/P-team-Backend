package com.demo.pteam.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJsonFormatException extends AuthenticationException {
    public InvalidJsonFormatException(String msg) {
        super(msg);
    }

    public InvalidJsonFormatException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
