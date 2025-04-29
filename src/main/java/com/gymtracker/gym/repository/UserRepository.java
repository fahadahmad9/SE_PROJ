package com.gymtracker.gym.repository;

import com.gymtracker.gym.model.User;
import com.gymtracker.gym.model.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by email and password for login
    User findByEmailAndPassword(String email, String password);

    // Find all users by approval status (Pending, Approved, Rejected)
    List<User> findByApprovalStatus(ApprovalStatus status);

    Optional<Object> findByName(String name);

    Optional<User> findByEmail(String email);

}