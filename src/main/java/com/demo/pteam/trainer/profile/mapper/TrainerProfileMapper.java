package com.demo.pteam.trainer.profile.mapper;


import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileResponse;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;

import java.time.LocalDateTime;

public class TrainerProfileMapper {

  // 프로필 도메인 -> 프로필 엔티티
  public static TrainerProfileEntity toEntity(
          TrainerProfile profile,
          AccountEntity trainer,
          TrainerAddressEntity address) {

    return TrainerProfileEntity.builder()
            .trainer(trainer)
            .address(address)
            .profileImg(profile.getProfileImg())
            .intro(profile.getIntro())
            .credit(profile.getCredit())
            .contactStartTime(profile.getContactStartTime())
            .contactEndTime(profile.getContactEndTime())
            .isNamePublic(profile.getIsNamePublic())
            .build();
  }

  // 프로필 엔티티 -> 프로필 도메인
  public static TrainerProfile toDomain(TrainerProfileEntity entity) {
    return new TrainerProfile(
            entity.getId(),
            entity.getTrainer().getId(),
            entity.getTrainer().getName(),
            entity.getTrainer().getNickname(),
            entity.getAddress().getId(),
            entity.getProfileImg(),
            entity.getIntro(),
            entity.getCredit(),
            entity.getContactStartTime(),
            entity.getContactEndTime(),
            entity.getIsNamePublic()
    );
  }

  // 프로필 도메인 -> 프로필 응답 DTO
  public static TrainerProfileResponse toResponse(TrainerProfile profile, TrainerAddress address, LocalDateTime createdAt, LocalDateTime updatedAt) {
    return TrainerProfileResponse.builder()
            .profileId(profile.getId())
            .displayName(profile.getDisplayName())
            .intro(profile.getIntro())
            .credit(profile.getCredit())
            .contactStartTime(profile.getContactStartTime())
            .contactEndTime(profile.getContactEndTime())
            .profileImg(profile.getProfileImg())
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .address(TrainerProfileResponse.Address.builder()
                    .roadAddress(address.getRoadAddress())
                    .detailAddress(address.getDetailAddress())
                    .postalCode(address.getPostalCode())
                    .latitude(address.getCoordinates().getLatitude())
                    .longitude(address.getCoordinates().getLongitude())
                    .build())
            .build();
  }
}
