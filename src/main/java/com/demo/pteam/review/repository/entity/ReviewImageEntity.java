package com.demo.pteam.review.repository.entity;

import com.demo.pteam.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review_image")
@AttributeOverrides({
        @AttributeOverride(name = "createdAt", column = @Column(name = "upload_date"))
})
@Getter
@NoArgsConstructor
public class ReviewImageEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "display_order", nullable = false)
    private Byte displayOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // 1: 활성화, 0: 비활성화 (기본값 1)

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_type", length = 30)
    private String fileType;

    @Column(name = "file_size")
    private Integer fileSize;
}
