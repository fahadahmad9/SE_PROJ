package com.gymtracker.gym.service;

import com.gymtracker.gym.model.Trainer;
import com.gymtracker.gym.model.TrainerSchedule;
import com.gymtracker.gym.repository.TrainerRepository;
import com.gymtracker.gym.repository.TrainerScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public void saveTrainer(Trainer trainer) {
        trainerRepository.save(trainer);
    }

    public Trainer getTrainerById(Long id) {
        Optional<Trainer> optional = trainerRepository.findById(id);
        return optional.orElse(null);
    }

    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }
    @Autowired
    private TrainerScheduleRepository trainerScheduleRepository;

    // Method to add a new available time slot for a trainer
    public void addTrainerSchedule(Long trainerId, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        TrainerSchedule schedule = new TrainerSchedule();
        schedule.setTrainer(trainer);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setAvailable(true);

        trainerScheduleRepository.save(schedule);
    }

    // Method to get all schedules of a trainer
    public List<TrainerSchedule> getTrainerSchedules(Long trainerId) {
        return trainerScheduleRepository.findByTrainerId(trainerId);
    }

}