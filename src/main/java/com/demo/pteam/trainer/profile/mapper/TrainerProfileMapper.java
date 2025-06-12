package com.demo.pteam.trainer.profile.mapper;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;

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

  // 프로필 요청 DTO -> 프로필 도메인
  public static TrainerProfile toDomain(TrainerProfileRequest dto, Long userId, Long addressId) {
    return new TrainerProfile(
            null,
            userId,
            null,
            null,
            addressId,
            dto.getProfileImg(),
            dto.getIntro(),
            dto.getCredit(),
            dto.getContactStartTime(),
            dto.getContactEndTime(),
            dto.getIsNamePublic()
    );
  }

}
