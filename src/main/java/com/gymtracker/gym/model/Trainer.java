package com.gymtracker.gym.model;

import jakarta.persistence.*;

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialization;
    private String availableTimes;
    private String contactInfo;

    // Getters and Setters
}
