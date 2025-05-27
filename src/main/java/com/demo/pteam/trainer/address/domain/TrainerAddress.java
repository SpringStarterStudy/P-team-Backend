package com.demo.pteam.trainer.address.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class TrainerAddress {

  private final Long id;
  private final String numberAddress;
  private final String roadAddress;
  private final String detailAddress;
  private final String postalCode;
  private final Coordinates coordinates;

  public TrainerAddress(Long id, String numberAddress, String roadAddress, String detailAddress,
                        String postalCode, Coordinates coordinates) {
    this.id = id;
    this.numberAddress = numberAddress;
    this.roadAddress = roadAddress;
    this.detailAddress = detailAddress;
    this.postalCode = postalCode;
    this.coordinates = coordinates;
  }

  public TrainerAddress withCompletedAddress(String numberAddress, String postalCode) {
    return new TrainerAddress(
            this.id,
            numberAddress,
            this.roadAddress,
            this.detailAddress,
            postalCode,
            this.coordinates
    );
  }

  public boolean matchesRoadAddress(String kakaoRoadAddress) {
    return this.roadAddress.equals(kakaoRoadAddress);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TrainerAddress that = (TrainerAddress) o;

    return Objects.equals(this.roadAddress, that.roadAddress) &&
            Objects.equals(this.detailAddress, that.detailAddress) &&
            this.coordinates.equals(that.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roadAddress, detailAddress, coordinates);
  }
}
