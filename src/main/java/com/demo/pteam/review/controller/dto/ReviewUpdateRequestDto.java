package com.demo.pteam.review.controller.dto;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.review.exception.ReviewErrorCode;
import com.demo.pteam.review.repository.type.PtPurpose;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequestDto {
    @Size(min = 20, max = 1000, message = "리뷰 내용은 20자 이상 1000자 이하여야 합니다.")
    private String content;

    @DecimalMin(value = "0.0", message = "평점은 0.0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "평점은 5.0 이하이어야 합니다.")
    private BigDecimal rating;

    private PtPurpose ptPurpose;
    private List<Long> imageIds;

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
