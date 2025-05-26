package com.demo.pteam.trainer.address.domain;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class Coordinates {
  private final BigDecimal latitude;
  private final BigDecimal longitude;

  public Coordinates(BigDecimal latitude, BigDecimal longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public boolean isNull() {
    return latitude == null || longitude == null;
  }

  public boolean isInvalidLatitude() {
    return latitude != null && latitude.abs().compareTo(BigDecimal.valueOf(90)) > 0;
  }

  public boolean isInvalidLongitude() {
    return longitude != null && longitude.abs().compareTo(BigDecimal.valueOf(180)) > 0;
  }

}
