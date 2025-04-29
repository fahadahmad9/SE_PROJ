package com.gymtracker.gym.repository;

import com.gymtracker.gym.model.TrainerBooking;
import com.gymtracker.gym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrainerBookingRepository extends JpaRepository<TrainerBooking, Long> {
    List<TrainerBooking> findByUser(User user);
    List<TrainerBooking> findByUserId(Long userId);
}
