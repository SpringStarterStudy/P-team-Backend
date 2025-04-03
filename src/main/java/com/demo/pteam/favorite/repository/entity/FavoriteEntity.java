package com.demo.pteam.favorite.repository.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorites")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteEntity {

    @EmbeddedId
    private FavoriteId favoritesId;

    // TODO: BaseEntity 상속

}