package com.gymtracker.gym.controller;

import com.gymtracker.gym.model.DietPlan;
import com.gymtracker.gym.service.DietPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class DietPlanController {

    @Autowired
    private DietPlanService dietPlanService;

    @GetMapping("/dietplans")
    public String viewDietPlans(Model model) {
        List<DietPlan> dietPlans = dietPlanService.getAllDietPlans();
        model.addAttribute("dietPlans", dietPlans);
        return "admin/dietplans";
    }

    @GetMapping("/dietplan/add")
    public String showAddDietPlanForm(Model model) {
        model.addAttribute("dietPlan", new DietPlan());
        return "admin/add-dietplan";
    }

    @PostMapping("/dietplan/add")
    public String addDietPlan(@ModelAttribute("dietPlan") DietPlan dietPlan) {
        dietPlanService.saveDietPlan(dietPlan);
        return "redirect:/admin/dietplans";
    }

    @GetMapping("/dietplan/edit/{id}")
    public String showEditDietPlanForm(@PathVariable Long id, Model model) {
        DietPlan dietPlan = dietPlanService.getDietPlanById(id);
        model.addAttribute("dietPlan", dietPlan);
        return "admin/edit-dietplan";
    }

    @PostMapping("/dietplan/update")
    public String updateDietPlan(@ModelAttribute("dietPlan") DietPlan dietPlan) {
        dietPlanService.saveDietPlan(dietPlan);
        return "redirect:/admin/dietplans";
    }

    @GetMapping("/dietplan/delete/{id}")
    public String deleteDietPlan(@PathVariable Long id) {
        dietPlanService.deleteDietPlan(id);
        return "redirect:/admin/dietplans";
    }
}
