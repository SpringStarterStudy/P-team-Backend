package com.demo.pteam.workout.repository.entity;

import com.demo.pteam.global.entity.SoftDeletableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workout_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO 회원 관계 설정

    @Enumerated(EnumType.STRING)
    private StatusType type;

    private LocalDate trainingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
