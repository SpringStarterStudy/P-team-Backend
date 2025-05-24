package com.demo.pteam.trainer.address.repository;

import com.demo.pteam.trainer.address.domain.TrainerAddress;
import java.util.Optional;

public interface TrainerAddressRepository {
    TrainerAddress save(TrainerAddress address);
    Optional<TrainerAddress> findById(Long addressId);
}