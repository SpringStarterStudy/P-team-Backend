package com.demo.pteam.trainer.address.domain;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.trainer.address.exception.TrainerAddressErrorCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Coordinates {
  private final BigDecimal latitude;
  private final BigDecimal longitude;

  public Coordinates(BigDecimal latitude, BigDecimal longitude) {
    if (latitude == null || longitude == null) {
      throw new ApiException(TrainerAddressErrorCode.COORDINATES_NULL);
    }
    if (latitude.abs().compareTo(BigDecimal.valueOf(90)) > 0) {
      throw new ApiException(TrainerAddressErrorCode.INVALID_LATITUDE);
    }
    if (longitude.abs().compareTo(BigDecimal.valueOf(180)) > 0) {
      throw new ApiException(TrainerAddressErrorCode.INVALID_LONGITUDE);
    }

    this.latitude = latitude;
    this.longitude = longitude;
  }
}
