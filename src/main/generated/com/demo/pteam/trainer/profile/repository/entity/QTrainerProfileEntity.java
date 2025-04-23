package com.demo.pteam.trainer.profile.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTrainerProfileEntity is a Querydsl query type for TrainerProfileEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrainerProfileEntity extends EntityPathBase<TrainerProfileEntity> {

    private static final long serialVersionUID = 384222811L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTrainerProfileEntity trainerProfileEntity = new QTrainerProfileEntity("trainerProfileEntity");

    public final com.demo.pteam.global.entity.QSoftDeletableEntity _super = new com.demo.pteam.global.entity.QSoftDeletableEntity(this);

    public final com.demo.pteam.trainer.address.repository.entity.QTrainerAddressEntity address;

    public final TimePath<java.time.LocalTime> contactEndTime = createTime("contactEndTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> contactStartTime = createTime("contactStartTime", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> credit = createNumber("credit", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath intro = createString("intro");

    public final BooleanPath isNamePublic = createBoolean("isNamePublic");

    public final StringPath profileImg = createString("profileImg");

    public final com.demo.pteam.authentication.repository.entity.QAccountEntity trainer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTrainerProfileEntity(String variable) {
        this(TrainerProfileEntity.class, forVariable(variable), INITS);
    }

    public QTrainerProfileEntity(Path<? extends TrainerProfileEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTrainerProfileEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTrainerProfileEntity(PathMetadata metadata, PathInits inits) {
        this(TrainerProfileEntity.class, metadata, inits);
    }

    public QTrainerProfileEntity(Class<? extends TrainerProfileEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.demo.pteam.trainer.address.repository.entity.QTrainerAddressEntity(forProperty("address")) : null;
        this.trainer = inits.isInitialized("trainer") ? new com.demo.pteam.authentication.repository.entity.QAccountEntity(forProperty("trainer")) : null;
    }

}

