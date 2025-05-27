package com.demo.pteam.trainer.profile.service;

import com.demo.pteam.external.kakao.dto.KakaoGeoResponse;
import com.demo.pteam.external.kakao.service.KakaoMapService;
import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.trainer.address.domain.Coordinates;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.exception.TrainerAddressErrorCode;
import com.demo.pteam.trainer.address.mapper.TrainerAddressMapper;
import com.demo.pteam.trainer.address.repository.TrainerAddressRepository;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.exception.TrainerProfileErrorCode;
import com.demo.pteam.trainer.profile.mapper.TrainerProfileMapper;
import com.demo.pteam.trainer.profile.repository.TrainerProfileRepository;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;
import jakarta.validation.Valid;
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
   * @param request 트레이너 프로필 요청 DTO
   * @param userId 로그인 사용자 id
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
   * 트레이너 프로필 수정
   * @param request 트레이너 프로필 요청 DTO
   * @param userId 로그인 사용자 id
   */
  public void updateProfile(@Valid TrainerProfileRequest request, Long userId) {
    TrainerProfileEntity entity = trainerProfileRepository.findEntityByUserId(userId)
            .orElseThrow(() -> new ApiException(TrainerProfileErrorCode.PROFILE_NOT_FOUND));

    // DB에 저장되어 있는 주소와 요청 주소가 같은지 확인
    TrainerAddress currentAddress = TrainerAddressMapper.toDomain(entity.getAddress()); // 기존 주소
    TrainerAddress newAddress = TrainerAddressMapper.toDomain(request.getAddress()); // 요청 주소

    TrainerAddressEntity newAddressEntity = entity.getAddress();

    boolean isAddressChanged = !newAddress.equals(currentAddress);

    if (isAddressChanged) {
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

      newAddressEntity = trainerAddressRepository.saveForEntityReference(completedAddress); // 영속 상태 반환
    }

    // 나머지 트레이너 프로필 값 업데이트
    entity.updateIntro(request.getIntro());
    entity.updateProfileImg(request.getProfileImg());
    entity.updateCredit(request.getCredit());
    entity.updateContactTime(request.getContactStartTime(), request.getContactEndTime());
    entity.updateNamePublic(request.getIsNamePublic());

    if (isAddressChanged) {
      entity.updateAddress(newAddressEntity);
    }

    TrainerProfile domain = TrainerProfileMapper.toDomain(entity);

    if (domain.isInvalidContactTimePair()) {
      throw new ApiException(TrainerProfileErrorCode.INVALID_CONTACT_TIME_PAIR);
    }

    if (domain.isInvalidContactTimeRange()) {
      throw new ApiException(TrainerProfileErrorCode.INVALID_CONTACT_TIME_RANGE);
    }

  }

}
