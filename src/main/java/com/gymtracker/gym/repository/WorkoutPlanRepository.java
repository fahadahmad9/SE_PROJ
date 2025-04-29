package com.gymtracker.gym.repository;

import com.gymtracker.gym.model.FitnessGoal;
import com.gymtracker.gym.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    List<WorkoutPlan> findByTargetGoal(FitnessGoal targetGoal);

    List<WorkoutPlan> findByUserId(Long userId);

}
