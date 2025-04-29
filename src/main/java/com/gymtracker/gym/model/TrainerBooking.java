package com.gymtracker.gym.model;

import jakarta.persistence.*;

@Entity
public class TrainerBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Trainer trainer;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private TrainerSchedule schedule; // NEW: link to schedule slot

    // Constructors
    public TrainerBooking() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public TrainerSchedule getSchedule() { return schedule; }
    public void setSchedule(TrainerSchedule schedule) { this.schedule = schedule; }
}
