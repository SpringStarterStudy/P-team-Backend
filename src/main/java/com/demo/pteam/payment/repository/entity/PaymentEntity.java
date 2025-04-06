package com.demo.pteam.payment.repository.entity;

import com.demo.pteam.payment.domain.PaymentMethod;
import com.demo.pteam.payment.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Payment")
@NoArgsConstructor
@Getter
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "product_id")
    private String productId;

    @Enumerated(EnumType.STRING)
    @Column(name= "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_money", nullable = false)
    private Long paymentMoney;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    //TODO : 계정 연관관계 맵핑
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, optional = true)
    private ExternalPaymentEntity externalPayment;

    //TODO : 계정 수정
    @Builder
    public PaymentEntity(Long paymentId, String productId, PaymentMethod paymentMethod,
                         Long paymentMoney, LocalDateTime transactionTime,
                         PaymentStatus status, Long accountId) {
        this.paymentId = paymentId;
        this.productId = productId;
        this.paymentMethod = paymentMethod;
        this.transactionTime = transactionTime;
        this.paymentMoney = paymentMoney;
        this.status = status;
        this.accountId = accountId;
    }
}
