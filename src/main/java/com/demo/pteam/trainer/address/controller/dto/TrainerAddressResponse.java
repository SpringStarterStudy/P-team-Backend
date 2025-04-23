package com.demo.pteam.trainer.address.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TrainerAddressResponse {
    private Long addressId;
    private Long profileId;
    private String streetAddress;
    private String detailAddress;
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
