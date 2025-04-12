package com.demo.pteam.review.controller.dto;

import com.demo.pteam.review.repository.type.PtPurpose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 리뷰 생성 요청을 위한 DTO
 * 클라이언트로부터 입력 받은 리뷰 생성 정보
 */

@Getter @Setter
@NoArgsConstructor
public class ReviewCreateRequestDto {

    private Long trainerId;
    private Long scheduleId;
    private BigDecimal rating;
    private String content;
    private PtPurpose ptPurpose;
    private List<Long> imageIds; // 이미지 ID 목록
}
