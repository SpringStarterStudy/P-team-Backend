package com.demo.pteam.review.domain;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.review.controller.dto.ReviewCreateRequestDto;
import com.demo.pteam.review.repository.entity.ReviewEntity;
import com.demo.pteam.review.repository.type.PtPurpose;
import com.demo.pteam.schedule.repository.entity.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
@AllArgsConstructor
public class ReviewDomain {
    private Long id;
    private Long trainerId;
    private Long userId;
    private Long scheduleId;
    private BigDecimal rating;
    private String content;
    private PtPurpose ptPurpose;
    private Integer ptSessionCount;
    private LocalDateTime createdAt;

    // 완료된 PT에 대해서만 리뷰 작성 가능
    public boolean isCompletedSchedule(LocalDateTime scheduleEndTime) {
        return scheduleEndTime.isBefore(LocalDateTime.now());
    }

    // 리뷰 작성 후 48시간 이내에만 수정 가능
    public boolean isEditable() {
        if (createdAt == null) return false;
        return ChronoUnit.HOURS.between(createdAt, LocalDateTime.now()) <= 48;
    }

    // 리뷰가 높은 평점인지 판단
    public boolean isHighRating() {
        return rating != null && rating.compareTo(new BigDecimal("4.0")) >= 0;
    }

    // Entity 변환 메서드
    public ReviewEntity toEntity(AccountEntity trainer, AccountEntity user, ScheduleEntity schedule) {
        return ReviewEntity.createReview(
                trainer,
                user,
                schedule,
                this.rating,
                this.content,
                this.ptPurpose,
                this.ptSessionCount
        );
    }

    // 팩토리 메서드
    public static ReviewDomain fromRequestDto(ReviewCreateRequestDto dto, Integer ptSessionCount, Long userId) {
        return ReviewDomain.builder()
                .trainerId(dto.getTrainerId())
                .userId(userId)
                .scheduleId(dto.getScheduleId())
                .rating(dto.getRating())
                .content(dto.getContent())
                .ptPurpose(dto.getPtPurpose())
                .ptSessionCount(ptSessionCount)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Entity로부터 Domain 객체 생성
    public static ReviewDomain fromEntity(ReviewEntity entity) {
        return ReviewDomain.builder()
                .id(entity.getId())
                .trainerId(entity.getTrainer().getId())
                .userId(entity.getUser().getId())
                .scheduleId(entity.getSchedule().getId())
                .rating(entity.getRating())
                .content(entity.getContent())
                .ptPurpose(entity.getPtPurpose())
                .ptSessionCount(entity.getPtSessionCount())
                .createdAt(entity.getCreatedAt()) // BaseEntity에서 상속받은 createdAt 필드
                .build();
    }
}
