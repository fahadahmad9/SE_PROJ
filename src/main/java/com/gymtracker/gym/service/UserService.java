package com.gymtracker.gym.service;

import com.gymtracker.gym.model.MembershipType;
import com.gymtracker.gym.model.Payment;
import com.gymtracker.gym.model.User;
import com.gymtracker.gym.model.ApprovalStatus;
import com.gymtracker.gym.repository.PaymentRepository;
import com.gymtracker.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // For CLI registration
    public void registerUser(String name, String email, String phone, String password, String securityQuestion, String securityAnswer) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setSecurityQuestion(securityQuestion);
        user.setSecurityAnswer(securityAnswer);
        user.setApprovalStatus(ApprovalStatus.PENDING);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null); // or throw a custom exception if needed
    }

    // For Web registration
    public void registerUser(User user) {
        user.setApprovalStatus(ApprovalStatus.PENDING);
        user.setMembershipType(MembershipType.TRIAL);
        user.setMembershipStartDate(LocalDate.now());
        user.setMembershipExpiryDate(LocalDate.now().plusDays(user.getMembershipType().getDurationDays()));
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> authenticateUser(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user != null && user.getApprovalStatus() == ApprovalStatus.APPROVED) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getPendingRegistrations() {
        return userRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }

    public void approveUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setApprovalStatus(ApprovalStatus.APPROVED);
            userRepository.save(user);
        }
    }

    public void rejectUser(Long userId, String reason) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setApprovalStatus(ApprovalStatus.REJECTED);
            user.setRejectionReason(reason);
            userRepository.save(user);
        }
    }

    public List<Payment> getUserPaymentHistory(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public boolean renewMembership(Long userId, MembershipType membershipType, double amount, String paymentMethod) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setMembershipType(membershipType);
            userRepository.save(user);

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setAmount(amount);
            payment.setPaymentDate(LocalDate.now());
            payment.setPaymentMethod(paymentMethod);
            paymentRepository.save(payment);

            return true;
        }
        return false;
    }

    public void purchaseMembership(Long userId, MembershipType membershipType) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setMembershipType(membershipType);
            userRepository.save(user);

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setAmount(50.0); // Example price
            payment.setPaymentDate(LocalDate.now());
            payment.setPaymentMethod("Cash");
            paymentRepository.save(payment);
        }
    }

    public void renewMembership(Long userId, MembershipType membershipType) {
    }

    public User findByUsername(String name) {
        return (User) userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User with name '" + name + "' not found"));
    }

}
