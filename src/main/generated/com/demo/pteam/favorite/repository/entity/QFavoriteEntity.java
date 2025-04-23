package com.demo.pteam.favorite.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFavoriteEntity is a Querydsl query type for FavoriteEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteEntity extends EntityPathBase<FavoriteEntity> {

    private static final long serialVersionUID = -1251664135L;

    public static final QFavoriteEntity favoriteEntity = new QFavoriteEntity("favoriteEntity");

    public final NumberPath<Long> favoriteId = createNumber("favoriteId", Long.class);

    public QFavoriteEntity(String variable) {
        super(FavoriteEntity.class, forVariable(variable));
    }

    public QFavoriteEntity(Path<? extends FavoriteEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFavoriteEntity(PathMetadata metadata) {
        super(FavoriteEntity.class, metadata);
    }

}

