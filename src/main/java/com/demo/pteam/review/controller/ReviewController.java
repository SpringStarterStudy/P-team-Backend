package com.demo.pteam.review.controller;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.review.controller.dto.ReviewCreateRequestDto;
import com.demo.pteam.review.controller.dto.ReviewResponseDto;
import com.demo.pteam.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 생성 API
     * @param requestDto 리뷰 생성 요청 DTO
     * @param userDetails 인증된 유저 정보
     * @return 생성된 리뷰 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(@Valid @RequestBody ReviewCreateRequestDto requestDto,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {

        // 추가 검증
        requestDto.validateFormat();

        // TODO
        // 현재 인증된 사용자 ID 가져오기
//        Long userId = ((CustomUserDetails) userDetails).getId();
        Long userId = null;

        ReviewResponseDto responseDto = reviewService.createReview(requestDto, userId);

        ApiResponse<ReviewResponseDto> apiResponse = ApiResponse.success("리뷰가 성공적으로 생성되었습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

    }
}
