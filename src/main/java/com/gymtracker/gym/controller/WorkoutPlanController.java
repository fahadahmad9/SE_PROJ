package com.gymtracker.gym.controller;

import com.gymtracker.gym.model.WorkoutPlan;
import com.gymtracker.gym.service.WorkoutPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class WorkoutPlanController {

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @GetMapping("/workoutplans")
    public String viewWorkoutPlans(Model model) {
        List<WorkoutPlan> workoutPlans = workoutPlanService.getAllWorkoutPlans();
        model.addAttribute("workoutPlans", workoutPlans);
        return "admin/workoutplans";
    }

    @GetMapping("/workoutplan/add")
    public String showAddWorkoutPlanForm(Model model) {
        model.addAttribute("workoutPlan", new WorkoutPlan());
        return "admin/add-workoutplan";
    }

    @PostMapping("/workoutplan/add")
    public String addWorkoutPlan(@ModelAttribute("workoutPlan") WorkoutPlan workoutPlan) {
        workoutPlanService.saveWorkoutPlan(workoutPlan);
        return "redirect:/admin/workoutplans";
    }

    @GetMapping("/workoutplan/edit/{id}")
    public String showEditWorkoutPlanForm(@PathVariable Long id, Model model) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        model.addAttribute("workoutPlan", workoutPlan);
        return "admin/edit-workoutplan";
    }

    @PostMapping("/workoutplan/update")
    public String updateWorkoutPlan(@ModelAttribute("workoutPlan") WorkoutPlan workoutPlan) {
        workoutPlanService.saveWorkoutPlan(workoutPlan);
        return "redirect:/admin/workoutplans";
    }

    @GetMapping("/workoutplan/delete/{id}")
    public String deleteWorkoutPlan(@PathVariable Long id) {
        workoutPlanService.deleteWorkoutPlan(id);
        return "redirect:/admin/workoutplans";
    }
}
