package com.demo.pteam.review.repository;

import com.demo.pteam.review.repository.entity.ReviewEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    public ReviewRepositoryImpl(ReviewJpaRepository reviewJpaRepository) {
        this.reviewJpaRepository = reviewJpaRepository;
    }

    @Override
    public ReviewEntity save(ReviewEntity review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public boolean existsByUserAndSchedule(Long userId, Long scheduleId) {
        return reviewJpaRepository.existsByUserAndSchedule(userId, scheduleId);
    }
}
