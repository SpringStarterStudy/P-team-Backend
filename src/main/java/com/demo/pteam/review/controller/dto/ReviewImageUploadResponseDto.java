package com.demo.pteam.review.controller.dto;

import com.demo.pteam.review.repository.entity.ReviewImageEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewImageUploadResponseDto {
    private Long id;
    private String imageUrl;
    private LocalDateTime createdAt;

    @Builder
    public ReviewImageUploadResponseDto(Long id, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public static ReviewImageUploadResponseDto from(ReviewImageEntity entity) {
        return ReviewImageUploadResponseDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
