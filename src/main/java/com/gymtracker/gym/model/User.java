package com.gymtracker.gym.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    // User Data
    @Enumerated(EnumType.STRING)
    private FitnessGoal fitnessGoal;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;  // ✅ Removed unique constraint (password shouldn't be unique)

    @Column(nullable = false)
    private String securityQuestion;  // ✅ Removed unique constraint

    @Column(nullable = false)
    private String securityAnswer;  // ✅ Removed unique constraint

    // Approval and Rejection
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING; // Default is PENDING

    @Column(nullable = true)
    private String rejectionReason; // Store reason when user is rejected

    // Membership data
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;
    private LocalDate membershipStartDate;
    private LocalDate membershipExpiryDate; // ✅ Expiry Date
    private boolean renewalReminderSent = false;  // ✅ Track if reminder sent

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentHistory = new ArrayList<>();  // ✅ Payment History

    public LocalDate getMembershipEndDate() {
        return membershipExpiryDate;
    }

    public void setMembershipEndDate(LocalDate membershipEndDate) {
        this.membershipExpiryDate = membershipEndDate;
    }

}
