package com.demo.pteam.trainer.profile.service;

import com.demo.pteam.external.kakao.dto.KakaoGeoResponse;
import com.demo.pteam.external.kakao.service.KakaoMapService;
import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.exception.TrainerAddressErrorCode;
import com.demo.pteam.trainer.address.mapper.TrainerAddressMapper;
import com.demo.pteam.trainer.address.repository.TrainerAddressRepository;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.repository.TrainerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainerProfileService {

  private final TrainerProfileRepository trainerProfileRepository;
  private final TrainerAddressRepository trainerAddressRepository;
  private final KakaoMapService kakaoMapService;

  @Transactional
  public void createProfile(TrainerProfileRequest request, Long userId) {

    TrainerAddress newAddress = TrainerAddressMapper.toDomain(request.getAddress());

    KakaoGeoResponse response = kakaoMapService.requestCoordToAddress(
            newAddress.getCoordinates().getLatitude(),
            newAddress.getCoordinates().getLongitude()
    );

    KakaoGeoResponse.Document document = response.getDocuments().get(0);

    if (document.getRoadAddress() == null) {
      throw new ApiException(TrainerAddressErrorCode.ROAD_ADDRESS_NOT_FOUND);
    }

    newAddress.validateMatchingRoadAddress(document.getRoadAddress().getAddressName());

    newAddress.completeAddress(
            document.getAddress().getAddressName(),
            document.getRoadAddress().getZoneNo()
    );

    TrainerAddress savedAddress = trainerAddressRepository.save(newAddress);

    TrainerProfile profile = TrainerProfile.of(
            userId,
            savedAddress.getId(),
            request.getProfileImg(),
            request.getIntro(),
            request.getCredit(),
            request.getContactStartTime(),
            request.getContactEndTime(),
            request.getIsNamePublic()
    );

    profile.validateContactTime();

    trainerProfileRepository.save(profile);
  }

}
