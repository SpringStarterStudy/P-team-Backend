package com.demo.pteam.trainer.address.repository;

import com.demo.pteam.trainer.address.repository.entity.TrainerAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerAddressJPARepository extends JpaRepository<TrainerAddressEntity, Long> {

}
