package com.demo.pteam.favorite.repository.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(name = "uk_favorites_user_profile",
        columnNames = {"user_id", "profile_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;

    // TODO: 회원, 프로필 관계 설정

    // TODO: Auditing 적용
}