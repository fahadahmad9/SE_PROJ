package com.gymtracker.gym.service;

import com.gymtracker.gym.model.User;
import com.gymtracker.gym.model.WorkoutPlan;
import com.gymtracker.gym.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    public void saveWorkoutPlan(WorkoutPlan workoutPlan) {
        workoutPlanRepository.save(workoutPlan);
    }

    public List<WorkoutPlan> getWorkoutPlansForUser(User user) {
        if (user.getFitnessGoal() == null) {
            return workoutPlanRepository.findAll();
        } else {
            return workoutPlanRepository.findByTargetGoal(user.getFitnessGoal());
        }
    }

    public WorkoutPlan getWorkoutPlanById(Long id) {
        Optional<WorkoutPlan> optional = workoutPlanRepository.findById(id);
        return optional.orElse(null);
    }

    public void deleteWorkoutPlan(Long id) {
        workoutPlanRepository.deleteById(id);
    }

    public List<WorkoutPlan> getPlansByUserId(Long userId) {
        return workoutPlanRepository.findByUserId(userId);
    }
}