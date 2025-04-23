package com.demo.pteam.review.repository.entity;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.global.entity.BaseEntity;
import com.demo.pteam.review.repository.type.PtPurpose;
import com.demo.pteam.schedule.repository.entity.ScheduleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReviewEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private AccountEntity trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AccountEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "pt_purpose")
    private PtPurpose ptPurpose;

    @Column(name = "pt_session_count", nullable = false)
    private Integer ptSessionCount;

    public static ReviewEntity createReview(AccountEntity trainer, AccountEntity user,
                                            ScheduleEntity schedule, BigDecimal rating,
                                            String content, PtPurpose ptPurpose, Integer ptSessionCount) {
        return ReviewEntity.builder()
                .trainer(trainer)
                .user(user)
                .schedule(schedule)
                .rating(rating)
                .content(content)
                .ptPurpose(ptPurpose)
                .ptSessionCount(ptSessionCount)
                .build();
    }
}
