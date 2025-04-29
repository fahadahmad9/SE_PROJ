package com.gymtracker.gym.service;

import com.gymtracker.gym.model.TrainerBooking;
import com.gymtracker.gym.model.TrainerSchedule;
import com.gymtracker.gym.model.User;
import com.gymtracker.gym.repository.TrainerBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gymtracker.gym.repository.TrainerScheduleRepository;
import com.gymtracker.gym.repository.UserRepository;
import java.util.List;

@Service
public class TrainerBookingService {

    @Autowired
    private TrainerScheduleRepository trainerScheduleRepository;

    @Autowired
    private UserRepository userRepository;
    private final TrainerBookingRepository trainerBookingRepository;

    public TrainerBookingService(TrainerBookingRepository trainerBookingRepository) {
        this.trainerBookingRepository = trainerBookingRepository;
    }

    public TrainerBooking bookTrainer(TrainerBooking trainerBooking) {
        return trainerBookingRepository.save(trainerBooking);
    }

    public List<TrainerBooking> listBookingsByUser(User user) {
        return trainerBookingRepository.findByUser(user);
    }
    public void bookTrainerSchedule(Long userId, Long scheduleId) {
        TrainerSchedule schedule = trainerScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.isAvailable()) {
            throw new RuntimeException("This time slot is no longer available");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TrainerBooking booking = new TrainerBooking();
        booking.setSchedule(schedule);
        booking.setTrainer(schedule.getTrainer());
        booking.setUser(user);

        trainerBookingRepository.save(booking);

        // After booking, mark schedule as unavailable
        schedule.setAvailable(false);
        trainerScheduleRepository.save(schedule);
    }

    public List<TrainerBooking> getBookingsByUser(Long userId) {
        return trainerBookingRepository.findByUserId(userId);
    }
}
