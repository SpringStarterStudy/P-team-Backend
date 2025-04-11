package com.demo.pteam.workout.repository;

import com.demo.pteam.workout.repository.entity.WorkoutEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutJpaRepository extends JpaRepository<WorkoutEntity, Long> {

    List<WorkoutEntity> findByTrainerId(Long trainerId);
}
