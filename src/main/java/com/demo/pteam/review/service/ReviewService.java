package com.demo.pteam.review.service;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.review.controller.dto.ReviewCreateRequestDto;
import com.demo.pteam.review.controller.dto.ReviewResponseDto;
import com.demo.pteam.review.repository.ReviewImageRepository;
import com.demo.pteam.review.repository.ReviewRepository;
import com.demo.pteam.review.repository.entity.ReviewEntity;
import com.demo.pteam.review.repository.entity.ReviewImageEntity;
import com.demo.pteam.schedule.repository.ScheduleRepository;
import com.demo.pteam.schedule.repository.entity.ScheduleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final AccountRepository accountRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 리뷰를 생성하는 메서드
     * @param requestDto 리뷰 생성 요청 DTO
     * @param userId 현재 인증된 사용자 ID
     * @return 생성된 리뷰의 응답 DTO
     */
    @Transactional
    public ReviewResponseDto createReview(ReviewCreateRequestDto requestDto, Long userId) {
        // 엔티티 조회
        AccountEntity trainer = findTrainerById(requestDto.getTrainerId());
        AccountEntity user = findUserById(userId);
        ScheduleEntity schedule = findScheduleById(requestDto.getScheduleId());

        // 이미 리뷰를 작성했는지 확인
        boolean hasReview = reviewRepository.existsByUserAndSchedule(userId, requestDto.getScheduleId());
        if (hasReview) {
            throw new IllegalStateException("해당 일정에 이미 리뷰를 작성했습니다.");
        }

        // PT 횟수 계산
        Integer ptSessionCount = calculatePtSessionCount(user, trainer, schedule);

        // 리뷰 생성
        ReviewEntity review = ReviewEntity.createReview(trainer, user, schedule,
                requestDto.getRating(), requestDto.getContent(), requestDto.getPtPurpose(), ptSessionCount);
        ReviewEntity savedReview = reviewRepository.save(review);

        // 이미지 연결 처리
        List<ReviewImageEntity> reviewImages = connectImagesToReview(requestDto.getImageIds(), savedReview);

        // 응답 DTO 생성 및 반환
        return new ReviewResponseDto(savedReview, reviewImages);
    }


    // 메서드

    // 이미지 연결
    private List<ReviewImageEntity> connectImagesToReview(List<Long> imageIds, ReviewEntity review) {
        if (imageIds == null || imageIds.isEmpty()) {
            return List.of();
        }

        return imageIds.stream()
                .map(imageId -> {
                    ReviewImageEntity image = reviewImageRepository.findById(imageId)
                            .orElseThrow(() -> new EntityNotFoundException("이미지를 찾을 수 없습니다. ID: " + imageId));

                    // 이미지가 이미 리뷰와 연결되어 있는지 확인
                    if (image.getReview() != null && !image.getReview().equals(review)) {
                        throw new IllegalStateException("이미지가 이미 다른 리뷰에 연결되어 있습니다: " + imageId);
                    }

                    image.updateReview(review);

                    return reviewImageRepository.save(image);
                })
                .collect(Collectors.toList());
    }

    // PT 횟수 계산
    private Integer calculatePtSessionCount(AccountEntity user, AccountEntity trainer, ScheduleEntity schedule) {
        // 해당 사용자와 트레이너의 현재까지 완료된 PT수 계산
        LocalDateTime now = LocalDateTime.now();
        List<ScheduleEntity> completedSchedules = scheduleRepository.findCompletedPtByUserAndTrainer(user, trainer, now);
        boolean includeCurrentSchedule = schedule.getEndTime().isBefore(now);
        return completedSchedules.size() + (includeCurrentSchedule ? 1 : 0); // 이 스케줄이 완료된 PT라면 +1회, 아니면 0회
    }

    // ID로 일정 조회
    private ScheduleEntity findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));
    }

    // ID로 사용자 조회
    private AccountEntity findUserById(Long userId) {
        return accountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // ID로 트레이너 조회
    private AccountEntity findTrainerById(Long trainerId) {
        return accountRepository.findById(trainerId)
                .orElseThrow(() -> new EntityNotFoundException("트레이너를 찾을 수 없습니다."));
    }
}
