package com.demo.pteam.authentication.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLocalAccountEntity is a Querydsl query type for LocalAccountEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLocalAccountEntity extends EntityPathBase<LocalAccountEntity> {

    private static final long serialVersionUID = -1125024765L;

    public static final QLocalAccountEntity localAccountEntity = new QLocalAccountEntity("localAccountEntity");

    public final QAccountEntity _super = new QAccountEntity(this);

    public final StringPath activeUsername = createString("activeUsername");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final StringPath nickname = _super.nickname;

    public final StringPath password = createString("password");

    //inherited
    public final EnumPath<com.demo.pteam.authentication.domain.Role> role = _super.role;

    public final EnumPath<com.demo.pteam.authentication.domain.AccountStatus> status = createEnum("status", com.demo.pteam.authentication.domain.AccountStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath username = createString("username");

    public QLocalAccountEntity(String variable) {
        super(LocalAccountEntity.class, forVariable(variable));
    }

    public QLocalAccountEntity(Path<? extends LocalAccountEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLocalAccountEntity(PathMetadata metadata) {
        super(LocalAccountEntity.class, metadata);
    }

}

