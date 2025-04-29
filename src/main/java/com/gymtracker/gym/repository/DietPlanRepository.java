package com.gymtracker.gym.repository;

import com.gymtracker.gym.model.DietPlan;
import com.gymtracker.gym.model.FitnessGoal;
import com.gymtracker.gym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    List<DietPlan> findByUser(User user);
    List<DietPlan> findByTargetGoal(FitnessGoal targetGoal);

}
