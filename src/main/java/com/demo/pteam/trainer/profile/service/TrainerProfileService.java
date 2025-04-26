package com.demo.pteam.trainer.profile.service;

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

    @Transactional
    public void createProfile(TrainerProfileRequest request, Long userId) {

        TrainerAddress savedAddress = trainerAddressRepository.save(
                TrainerAddress.of(
                        request.getAddress().getNumberAddress(),
                        request.getAddress().getStreetAddress(),
                        request.getAddress().getDetailAddress(),
                        request.getAddress().getPostalCode(),
                        request.getAddress().getLatitude(),
                        request.getAddress().getLongitude()
                )
        );


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
