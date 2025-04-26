package com.demo.pteam.review.repository;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.review.repository.entity.ReviewEntity;
import com.demo.pteam.schedule.repository.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {

    // 리뷰 생성
    ReviewEntity save(ReviewEntity review);

    // 사용자와 스케줄 ID로 리뷰 존재 여부 확인
    boolean existsByUserAndSchedule(Long userId, Long scheduleId);

    // 사용자가 작성한 리뷰 수를 반환
    long countByUserId(Long userId);

    // 리뷰 조회

    // 리뷰 수정

    // 리뷰 삭제
}
