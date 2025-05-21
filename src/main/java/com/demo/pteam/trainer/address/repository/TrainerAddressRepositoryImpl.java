package com.demo.pteam.trainer.address.repository;

import com.demo.pteam.trainer.address.domain.Coordinates;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrainerAddressRepositoryImpl implements TrainerAddressRepository {

  private final TrainerAddressJPARepository jpaRepository;

  @Override
  public TrainerAddress save(TrainerAddress address) {
    TrainerAddressEntity entity = TrainerAddressEntity.builder()
            .numberAddress(address.getNumberAddress())
            .roadAddress(address.getRoadAddress())
            .detailAddress(address.getDetailAddress())
            .postalCode(address.getPostalCode())
            .latitude(address.getCoordinates().getLatitude())
            .longitude(address.getCoordinates().getLongitude())
            .build();

    TrainerAddressEntity saved = jpaRepository.save(entity);

    Coordinates coordinates = new Coordinates(
            saved.getLatitude(),
            saved.getLongitude()
    );

    return new TrainerAddress(
            saved.getId(),
            saved.getNumberAddress(),
            saved.getRoadAddress(),
            saved.getDetailAddress(),
            saved.getPostalCode(),
            coordinates
    );
  }

}