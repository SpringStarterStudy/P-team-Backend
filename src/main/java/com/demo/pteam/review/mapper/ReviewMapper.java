package com.demo.pteam.review.mapper;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.review.controller.dto.ReviewCreateRequestDto;
import com.demo.pteam.review.domain.ReviewDomain;
import com.demo.pteam.review.repository.entity.ReviewEntity;
import com.demo.pteam.schedule.repository.entity.ScheduleEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    // Entity 변환 메서드
    public ReviewEntity toEntity(ReviewDomain domain, AccountEntity trainer, AccountEntity user, ScheduleEntity schedule) {
        return ReviewEntity.createReview(
                trainer,
                user,
                schedule,
                domain.getRating(),
                domain.getContent(),
                domain.getPtPurpose(),
                domain.getPtSessionCount()
        );
    }

    // 팩토리 메서드
    public ReviewDomain fromRequestDto(ReviewCreateRequestDto dto, Integer ptSessionCount, Long userId) {
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

    // Entity를 도메인 객체로 변환
    public ReviewDomain fromEntity(ReviewEntity entity) {
        return ReviewDomain.builder()
                .id(entity.getId())
                .trainerId(entity.getTrainer().getId())
                .userId(entity.getUser().getId())
                .scheduleId(entity.getSchedule().getId())
                .rating(entity.getRating())
                .content(entity.getContent())
                .ptPurpose(entity.getPtPurpose())
                .ptSessionCount(entity.getPtSessionCount())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
