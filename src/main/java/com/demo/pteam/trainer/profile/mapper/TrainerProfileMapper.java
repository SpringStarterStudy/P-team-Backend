package com.demo.pteam.trainer.profile.mapper;


import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;

public class TrainerProfileMapper {

  // 프로필 도메인 -> 프로필 엔티티 변환
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
}
