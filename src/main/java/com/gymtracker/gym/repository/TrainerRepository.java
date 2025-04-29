package com.gymtracker.gym.repository;

import com.gymtracker.gym.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
