package com.demo.pteam.review.repository;

import com.demo.pteam.review.repository.entity.ReviewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImageEntity, Long> {

    // 이미지 생성
    // 기본 save() 메서드

    // 이미지 조회
    List<ReviewImageEntity> findByReviewId(Long reviewId);

}
