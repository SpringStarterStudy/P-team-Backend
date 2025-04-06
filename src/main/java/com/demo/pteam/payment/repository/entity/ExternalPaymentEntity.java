package com.demo.pteam.payment.repository.entity;

import com.demo.pteam.payment.service.domain.GatewayType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ExternalPayment")
@NoArgsConstructor
@Getter
public class ExternalPaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "gateway_type", nullable = false)
    private GatewayType gatewayType;

    @Column(name = "external_payment_key", nullable = false, length = 20)
    private String externalPaymentKey;

    @Column(name = "cid", nullable = false, length = 20)
    private String cid;

    @Column(name = "approved_at", nullable = false)
    private LocalDateTime approvedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "amount", columnDefinition = "text", nullable = false)
    private String amount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Builder
    public ExternalPaymentEntity(GatewayType gatewayType, String externalPaymentKey, String cid,
                                 LocalDateTime approvedAt, LocalDateTime cancelledAt,
                                 String amount, PaymentEntity payment) {
        this.gatewayType = gatewayType;
        this.externalPaymentKey = externalPaymentKey;
        this.cid = cid;
        this.approvedAt = approvedAt;
        this.cancelledAt = cancelledAt;
        this.amount = amount;
        this.payment = payment;
    }

}
