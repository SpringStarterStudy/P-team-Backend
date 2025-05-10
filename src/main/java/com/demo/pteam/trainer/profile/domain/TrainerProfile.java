package com.demo.pteam.trainer.profile.domain;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.trainer.profile.exception.TrainerProfileErrorCode;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TrainerProfile {

    private final Long id;
    private final Long userId;
    private final Long addressId;
    private final String profileImg;
    private final String intro;
    private final Integer credit;
    private final LocalTime contactStartTime;
    private final LocalTime contactEndTime;
    private final Boolean isNamePublic;

    public TrainerProfile(Long id, Long userId, Long addressId, String profileImg, String intro, Integer credit,
                          LocalTime contactStartTime, LocalTime contactEndTime, Boolean isNamePublic) {
        this.id = id;
        this.userId = userId;
        this.addressId = addressId;
        this.profileImg = profileImg;
        this.intro = intro;
        this.credit = credit;
        this.contactStartTime = contactStartTime;
        this.contactEndTime = contactEndTime;
        this.isNamePublic = isNamePublic;
        validate();
    }

    public static TrainerProfile of(Long userId, Long addressId, String profileImg, String intro, Integer credit,
                                    LocalTime contactStartTime, LocalTime contactEndTime, Boolean isNamePublic) {
        return new TrainerProfile(null, userId, addressId, profileImg, intro, credit,
                contactStartTime, contactEndTime, isNamePublic);
    }

    private void validate() {
        if (userId == null) {
            throw new ApiException(TrainerProfileErrorCode.PROFILE_USER_ID_NULL);
        }
        if (isNamePublic == null) {
            throw new ApiException(TrainerProfileErrorCode.PROFILE_NAME_PUBLIC_NULL);
        }
    }

    public void validateContactTime() {
        if ((contactStartTime == null && contactEndTime != null) ||
                (contactStartTime != null && contactEndTime == null)) {
            throw new ApiException(TrainerProfileErrorCode.INVALID_CONTACT_TIME_PAIR);
        }

        if (contactStartTime != null && endTimeAfterStart()) {
            throw new ApiException(TrainerProfileErrorCode.INVALID_CONTACT_TIME_RANGE);
        }
    }

    private boolean endTimeAfterStart() {
        return contactStartTime.isAfter(contactEndTime);
    }

}
