package com.demo.pteam.trainer.profile.service;

import com.demo.pteam.external.kakao.service.KakaoMapService;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.repository.TrainerAddressRepository;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.repository.TrainerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TrainerProfileService {

    private final TrainerProfileRepository trainerProfileRepository;
    private final TrainerAddressRepository trainerAddressRepository;
    private final KakaoMapService kakaoMapService;

    @Transactional
    public void createProfile(TrainerProfileRequest request, Long userId) {

        TrainerAddress newAddress = TrainerAddress.from(
                request.getAddress().getRoadAddress(),
                request.getAddress().getDetailAddress(),
                request.getAddress().getLatitude(),
                request.getAddress().getLongitude()
        );

        newAddress.verifyAndCompleteAddressWithKakao(kakaoMapService);

        TrainerAddress savedAddress = trainerAddressRepository.save(newAddress);

        trainerProfileRepository.save(
                TrainerProfile.of(
                        userId,
                        savedAddress.getId(),
                        request.getProfileImg(),
                        request.getIntro(),
                        request.getCredit(),
                        LocalTime.parse(request.getContactStartTime()),
                        LocalTime.parse(request.getContactEndTime()),
                        request.getIsNamePublic()
                )
        );
    }
}
