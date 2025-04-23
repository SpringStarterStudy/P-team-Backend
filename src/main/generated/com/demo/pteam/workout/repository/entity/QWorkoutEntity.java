package com.demo.pteam.workout.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWorkoutEntity is a Querydsl query type for WorkoutEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkoutEntity extends EntityPathBase<WorkoutEntity> {

    private static final long serialVersionUID = -1478022807L;

    public static final QWorkoutEntity workoutEntity = new QWorkoutEntity("workoutEntity");

    public final com.demo.pteam.global.entity.QSoftDeletableEntity _super = new com.demo.pteam.global.entity.QSoftDeletableEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> trainingDate = createDate("trainingDate", java.time.LocalDate.class);

    public final EnumPath<StatusType> type = createEnum("type", StatusType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QWorkoutEntity(String variable) {
        super(WorkoutEntity.class, forVariable(variable));
    }

    public QWorkoutEntity(Path<? extends WorkoutEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorkoutEntity(PathMetadata metadata) {
        super(WorkoutEntity.class, metadata);
    }

}

