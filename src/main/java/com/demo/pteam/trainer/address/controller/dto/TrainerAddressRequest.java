package com.demo.pteam.trainer.address.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerAddressRequest {

    private Long profileId;
    private String numberAddress;
    private String streetAddress;
    private String detailAddress;
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;

}