package com.demo.pteam.trainer.address.repository;

import com.demo.pteam.trainer.address.domain.Coordinates;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.mapper.TrainerAddressMapper;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainerAddressRepositoryImpl implements TrainerAddressRepository {

  private final TrainerAddressJPARepository jpaRepository;

  @Override
  public TrainerAddress save(TrainerAddress address) {
    TrainerAddressEntity entity = TrainerAddressMapper.toEntity(address);
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

  @Override
  public Optional<TrainerAddress> findById(Long addressId) {
    return jpaRepository.findById(addressId)
            .map(entity -> new TrainerAddress(
                    entity.getId(),
                    entity.getNumberAddress(),
                    entity.getRoadAddress(),
                    entity.getDetailAddress(),
                    entity.getPostalCode(),
                    new Coordinates(entity.getLatitude(), entity.getLongitude())
            ));
  }

}