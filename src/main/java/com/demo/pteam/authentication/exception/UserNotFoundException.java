package com.demo.pteam.authentication.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
