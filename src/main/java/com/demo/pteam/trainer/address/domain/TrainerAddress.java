package com.demo.pteam.trainer.address.domain;

import lombok.Getter;

@Getter
public class TrainerAddress {

  private final Long id;
  private String numberAddress;
  private final String roadAddress;
  private final String detailAddress;
  private String postalCode;
  private Coordinates coordinates;

  public TrainerAddress(Long id, String numberAddress, String roadAddress, String detailAddress,
                        String postalCode, Coordinates coordinates) {
    this.id = id;
    this.numberAddress = numberAddress;
    this.roadAddress = roadAddress;
    this.detailAddress = detailAddress;
    this.postalCode = postalCode;
    this.coordinates = coordinates;
  }

  public static TrainerAddress from(String roadAddress, String detailAddress, Coordinates coordinates) {
    return new TrainerAddress(null, null, roadAddress, detailAddress, null, coordinates);
  }

  public void completeAddress(String numberAddress, String postalCode) {
    this.numberAddress = numberAddress;
    this.postalCode = postalCode;
  }

  public boolean matchesRoadAddress(String kakaoRoadAddress) {
    return this.roadAddress.equals(kakaoRoadAddress);
  }

}
