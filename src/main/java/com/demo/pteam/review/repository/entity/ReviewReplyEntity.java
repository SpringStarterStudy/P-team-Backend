package com.demo.pteam.review.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_reply")
@Getter
@NoArgsConstructor
public class ReviewReplyEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private AccountEntity trainer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
