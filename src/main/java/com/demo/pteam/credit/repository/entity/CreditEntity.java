package com.demo.pteam.credit.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Credit")
@NoArgsConstructor
@Getter
public class CreditEntity {
    // TODO : 테이블 제약조건 추가

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : 계정 연관관계 추가
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "credit_balance", nullable = false)
    private Integer creditBalance;

    @Builder
    public CreditEntity(Long accountId, Integer creditBalance) {
        this.accountId = accountId;
        this.creditBalance = creditBalance;
    }

}
