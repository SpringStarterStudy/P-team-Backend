package com.demo.pteam.credit.repository.entity;

import com.demo.pteam.credit.domain.CreditInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CreditLog")
@NoArgsConstructor
@Getter
public class CreditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: 계정 연관관계 추가
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "credit_difference", nullable = false)
    private Integer creditDifference;

    @Enumerated(EnumType.STRING)
    //TODO: 크레딧 인포 DB 수정
    @Column(name = "credit_info", nullable = false)
    private CreditInfo creditInfo;

    @Column(name = "credit_balance", nullable = false)
    private Integer creditBalance;

    @Builder
    public CreditLogEntity(Long accountId, Integer creditDifference,
                           CreditInfo creditInfo, Integer creditBalance) {
        this.accountId = accountId;
        this.creditDifference = creditDifference;
        this.creditInfo = creditInfo;
        this.creditBalance = creditBalance;
    }

}
