package com.demo.pteam.trainer.profile.domain;

import lombok.Getter;
import java.time.LocalTime;

@Getter
public class TrainerProfile {

  private final Long id;
  private final Long userId;
  private final String name;
  private final String nickname;
  private final Long addressId;
  private final String profileImg;
  private final String intro;
  private final Integer credit;
  private final LocalTime contactStartTime;
  private final LocalTime contactEndTime;
  private final Boolean isNamePublic;

  public TrainerProfile(Long id, Long userId, String name, String nickname, Long addressId,
                        String profileImg, String intro, Integer credit,
                        LocalTime contactStartTime, LocalTime contactEndTime, Boolean isNamePublic) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.nickname = nickname;
    this.addressId = addressId;
    this.profileImg = profileImg;
    this.intro = intro;
    this.credit = credit;
    this.contactStartTime = contactStartTime;
    this.contactEndTime = contactEndTime;
    this.isNamePublic = isNamePublic;
  }

  public static TrainerProfile of(Long userId, String name, String nickname, Long addressId, String profileImg,
                                  String intro, Integer credit,
                                  LocalTime contactStartTime, LocalTime contactEndTime, Boolean isNamePublic) {
    return new TrainerProfile(null, userId, name, nickname, addressId, profileImg, intro, credit,
            contactStartTime, contactEndTime, isNamePublic);
  }

  public String getDisplayName() {
    return isNamePublic ? name : nickname;
  }

  public boolean isContactTimePairValid() {
    return (contactStartTime == null && contactEndTime == null) ||
            (contactStartTime != null && contactEndTime != null);
  }

  public boolean hasContactTime() {
    return contactStartTime != null && contactEndTime != null;
  }

  public boolean isValidContatTimeRange() {
    return hasContactTime() && !contactStartTime.isAfter(contactEndTime);
  }

  public boolean isProfileComplete() {
    return userId != null && isNamePublic != null;
  }

}
