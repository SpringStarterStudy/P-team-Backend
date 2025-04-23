package com.demo.pteam.authentication.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSocialAccountEntity is a Querydsl query type for SocialAccountEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSocialAccountEntity extends EntityPathBase<SocialAccountEntity> {

    private static final long serialVersionUID = 1309113477L;

    public static final QSocialAccountEntity socialAccountEntity = new QSocialAccountEntity("socialAccountEntity");

    public final QAccountEntity _super = new QAccountEntity(this);

    public final StringPath activeUsernameCode = createString("activeUsernameCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final StringPath nickname = _super.nickname;

    //inherited
    public final EnumPath<com.demo.pteam.authentication.domain.Role> role = _super.role;

    public final EnumPath<com.demo.pteam.authentication.domain.AccountStatus> status = createEnum("status", com.demo.pteam.authentication.domain.AccountStatus.class);

    public final EnumPath<com.demo.pteam.authentication.domain.SocialType> type = createEnum("type", com.demo.pteam.authentication.domain.SocialType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath usernameCode = createString("usernameCode");

    public QSocialAccountEntity(String variable) {
        super(SocialAccountEntity.class, forVariable(variable));
    }

    public QSocialAccountEntity(Path<? extends SocialAccountEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSocialAccountEntity(PathMetadata metadata) {
        super(SocialAccountEntity.class, metadata);
    }

}

