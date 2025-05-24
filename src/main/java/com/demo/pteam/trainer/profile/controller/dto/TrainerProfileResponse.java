package com.demo.pteam.trainer.profile.controller.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileResponse {
  private Long profileId;
  private String displayName;
  private String intro;
  private Integer credit;
  private LocalTime contactStartTime;
  private LocalTime contactEndTime;
  private String profileImg;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Address address;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Address {
    private String roadAddress;
    private String detailAddress;
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
  }

}

