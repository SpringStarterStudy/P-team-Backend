package com.demo.pteam.schedule.repository.entity;

import com.demo.pteam.authentication.repository.entity.AccountEntity;
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
public class ScheduleEntity {
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

    // TODO: 임시 구현
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private LocalDateTime deletedAt;

    @Builder
    public ScheduleEntity(AccountEntity userAccountEntity, AccountEntity trainerAccountEntity, LocalDateTime startTime, LocalDateTime endTime) {
        this.userAccountEntity = userAccountEntity;
        this.trainerAccountEntity = trainerAccountEntity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
