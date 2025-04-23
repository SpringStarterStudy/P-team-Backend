package com.demo.pteam.credit.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreditLogEntity is a Querydsl query type for CreditLogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCreditLogEntity extends EntityPathBase<CreditLogEntity> {

    private static final long serialVersionUID = 1074232337L;

    public static final QCreditLogEntity creditLogEntity = new QCreditLogEntity("creditLogEntity");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final NumberPath<Integer> creditBalance = createNumber("creditBalance", Integer.class);

    public final NumberPath<Integer> creditDifference = createNumber("creditDifference", Integer.class);

    public final EnumPath<com.demo.pteam.credit.domain.CreditInfo> creditInfo = createEnum("creditInfo", com.demo.pteam.credit.domain.CreditInfo.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCreditLogEntity(String variable) {
        super(CreditLogEntity.class, forVariable(variable));
    }

    public QCreditLogEntity(Path<? extends CreditLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreditLogEntity(PathMetadata metadata) {
        super(CreditLogEntity.class, metadata);
    }

}

