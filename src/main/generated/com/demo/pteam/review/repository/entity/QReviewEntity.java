package com.demo.pteam.review.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewEntity is a Querydsl query type for ReviewEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewEntity extends EntityPathBase<ReviewEntity> {

    private static final long serialVersionUID = 817900857L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewEntity reviewEntity = new QReviewEntity("reviewEntity");

    public final com.demo.pteam.global.entity.QBaseEntity _super = new com.demo.pteam.global.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.demo.pteam.review.repository.type.PtPurpose> ptPurpose = createEnum("ptPurpose", com.demo.pteam.review.repository.type.PtPurpose.class);

    public final NumberPath<Integer> ptSessionCount = createNumber("ptSessionCount", Integer.class);

    public final NumberPath<java.math.BigDecimal> rating = createNumber("rating", java.math.BigDecimal.class);

    public final com.demo.pteam.schedule.repository.entity.QScheduleEntity schedule;

    public final com.demo.pteam.authentication.repository.entity.QAccountEntity trainer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.demo.pteam.authentication.repository.entity.QAccountEntity user;

    public QReviewEntity(String variable) {
        this(ReviewEntity.class, forVariable(variable), INITS);
    }

    public QReviewEntity(Path<? extends ReviewEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewEntity.class, metadata, inits);
    }

    public QReviewEntity(Class<? extends ReviewEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.schedule = inits.isInitialized("schedule") ? new com.demo.pteam.schedule.repository.entity.QScheduleEntity(forProperty("schedule"), inits.get("schedule")) : null;
        this.trainer = inits.isInitialized("trainer") ? new com.demo.pteam.authentication.repository.entity.QAccountEntity(forProperty("trainer")) : null;
        this.user = inits.isInitialized("user") ? new com.demo.pteam.authentication.repository.entity.QAccountEntity(forProperty("user")) : null;
    }

}

