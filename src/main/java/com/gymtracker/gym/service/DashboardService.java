package com.gymtracker.gym.service;

import com.gymtracker.gym.repository.PaymentRepository;
import com.gymtracker.gym.repository.TrainerRepository;
import com.gymtracker.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        long totalTrainers = trainerRepository.count();
        long totalPayments = paymentRepository.count();

        Double totalEarnings = paymentRepository.sumCompletedPayments();

        stats.put("totalUsers", totalUsers);
        stats.put("totalTrainers", totalTrainers);
        stats.put("totalPayments", totalPayments);
        stats.put("totalEarnings", totalEarnings != null ? totalEarnings : 0.0);

        // Monthly earnings chart (current year)
        Map<String, Double> monthlyEarnings = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth ym = YearMonth.of(YearMonth.now().getYear(), month);
            Double earnings = paymentRepository.sumPaymentsByMonth(ym.getYear(), ym.getMonthValue());
            monthlyEarnings.put(ym.getMonth().toString(), earnings != null ? earnings : 0.0);
        }

        stats.put("monthlyEarnings", monthlyEarnings);

        return stats;
    }
}
