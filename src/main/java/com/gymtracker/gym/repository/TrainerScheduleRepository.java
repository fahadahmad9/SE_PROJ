package com.gymtracker.gym.repository;

import com.gymtracker.gym.model.TrainerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerScheduleRepository extends JpaRepository<TrainerSchedule, Long> {

    // Custom finder method to get schedules of a specific trainer
    List<TrainerSchedule> findByTrainerId(Long trainerId);
}
