package com.demo.pteam.review.domain;

import com.demo.pteam.review.repository.type.PtPurpose;
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
}
