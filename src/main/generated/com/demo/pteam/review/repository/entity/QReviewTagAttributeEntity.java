package com.demo.pteam.review.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewTagAttributeEntity is a Querydsl query type for ReviewTagAttributeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewTagAttributeEntity extends EntityPathBase<ReviewTagAttributeEntity> {

    private static final long serialVersionUID = -1410687237L;

    public static final QReviewTagAttributeEntity reviewTagAttributeEntity = new QReviewTagAttributeEntity("reviewTagAttributeEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public QReviewTagAttributeEntity(String variable) {
        super(ReviewTagAttributeEntity.class, forVariable(variable));
    }

    public QReviewTagAttributeEntity(Path<? extends ReviewTagAttributeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewTagAttributeEntity(PathMetadata metadata) {
        super(ReviewTagAttributeEntity.class, metadata);
    }

}

