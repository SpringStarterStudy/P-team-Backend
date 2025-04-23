package com.demo.pteam.payment.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExternalPaymentEntity is a Querydsl query type for ExternalPaymentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExternalPaymentEntity extends EntityPathBase<ExternalPaymentEntity> {

    private static final long serialVersionUID = 916565360L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExternalPaymentEntity externalPaymentEntity = new QExternalPaymentEntity("externalPaymentEntity");

    public final StringPath amount = createString("amount");

    public final DateTimePath<java.time.LocalDateTime> approvedAt = createDateTime("approvedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> cancelledAt = createDateTime("cancelledAt", java.time.LocalDateTime.class);

    public final StringPath cid = createString("cid");

    public final StringPath externalPaymentKey = createString("externalPaymentKey");

    public final EnumPath<com.demo.pteam.payment.domain.GatewayType> gatewayType = createEnum("gatewayType", com.demo.pteam.payment.domain.GatewayType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPaymentEntity payment;

    public QExternalPaymentEntity(String variable) {
        this(ExternalPaymentEntity.class, forVariable(variable), INITS);
    }

    public QExternalPaymentEntity(Path<? extends ExternalPaymentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExternalPaymentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExternalPaymentEntity(PathMetadata metadata, PathInits inits) {
        this(ExternalPaymentEntity.class, metadata, inits);
    }

    public QExternalPaymentEntity(Class<? extends ExternalPaymentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.payment = inits.isInitialized("payment") ? new QPaymentEntity(forProperty("payment"), inits.get("payment")) : null;
    }

}

