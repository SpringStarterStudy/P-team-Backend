package com.demo.pteam.payment.repository;

import com.demo.pteam.payment.repository.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
