package com.demo.pteam.review.controller.dto;

import com.demo.pteam.review.repository.entity.ReviewEntity;
import com.demo.pteam.review.repository.entity.ReviewImageEntity;
import com.demo.pteam.review.repository.type.PtPurpose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 정보 응답 DTO
 * 클라이언트에게 리뷰 상세 정보를 제공
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private Long trainerId;
    private String trainerName;
    private Long userId;
    private String userName;
    private Long scheduleId;
    private BigDecimal rating;
    private String content;
    private PtPurpose ptPurpose;
    private Integer ptSessionCount;
    private LocalDateTime createdAt;
    private List<ReviewImageResponseDto> images;

    /**
     * 리뷰 엔티티와 리뷰 이미지 엔티티를 DTO로 변환하는 생성자
     * @param entity 변환할 리뷰 엔티티
     * @param reviewImages 리뷰 이미지 엔티티 목록
     */
    public ReviewResponseDto(ReviewEntity entity, List<ReviewImageEntity> reviewImages) {
        this.id = entity.getId();
        this.trainerId = entity.getTrainer().getId();
        this.trainerName = entity.getUser().getName();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getName();
        this.scheduleId = entity.getSchedule().getId();
        this.rating = entity.getRating();
        this.content = entity.getContent();
        this.ptPurpose = entity.getPtPurpose();
        this.ptSessionCount = entity.getPtSessionCount();
        this.createdAt = entity.getCreatedAt();

        this.images = reviewImages.stream()
                .filter(ReviewImageEntity::getIsActive)
                .map(ReviewImageResponseDto::new)
                .collect(Collectors.toList());
    }
}
