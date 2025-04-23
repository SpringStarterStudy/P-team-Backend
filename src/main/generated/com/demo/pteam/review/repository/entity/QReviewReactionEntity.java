package com.demo.pteam.review.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewReactionEntity is a Querydsl query type for ReviewReactionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewReactionEntity extends EntityPathBase<ReviewReactionEntity> {

    private static final long serialVersionUID = 2129148610L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewReactionEntity reviewReactionEntity = new QReviewReactionEntity("reviewReactionEntity");

    public final com.demo.pteam.global.entity.QBaseEntity _super = new com.demo.pteam.global.entity.QBaseEntity(this);

    public final com.demo.pteam.authentication.repository.entity.QAccountEntity account;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.demo.pteam.review.repository.type.Reaction> reaction = createEnum("reaction", com.demo.pteam.review.repository.type.Reaction.class);

    public final QReviewEntity review;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewReactionEntity(String variable) {
        this(ReviewReactionEntity.class, forVariable(variable), INITS);
    }

    public QReviewReactionEntity(Path<? extends ReviewReactionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewReactionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewReactionEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewReactionEntity.class, metadata, inits);
    }

    public QReviewReactionEntity(Class<? extends ReviewReactionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.demo.pteam.authentication.repository.entity.QAccountEntity(forProperty("account")) : null;
        this.review = inits.isInitialized("review") ? new QReviewEntity(forProperty("review"), inits.get("review")) : null;
    }

}

