package com.demo.pteam.review.repository;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.review.repository.entity.ReviewEntity;
import com.demo.pteam.schedule.repository.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {
    boolean existsByUserAndSchedule(Long userId, Long scheduleId);
    long countByUserId(Long userId);
}
