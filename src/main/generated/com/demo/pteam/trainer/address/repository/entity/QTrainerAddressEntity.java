package com.demo.pteam.trainer.address.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTrainerAddressEntity is a Querydsl query type for TrainerAddressEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrainerAddressEntity extends EntityPathBase<TrainerAddressEntity> {

    private static final long serialVersionUID = -1269173317L;

    public static final QTrainerAddressEntity trainerAddressEntity = new QTrainerAddressEntity("trainerAddressEntity");

    public final StringPath detailAddress = createString("detailAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> latitude = createNumber("latitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> longitude = createNumber("longitude", java.math.BigDecimal.class);

    public final StringPath numberAddress = createString("numberAddress");

    public final StringPath postalCode = createString("postalCode");

    public final StringPath roadAddress = createString("roadAddress");

    public QTrainerAddressEntity(String variable) {
        super(TrainerAddressEntity.class, forVariable(variable));
    }

    public QTrainerAddressEntity(Path<? extends TrainerAddressEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTrainerAddressEntity(PathMetadata metadata) {
        super(TrainerAddressEntity.class, metadata);
    }

}

