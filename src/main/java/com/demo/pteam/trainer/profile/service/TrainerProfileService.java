package com.demo.pteam.trainer.profile.service;

import com.demo.pteam.external.kakao.dto.KakaoGeoResponse;
import com.demo.pteam.external.kakao.service.KakaoMapService;
import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.trainer.address.domain.Coordinates;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.exception.TrainerAddressErrorCode;
import com.demo.pteam.trainer.address.mapper.TrainerAddressMapper;
import com.demo.pteam.trainer.address.repository.TrainerAddressRepository;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.exception.TrainerProfileErrorCode;
import com.demo.pteam.trainer.profile.mapper.TrainerProfileMapper;
import com.demo.pteam.trainer.profile.repository.TrainerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainerProfileService {

  private final TrainerProfileRepository trainerProfileRepository;
  private final TrainerAddressRepository trainerAddressRepository;
  private final KakaoMapService kakaoMapService;

  /**
   * 트레이너 프로필 등록
   *
   * @param request 트레이너 프로필 요청 DTO
   * @param userId  로그인 사용자 id
   */
  public void createProfile(TrainerProfileRequest request, Long userId) {
    // TODO: '회원'이 아닌 '트레이너' 확인 여부

    TrainerAddress newAddress = TrainerAddressMapper.toDomain(request.getAddress());

    Coordinates coordinates = newAddress.getCoordinates();

    if (coordinates.isNull()) {
      throw new ApiException(TrainerAddressErrorCode.COORDINATES_NULL);
    }
    if (coordinates.isInvalidLatitude()) {
      throw new ApiException(TrainerAddressErrorCode.INVALID_LATITUDE);
    }
    if (coordinates.isInvalidLongitude()) {
      throw new ApiException(TrainerAddressErrorCode.INVALID_LONGITUDE);
    }

    KakaoGeoResponse response = kakaoMapService.requestCoordToAddress(
            newAddress.getCoordinates().getLatitude(),
            newAddress.getCoordinates().getLongitude()
    );
    KakaoGeoResponse.Document document = response.getDocuments().get(0);
    if (document.getRoadAddress() == null) {
      throw new ApiException(TrainerAddressErrorCode.ROAD_ADDRESS_NOT_FOUND);
    }
    if (!newAddress.matchesRoadAddress(document.getRoadAddress().getAddressName())) {
      throw new ApiException(TrainerAddressErrorCode.ADDRESS_COORDINATE_MISMATCH);
    }

    TrainerAddress completedAddress = newAddress.withCompletedAddress(
            document.getAddress().getAddressName(),
            document.getRoadAddress().getZoneNo()
    );

    TrainerAddress savedAddress = trainerAddressRepository.save(completedAddress);

    // name, nickname 임시
    TrainerProfile profile = TrainerProfileMapper.toDomain(request, userId, savedAddress.getId());

    if (profile.isInvalidContactTimePair()) {
      throw new ApiException(TrainerProfileErrorCode.INVALID_CONTACT_TIME_PAIR);
    }

    if (profile.isInvalidContactTimeRange()) {
      throw new ApiException(TrainerProfileErrorCode.INVALID_CONTACT_TIME_RANGE);
    }

    trainerProfileRepository.save(profile);
  }

  /**
   * 트레이너 프로필 삭제
   * @param userId 로그인 사용자 id
   */
  public void deleteProfile(Long userId) {
    TrainerProfile profile = trainerProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ApiException(TrainerProfileErrorCode.PROFILE_NOT_FOUND));

    trainerProfileRepository.delete(profile.getId());
  }

}
