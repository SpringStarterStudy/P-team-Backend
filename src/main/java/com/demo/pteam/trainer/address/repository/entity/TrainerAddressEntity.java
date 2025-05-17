package com.demo.pteam.trainer.address.repository.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "trainer_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TrainerAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String numberAddress;

    @Column(nullable = false, length = 255)
    private String roadAddress;

    @Column(length = 100)
    private String detailAddress;

    @Column(nullable = false, length = 20)
    private String postalCode;

    @Column(nullable = false, precision = 16, scale = 14)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 17, scale = 14)
    private BigDecimal longitude;

    @Builder
    public TrainerAddressEntity(String numberAddress, String roadAddress, String detailAddress, String postalCode, BigDecimal latitude, BigDecimal longitude) {
        this.numberAddress = numberAddress;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}


