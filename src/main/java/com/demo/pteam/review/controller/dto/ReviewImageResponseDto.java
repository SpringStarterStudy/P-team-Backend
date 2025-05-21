package com.demo.pteam.review.controller.dto;

import com.demo.pteam.review.repository.entity.ReviewImageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 리뷰 이미지 정보가 담긴 응답 DTO
 * 클라이언트에게 리뷰 이미지 정보를 제공
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageResponseDto {

    private Long id;
    private String imageUrl;
    private Byte displayOrder; // 이미지 표시 순서
    // 이미지 파일 정보는 포함안함

    /**
     * 리뷰 이미지 엔티티를 DTO로 변환하는 생성자
     * @param entity 변환할 리뷰 이미지 엔티티
     */
    public ReviewImageResponseDto(ReviewImageEntity entity) {
        this.id = entity.getId();
        this.imageUrl = entity.getImageUrl();
        this.displayOrder = entity.getDisplayOrder();
    }
}
