package com.gymtracker.gym.service;

import com.gymtracker.gym.model.DietPlan;
import com.gymtracker.gym.model.User;
import com.gymtracker.gym.repository.DietPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DietPlanService {

    @Autowired
    private DietPlanRepository dietPlanRepository;

    public List<DietPlan> getAllDietPlans() {
        return dietPlanRepository.findAll();
    }

    public void saveDietPlan(DietPlan dietPlan) {
        dietPlanRepository.save(dietPlan);
    }

    public DietPlan getDietPlanById(Long id) {
        Optional<DietPlan> optional = dietPlanRepository.findById(id);
        return optional.orElse(null);
    }
    public List<DietPlan> getDietPlansForUser(User user) {
        if (user.getFitnessGoal() == null) {
            return dietPlanRepository.findAll();
        } else {
            return dietPlanRepository.findByTargetGoal(user.getFitnessGoal());
        }
    }

    public void deleteDietPlan(Long id) {
        dietPlanRepository.deleteById(id);
    }

}