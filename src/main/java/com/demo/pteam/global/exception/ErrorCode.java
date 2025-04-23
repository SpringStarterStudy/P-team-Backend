package com.demo.pteam.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();

}
