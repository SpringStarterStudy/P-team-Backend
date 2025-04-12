package com.demo.pteam.payment.domain;

import com.demo.pteam.global.exception.ErrorCode;
import com.demo.pteam.payment.exception.PaymentException;
import lombok.Getter;

public class Payment {
    private final Long accountId;
    private final String productId;
    private final PaymentMethod paymentMethod;
    private final Long paymentMoney;
    @Getter
    private PaymentStatus status;

    private Payment(Long accountId, String productId, PaymentMethod paymentMethod, Long paymentMoney){
        if (paymentMoney <= 0) {
            throw new IllegalArgumentException("결제 금액은 0보다 커야 합니다.");
        }
        this.accountId = accountId;
        this.productId = productId;
        this.paymentMethod = paymentMethod;
        this.paymentMoney = paymentMoney;
        this.status = PaymentStatus.PENDING;
    }


    public static Payment create(Long accountId, String productId, PaymentMethod method, Long paymentMoney){
        return new Payment(accountId, productId, method, paymentMoney);
    }

    public void success(){
        if (status != PaymentStatus.PENDING) {
            throw new PaymentException(ErrorCode.PAYMENT_EXCEPTION);
        }
        changeStatus(PaymentStatus.SUCCESS);
    }


    public void cancel(){
        if (status != PaymentStatus.SUCCESS){
            throw new PaymentException(ErrorCode.PAYMENT_EXCEPTION);
        }
        changeStatus(PaymentStatus.CANCELLED);
    }

    public void fail() {
        if (status != PaymentStatus.PENDING) {
            throw new PaymentException(ErrorCode.PAYMENT_EXCEPTION);
        }
        changeStatus(PaymentStatus.FAILED);
    }


    private void changeStatus(PaymentStatus newStatus){
        this.status = newStatus;
    }
}
