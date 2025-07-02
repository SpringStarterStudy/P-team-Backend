package com.demo.pteam.schedule.repository;

import com.demo.pteam.schedule.repository.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleJPARepository extends JpaRepository<ScheduleEntity, Long>, ScheduleJPARepositoryCustom {
}
