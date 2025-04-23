package com.demo.pteam.schedule.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScheduleEntity is a Querydsl query type for ScheduleEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduleEntity extends EntityPathBase<ScheduleEntity> {

    private static final long serialVersionUID = 1447658009L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScheduleEntity scheduleEntity = new QScheduleEntity("scheduleEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final com.demo.pteam.authentication.repository.entity.QAccountEntity trainerAccountEntity;

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final com.demo.pteam.authentication.repository.entity.QAccountEntity userAccountEntity;

    public QScheduleEntity(String variable) {
        this(ScheduleEntity.class, forVariable(variable), INITS);
    }

    public QScheduleEntity(Path<? extends ScheduleEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScheduleEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScheduleEntity(PathMetadata metadata, PathInits inits) {
        this(ScheduleEntity.class, metadata, inits);
    }

    public QScheduleEntity(Class<? extends ScheduleEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.trainerAccountEntity = inits.isInitialized("trainerAccountEntity") ? new com.demo.pteam.authentication.repository.entity.QAccountEntity(forProperty("trainerAccountEntity")) : null;
        this.userAccountEntity = inits.isInitialized("userAccountEntity") ? new com.demo.pteam.authentication.repository.entity.QAccountEntity(forProperty("userAccountEntity")) : null;
    }

}

