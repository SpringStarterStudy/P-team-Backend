package com.demo.pteam.review.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewTagEntity is a Querydsl query type for ReviewTagEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewTagEntity extends EntityPathBase<ReviewTagEntity> {

    private static final long serialVersionUID = -1477631225L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewTagEntity reviewTagEntity = new QReviewTagEntity("reviewTagEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReviewEntity review;

    public final QReviewTagAttributeEntity tag;

    public QReviewTagEntity(String variable) {
        this(ReviewTagEntity.class, forVariable(variable), INITS);
    }

    public QReviewTagEntity(Path<? extends ReviewTagEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewTagEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewTagEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewTagEntity.class, metadata, inits);
    }

    public QReviewTagEntity(Class<? extends ReviewTagEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReviewEntity(forProperty("review"), inits.get("review")) : null;
        this.tag = inits.isInitialized("tag") ? new QReviewTagAttributeEntity(forProperty("tag")) : null;
    }

}

