package com.demo.pteam.payment.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentEntity is a Querydsl query type for PaymentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentEntity extends EntityPathBase<PaymentEntity> {

    private static final long serialVersionUID = -3264133L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentEntity paymentEntity = new QPaymentEntity("paymentEntity");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final QExternalPaymentEntity externalPayment;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> paymentId = createNumber("paymentId", Long.class);

    public final EnumPath<com.demo.pteam.payment.domain.PaymentMethod> paymentMethod = createEnum("paymentMethod", com.demo.pteam.payment.domain.PaymentMethod.class);

    public final NumberPath<Long> paymentMoney = createNumber("paymentMoney", Long.class);

    public final StringPath productId = createString("productId");

    public final EnumPath<com.demo.pteam.payment.domain.PaymentStatus> status = createEnum("status", com.demo.pteam.payment.domain.PaymentStatus.class);

    public final DateTimePath<java.time.LocalDateTime> transactionTime = createDateTime("transactionTime", java.time.LocalDateTime.class);

    public QPaymentEntity(String variable) {
        this(PaymentEntity.class, forVariable(variable), INITS);
    }

    public QPaymentEntity(Path<? extends PaymentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentEntity(PathMetadata metadata, PathInits inits) {
        this(PaymentEntity.class, metadata, inits);
    }

    public QPaymentEntity(Class<? extends PaymentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.externalPayment = inits.isInitialized("externalPayment") ? new QExternalPaymentEntity(forProperty("externalPayment"), inits.get("externalPayment")) : null;
    }

}

