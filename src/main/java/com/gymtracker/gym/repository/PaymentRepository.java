package com.gymtracker.gym.repository;

import com.gymtracker.gym.model.Payment;
import com.gymtracker.gym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUser(User user);
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'Completed'")
    Double sumCompletedPayments();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE FUNCTION('YEAR', p.paymentDate) = :year AND FUNCTION('MONTH', p.paymentDate) = :month AND p.paymentStatus = 'Completed'")
    Double sumPaymentsByMonth(int year, int month);
    Payment findTopByUserOrderByPaymentDateDesc(User user);

    List<Payment> findByUserId(Long userId);

    Payment findByStripePaymentIntentId(String stripePaymentIntentId);

}

