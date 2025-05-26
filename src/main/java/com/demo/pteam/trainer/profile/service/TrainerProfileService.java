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
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileResponse;
import com.demo.pteam.trainer.profile.domain.TrainerProfile;
import com.demo.pteam.trainer.profile.exception.TrainerProfileErrorCode;
import com.demo.pteam.trainer.profile.mapper.TrainerProfileMapper;
import com.demo.pteam.trainer.profile.repository.TrainerProfileRepository;
import com.demo.pteam.trainer.profile.repository.entity.TrainerProfileEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainerProfileService {

  private final TrainerProfileRepository trainerProfileRepository;
  private final TrainerAddressRepository trainerAddressRepository;
  private final KakaoMapService kakaoMapService;

  /**
   * 트레이너 프로필 등록
   * @param request 트레이너 프로필 DTO
   * @param userId 로그인 사용자 id
   */
  public void createProfile(TrainerProfileRequest request, Long userId) {
    // TODO: '회원'이 아닌 '트레이너' 확인 여부
    log.info("📌 profileImg = {}", request.getProfileImg());
    log.info("📌 intro = {}", request.getIntro());
    log.info("📌 credit = {}", request.getCredit());
    log.info("📌 contactStartTime = {}", request.getContactStartTime());
    log.info("📌 contactEndTime = {}", request.getContactEndTime());
    log.info("📌 isNamePublic = {}", request.getIsNamePublic());

    TrainerProfileRequest.Address addr = request.getAddress();
    log.info("📍 address.roadAddress = {}", addr.getRoadAddress());
    log.info("📍 address.detailAddress = {}", addr.getDetailAddress());
    log.info("📍 address.latitude = {}", addr.getLatitude());
    log.info("📍 address.longitude = {}", addr.getLongitude());

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

    // name, nickname 임시 null
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
   * 트레이너 프로필 조회 API (사용자 본인)
   * @return 트레이너 프로필 응답 DTO
   */
  @Transactional(readOnly = true)
  public TrainerProfileResponse getProfile(Long userId) {
    // TODO: '회원'이 아닌 '트레이너' 확인 여부

    // 로그인한 사용자의 프로필이 있는지 여부
    TrainerProfileEntity entity = trainerProfileRepository.findEntityByUserId(userId)
            .orElseThrow(() -> new ApiException(TrainerProfileErrorCode.PROFILE_NOT_FOUND));

    TrainerProfile profile = TrainerProfileMapper.toDomain(entity);

    TrainerAddress address = trainerAddressRepository.findById(profile.getAddressId())
            .orElseThrow(() -> new ApiException(TrainerAddressErrorCode.ADDRESS_NOT_FOUND));

    return TrainerProfileMapper.toResponse(profile, address, entity.getCreatedAt(), entity.getUpdatedAt());
  }

}
