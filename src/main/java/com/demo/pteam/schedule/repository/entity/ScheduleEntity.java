package com.demo.pteam.schedule.repository.entity;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
import com.demo.pteam.global.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleEntity extends SoftDeletableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_accounts_id")
    private AccountEntity userAccountEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_accounts_id")
    private AccountEntity trainerAccountEntity;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder
    public ScheduleEntity(LocalDateTime createdAt, AccountEntity userAccountEntity, AccountEntity trainerAccountEntity,
                          LocalDateTime startTime, LocalDateTime endTime) {
        super(createdAt);
        this.userAccountEntity = userAccountEntity;
        this.trainerAccountEntity = trainerAccountEntity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
