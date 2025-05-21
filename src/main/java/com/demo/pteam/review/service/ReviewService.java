package com.demo.pteam.review.service;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.review.controller.dto.ReviewCreateRequestDto;
import com.demo.pteam.review.controller.dto.ReviewResponseDto;
import com.demo.pteam.review.domain.ReviewDomain;
import com.demo.pteam.review.exception.ReviewErrorCode;
import com.demo.pteam.review.mapper.ReviewMapper;
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
//    private final AccountRepository accountRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReviewMapper reviewMapper;

    /**
     * 리뷰를 생성하는 메서드
     * @param requestDto 리뷰 생성 요청 DTO
     * @param userId 현재 인증된 사용자 ID
     * @return 생성된 리뷰의 응답 DTO
     */
    @Transactional
    public ReviewResponseDto createReview(ReviewCreateRequestDto requestDto, Long userId) {
        // 엔티티 조회
        AccountEntity user = findUserById(userId);
        AccountEntity trainer = findTrainerById(requestDto.getTrainerId());
        ScheduleEntity schedule = findScheduleById(requestDto.getScheduleId());

        // 중복 리뷰 확인
        boolean hasReview = reviewRepository.existsByUserAndSchedule(userId, requestDto.getScheduleId());
        if (hasReview) {
            throw new ApiException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }

        // PT 횟수 계산
        Integer ptSessionCount = calculatePtSessionCount(user, trainer, schedule);

        // Domain 객체 생성
        ReviewDomain reviewDomain = reviewMapper.fromRequestDto(requestDto, ptSessionCount, userId);

        // PT 완료 여부 확인
        if (!reviewDomain.isCompletedSchedule(schedule.getEndTime())) {
            throw new ApiException(ReviewErrorCode.PT_NOT_COMPLETED);
        }

        // 리뷰 생성
        ReviewEntity review = reviewMapper.toEntity(reviewDomain, trainer, user, schedule);
        ReviewEntity savedReview = reviewRepository.save(review);

        // TODO: 크레딧 서비스 코드 구현 확인 후 수정
        // 첫 리뷰 작성 시 5크레딧 지급
        boolean isFirstReview = reviewRepository.countByUserId(userId) == 0;
        if (isFirstReview) {
//            creditService.addCredits(userId, 5, "첫 리뷰 작성 보너스!");
        }

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
                            .orElseThrow(() -> new ApiException(ReviewErrorCode.IMAGE_NOT_FOUND));

                    // 이미지가 이미 리뷰와 연결되어 있는지 확인
                    if (image.getReview() != null && !image.getReview().equals(review)) {
                        throw new ApiException(ReviewErrorCode.IMAGE_ALREADY_LINKED);
                    }

                    image.updateReview(review);

                    return reviewImageRepository.save(image);
                })
                .collect(Collectors.toList());
    }

    // TODO: 운동 일정 구현 코드 확인 후 수정 예정
    // PT 횟수 계산
    private Integer calculatePtSessionCount(AccountEntity user, AccountEntity trainer, ScheduleEntity schedule) {
        // 해당 사용자와 트레이너의 현재까지 완료된 PT수 계산
//        LocalDateTime now = LocalDateTime.now();
//        List<ScheduleEntity> completedSchedules = scheduleRepository.findCompletedPtByUserAndTrainer(user, trainer, now);
//        boolean includeCurrentSchedule = schedule.getEndTime().isBefore(now);
//        return completedSchedules.size() + (includeCurrentSchedule ? 1 : 0); // 이 스케줄이 완료된 PT라면 +1회, 아니면 0회
          return 0;
    }

    // ID로 일정 조회
    private ScheduleEntity findScheduleById(Long scheduleId) {
//        return scheduleRepository.findById(scheduleId)
//                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));
          return null;
    }

    // ID로 사용자 조회
    private AccountEntity findUserById(Long userId) {
//        return accountRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
          return null;
    }

    // ID로 트레이너 조회
    private AccountEntity findTrainerById(Long trainerId) {
//        return accountRepository.findById(trainerId)
//                .orElseThrow(() -> new EntityNotFoundException("트레이너를 찾을 수 없습니다."));
          return null;
    }
}
