package com.gymtracker.gym.repository;

import com.gymtracker.gym.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Find admin by username and password for login
    Admin findByUsernameAndPassword(String username, String password);

    // Fix the method to use 'email' instead of 'username'
    Admin findByEmailAndPassword(String email, String password);

    Optional<Admin> findByEmail(String email);

}