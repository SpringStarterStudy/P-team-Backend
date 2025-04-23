package com.demo.pteam.review.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewReplyEntity is a Querydsl query type for ReviewReplyEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewReplyEntity extends EntityPathBase<ReviewReplyEntity> {

    private static final long serialVersionUID = 137996247L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewReplyEntity reviewReplyEntity = new QReviewReplyEntity("reviewReplyEntity");

    public final com.demo.pteam.global.entity.QBaseEntity _super = new com.demo.pteam.global.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReviewEntity review;

    public final com.demo.pteam.authentication.repository.entity.QAccountEntity trainer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewReplyEntity(String variable) {
        this(ReviewReplyEntity.class, forVariable(variable), INITS);
    }

    public QReviewReplyEntity(Path<? extends ReviewReplyEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewReplyEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewReplyEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewReplyEntity.class, metadata, inits);
    }

    public QReviewReplyEntity(Class<? extends ReviewReplyEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReviewEntity(forProperty("review"), inits.get("review")) : null;
        this.trainer = inits.isInitialized("trainer") ? new com.demo.pteam.authentication.repository.entity.QAccountEntity(forProperty("trainer")) : null;
    }

}

