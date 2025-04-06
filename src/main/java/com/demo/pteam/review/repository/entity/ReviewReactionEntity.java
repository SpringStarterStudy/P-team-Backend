package com.demo.pteam.review.repository.entity;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.global.entity.BaseEntity;
import com.demo.pteam.review.repository.type.Reaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_reaction", uniqueConstraints = {
        @UniqueConstraint(name = "uk_review_account", columnNames = {"review_id", "account_id"})
})
@Getter
@NoArgsConstructor
public class ReviewReactionEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_reaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction", nullable = false)
    private Reaction reaction;
}
