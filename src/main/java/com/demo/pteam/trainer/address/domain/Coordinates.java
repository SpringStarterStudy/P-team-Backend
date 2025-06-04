package com.demo.pteam.trainer.address.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Coordinates that = (Coordinates) o;

    return latitude.compareTo(that.latitude) == 0 &&
            longitude.compareTo(that.longitude) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(latitude, longitude);
  }
}
