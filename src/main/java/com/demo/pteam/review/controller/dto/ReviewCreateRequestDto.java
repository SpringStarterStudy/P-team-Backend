package com.demo.pteam.review.controller.dto;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.review.domain.ReviewDomain;
import com.demo.pteam.review.exception.ReviewErrorCode;
import com.demo.pteam.review.repository.type.PtPurpose;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 리뷰 생성 요청을 위한 DTO
 * 클라이언트로부터 입력 받은 리뷰 생성 정보
 */

@Getter @Setter
@NoArgsConstructor
public class ReviewCreateRequestDto {

    @NotNull(message = "트레이너 ID는 필수입니다.")
    private Long trainerId;

    @NotNull(message = "일정 ID는 필수입니다.")
    private Long scheduleId;

    @NotNull(message = "평점을 필수입니다.")
    @DecimalMin(value = "0.0", message = "평점은 0.0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "평점은 5.0 이하이어야 합니다.")
    private BigDecimal rating;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    @Size(min = 20, max = 1000, message = "리뷰 내용은 20자 이상 1000자 이하여야 합니다.")
    private String content;

    @NotNull(message = "PT 목적은 필수입니다.")
    private PtPurpose ptPurpose;

    @Valid // 리스트 내부에 유효성 검사 활성화
    private List<@NotNull @Positive(message = "이미지 ID는 양수여야 합니다.") Long> imageIds; // 이미지 ID 목록

    // 기타 유효성 검증
    public void validateFormat() {
        if (imageIds != null) {
            if (imageIds.stream().anyMatch(Objects::isNull)) {
                throw new ApiException(ReviewErrorCode.IMAGE_ID_NULL);
            }

            if (imageIds.size() > 3) {
                throw new ApiException(ReviewErrorCode.IMAGE_COUNT_EXCEEDED);
            }
        }
    }
}
