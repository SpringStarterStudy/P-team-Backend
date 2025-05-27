package com.demo.pteam.trainer.address.repository;

import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;

public interface TrainerAddressRepository {
    TrainerAddress save(TrainerAddress address);
    TrainerAddressEntity saveForEntityReference(TrainerAddress address);
}