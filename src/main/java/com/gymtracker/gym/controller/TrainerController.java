package com.gymtracker.gym.controller;

import com.gymtracker.gym.model.Trainer;
import com.gymtracker.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;
    @GetMapping("/trainers")
    public String viewTrainers(Model model) {
        List<Trainer> trainers = trainerService.getAllTrainers();
        model.addAttribute("trainers", trainers);
        return "admin/trainers";
    }
    @PostMapping("/trainer/add-schedule")
    public String addTrainerSchedule(
            @RequestParam Long trainerId,
            @RequestParam String dayOfWeek,
            @RequestParam String startTime,
            @RequestParam String endTime) {

        trainerService.addTrainerSchedule(trainerId, dayOfWeek, LocalTime.parse(startTime), LocalTime.parse(endTime));
        return "redirect:/trainer-dashboard"; // Assume you have or will create this page
    }

    @GetMapping("/trainer/add")
    public String showAddTrainerForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        return "admin/add-trainer";
    }

    @PostMapping("/trainer/add")
    public String addTrainer(@ModelAttribute("trainer") Trainer trainer) {
        trainerService.saveTrainer(trainer);
        return "redirect:/admin/trainers";
    }

    @GetMapping("/trainer/edit/{id}")
    public String showEditTrainerForm(@PathVariable Long id, Model model) {
        Trainer trainer = trainerService.getTrainerById(id);
        model.addAttribute("trainer", trainer);
        return "admin/edit-trainer";
    }

    @PostMapping("/trainer/update")
    public String updateTrainer(@ModelAttribute("trainer") Trainer trainer) {
        trainerService.saveTrainer(trainer);
        return "redirect:/admin/trainers";
    }

    @GetMapping("/trainer/delete/{id}")
    public String deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return "redirect:/admin/trainers";
    }
}
