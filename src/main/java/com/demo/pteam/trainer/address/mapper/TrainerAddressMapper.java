package com.demo.pteam.trainer.address.mapper;

import com.demo.pteam.trainer.address.domain.Coordinates;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import com.demo.pteam.trainer.profile.controller.dto.TrainerProfileRequest;

public class TrainerAddressMapper {

  // 요청 DTO -> 도메인 변환
  public static TrainerAddress toDomain(TrainerProfileRequest.Address dto) {
    Coordinates coordinates = new Coordinates(dto.getLatitude(), dto.getLongitude());
    return new TrainerAddress(
            null,
            null,
            dto.getRoadAddress(),
            dto.getDetailAddress(),
            null,
            coordinates
    );
  }

  // 엔티티 -> 도메인 변환
  public static TrainerAddress toDomain(TrainerAddressEntity entity) {
    Coordinates coordinates = new Coordinates(entity.getLatitude(), entity.getLongitude());
    return new TrainerAddress(
            entity.getId(),
            entity.getNumberAddress(),
            entity.getRoadAddress(),
            entity.getDetailAddress(),
            entity.getPostalCode(),
            coordinates
    );
  }

  // 도메인 -> 엔티티 변환
  public static TrainerAddressEntity toEntity(TrainerAddress address) {
    Coordinates coordinates = address.getCoordinates();

    return TrainerAddressEntity.builder()
            .numberAddress(address.getNumberAddress())
            .roadAddress(address.getRoadAddress())
            .detailAddress(address.getDetailAddress())
            .postalCode(address.getPostalCode())
            .latitude(coordinates.getLatitude())
            .longitude(coordinates.getLongitude())
            .build();
  }

}