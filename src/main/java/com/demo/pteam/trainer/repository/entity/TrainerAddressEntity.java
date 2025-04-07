package com.demo.pteam.trainer.repository.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "trainer_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(nullable = false, length = 255)
    private String numberAddress;

    @Column(nullable = false, length = 255)
    private String roadAddress;

    @Column(length = 100)
    private String detailAddress;

    @Column(nullable = false, length = 20)
    private String postalCode;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;
}
