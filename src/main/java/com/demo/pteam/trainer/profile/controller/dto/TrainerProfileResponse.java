package com.demo.pteam.trainer.profile.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

  @JsonFormat(pattern = "HH:mm")
  private LocalTime contactStartTime;

  @JsonFormat(pattern = "HH:mm")
  private LocalTime contactEndTime;
  private String profileImg;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

