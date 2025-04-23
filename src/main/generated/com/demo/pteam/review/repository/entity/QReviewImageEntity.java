package com.demo.pteam.review.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewImageEntity is a Querydsl query type for ReviewImageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewImageEntity extends EntityPathBase<ReviewImageEntity> {

    private static final long serialVersionUID = -1793727096L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewImageEntity reviewImageEntity = new QReviewImageEntity("reviewImageEntity");

    public final com.demo.pteam.global.entity.QBaseEntity _super = new com.demo.pteam.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Byte> displayOrder = createNumber("displayOrder", Byte.class);

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Integer> fileSize = createNumber("fileSize", Integer.class);

    public final StringPath fileType = createString("fileType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isActive = createBoolean("isActive");

    public final QReviewEntity review;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewImageEntity(String variable) {
        this(ReviewImageEntity.class, forVariable(variable), INITS);
    }

    public QReviewImageEntity(Path<? extends ReviewImageEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewImageEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewImageEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewImageEntity.class, metadata, inits);
    }

    public QReviewImageEntity(Class<? extends ReviewImageEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReviewEntity(forProperty("review"), inits.get("review")) : null;
    }

}

