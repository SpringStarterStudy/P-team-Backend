package com.demo.pteam.payment.repository;

import com.demo.pteam.payment.repository.entity.ExternalPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalPaymentRepository extends JpaRepository<ExternalPaymentEntity, Long> {
}
