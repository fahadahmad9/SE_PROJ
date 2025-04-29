package com.gymtracker.gym.service;

import com.gymtracker.gym.model.Admin;
import com.gymtracker.gym.model.User;
import com.gymtracker.gym.model.Payment;
import com.gymtracker.gym.model.ApprovalStatus;
import com.gymtracker.gym.repository.AdminRepository;
import com.gymtracker.gym.repository.UserRepository;
import com.gymtracker.gym.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Register new admin
    public void registerAdmin(String name, String email, String password) {
        // Check if an admin with this email already exists
        Optional<Admin> existingAdmin = adminRepository.findByEmail(email);
        if (existingAdmin.isPresent()) {
            throw new IllegalArgumentException("Admin already exists.");
        }

        // Create new admin object
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(password); // Consider hashing the password for security

        // Save the admin in the database
        adminRepository.save(admin);
    }


    public Admin authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmailAndPassword(email, password);
        return admin;
    }

    public List<User> getPendingUsers() {
        return userRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }

    public void approveUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setApprovalStatus(ApprovalStatus.APPROVED);
            userRepository.save(user);
        }
    }

    public void rejectUser(Long userId, String reason) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setApprovalStatus(ApprovalStatus.REJECTED);
            user.setRejectionReason(reason);
            userRepository.save(user);
        }
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public void generateInvoices() {
        List<Payment> payments = paymentRepository.findAll();
        for (Payment p : payments) {
            System.out.println("Invoice: " + p.getUser().getName() + ", Paid: $" + p.getAmount() + " on " + p.getPaymentDate());
        }
    }
}
