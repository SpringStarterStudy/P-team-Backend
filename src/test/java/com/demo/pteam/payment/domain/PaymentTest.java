package com.demo.pteam.payment.domain;

import com.demo.pteam.payment.exception.PaymentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {

    // 기본 결제 객체
    private Payment createPayment() {
        return Payment.create(1L, "PROD0", PaymentMethod.SIMPLE_PAYMENT, 15000L);
    }

    // 결제 상태 관리 테스트
    @DisplayName("결제 객체 생성 시 상태는 PENDING 이다")
    @Test
    void should_initialize_with_pending_status() {
        Payment payment = createPayment();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @DisplayName("결제를 승인하면 상태는 SUCCESS 로 변경된다")
    @Test
    void should_change_status_to_success_when_approved() {
        Payment payment = createPayment();
        payment.success();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
    }

    @DisplayName("결제가 실패하면 상태는 FAILED 로 변경된다")
    @Test
    void should_change_status_to_failed_when_payment_fails() {
        Payment payment = createPayment();
        payment.fail();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @DisplayName("승인된 결제를 취소하면 상태는 CANCELLED 로 변경된다")
    @Test
    void should_change_status_to_cancelled_when_success_payment_is_cancelled() {
        Payment payment = createPayment();
        payment.success();
        payment.cancel();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELLED);
    }

    // 예외 처리 테스트
    @DisplayName("이미 승인된 결제를 다시 승인하려고 하면 예외가 발생한다")
    @Test
    void should_throw_exception_when_approving_already_approved_payment() {
        Payment payment = createPayment();
        payment.success();

        assertThrows(PaymentException.class, payment::success);
    }

    @DisplayName("성공하지 않은 결제를 취소하려고 하면 예외가 발생한다")
    @Test
    void should_throw_exception_when_cancelling_unapproved_payment() {
        Payment payment = createPayment();
        assertThrows(PaymentException.class, payment::cancel);
    }

    @DisplayName("이미 성공한 결제를 실패 처리하려고 하면 예외가 발생한다")
    @Test
    void should_throw_exception_when_failing_already_successful_payment() {
        Payment payment = createPayment();
        payment.success();
        assertThrows(PaymentException.class, payment::fail);
    }

    // 데이터 유효성 테스트
    @DisplayName("결제 금액이 0원 이하이면 예외가 발생한다")
    @Test
    void should_throw_exception_when_amount_is_zero_or_less() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment.create(1L, "PROD0", PaymentMethod.SIMPLE_PAYMENT, 0L);
        });
    }
}
