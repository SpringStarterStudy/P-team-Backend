package com.demo.pteam.security.exception;

import org.springframework.security.core.AuthenticationException;

public class MethodNotAllowedException extends AuthenticationException {
    public MethodNotAllowedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MethodNotAllowedException(String msg) {
        super(msg);
    }
}
