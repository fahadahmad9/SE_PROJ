package com.gymtracker.gym.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter   // Generates Getters for all fields
@Setter   // Generates Setters for all fields
@NoArgsConstructor  // Required for JPA
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stripePaymentIntentId;
    private String paymentStatus; // e.g., Pending, Completed, Failed

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate paymentDate;
    private double amount;
    private String paymentMethod; // e.g., "Credit Card", "PayPal"
    private String membershipType;

    // Custom Constructor (Optional)
    public Payment(User user, double amount, String paymentMethod) {
        this.user = user;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDate.now();
    }

    public String getMembershipType() {
        return membershipType;
    }

}
