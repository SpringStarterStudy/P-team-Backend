package com.demo.pteam.schedule.repository;

import com.demo.pteam.schedule.repository.dto.ScheduleDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.demo.pteam.authentication.repository.entity.QAccountEntity.accountEntity;
import static com.demo.pteam.schedule.repository.entity.QScheduleEntity.scheduleEntity;

@Repository
@RequiredArgsConstructor
public class ScheduleJPARepositoryCustomImpl implements ScheduleJPARepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleDto> findByUserIdWithinPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        return findByAccountRoleWithinPeriod(userId, start, end, scheduleEntity.userAccountEntity.id);
    }

    @Override
    public List<ScheduleDto> findByTrainerIdWithinPeriod(Long trainerId, LocalDateTime start, LocalDateTime end) {
        return findByAccountRoleWithinPeriod(trainerId, start, end, scheduleEntity.trainerAccountEntity.id);
    }

    private List<ScheduleDto> findByAccountRoleWithinPeriod(Long accountId, LocalDateTime start, LocalDateTime end, NumberPath<Long> joinTarget) {
        List<Tuple> result = queryFactory
                .select(
                        scheduleEntity.id,
                        accountEntity.id,
                        scheduleEntity.userAccountEntity.id,
                        scheduleEntity.trainerAccountEntity.id,
                        accountEntity.nickname,
                        scheduleEntity.startTime,
                        scheduleEntity.endTime)
                .from(accountEntity)
                .join(scheduleEntity).on(
                        joinTarget.eq(accountEntity.id),
                        scheduleEntity.startTime.goe(start),
                        scheduleEntity.endTime.lt(end))
                .where(accountEntity.id.eq(accountId),
                        accountEntity.deletedAt.isNull())
                .fetch();
        return createScheduleDto(result);
    }

    private List<ScheduleDto> createScheduleDto(List<Tuple> result) {
        return result.stream()
                .map(tuple -> ScheduleDto.builder()
                        .id(tuple.get(scheduleEntity.id))
                        .accountId(tuple.get(accountEntity.id))
                        .userId(tuple.get(scheduleEntity.userAccountEntity.id))
                        .trainerId(tuple.get(scheduleEntity.trainerAccountEntity.id))
                        .nickname(tuple.get(accountEntity.nickname))
                        .startTime(tuple.get(scheduleEntity.startTime))
                        .endTime(tuple.get(scheduleEntity.endTime))
                        .build()
                ).toList();
    }
}
