package com.demo.pteam.payment.exception;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.global.exception.ErrorCode;

public class PaymentException extends ApiException {
    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
