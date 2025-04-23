package com.demo.pteam.trainer.address.repository;

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
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();

        TrainerAddressEntity saved = jpaRepository.save(entity);

        return new TrainerAddress(
                saved.getId(),
                saved.getNumberAddress(),
                saved.getRoadAddress(),
                saved.getDetailAddress(),
                saved.getPostalCode(),
                saved.getLatitude(),
                saved.getLongitude()
        );
    }

}