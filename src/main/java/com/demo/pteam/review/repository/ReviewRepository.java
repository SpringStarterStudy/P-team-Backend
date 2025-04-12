package com.demo.pteam.review.repository;

import com.demo.pteam.review.repository.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 리뷰 생성
    // 기본 save() 메서드

    // 리뷰 조회

    // 리뷰 수정

    // 리뷰 삭제
}
