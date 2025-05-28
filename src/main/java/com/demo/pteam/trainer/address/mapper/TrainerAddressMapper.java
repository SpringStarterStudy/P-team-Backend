package com.demo.pteam.trainer.address.mapper;

import com.demo.pteam.trainer.address.domain.Coordinates;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
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

}
