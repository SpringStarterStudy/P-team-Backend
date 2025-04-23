package com.demo.pteam.credit.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreditEntity is a Querydsl query type for CreditEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCreditEntity extends EntityPathBase<CreditEntity> {

    private static final long serialVersionUID = 1220213849L;

    public static final QCreditEntity creditEntity = new QCreditEntity("creditEntity");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final NumberPath<Integer> creditBalance = createNumber("creditBalance", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCreditEntity(String variable) {
        super(CreditEntity.class, forVariable(variable));
    }

    public QCreditEntity(Path<? extends CreditEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreditEntity(PathMetadata metadata) {
        super(CreditEntity.class, metadata);
    }

}

