package com.demo.pteam.review.repository;

import com.demo.pteam.review.repository.entity.ReviewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImageEntity, Long> {

    // 이미지 생성
    // 기본 save() 메서드

}
