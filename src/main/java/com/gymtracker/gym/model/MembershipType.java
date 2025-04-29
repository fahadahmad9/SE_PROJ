package com.gymtracker.gym.model;

public enum MembershipType {
    GOLD(365),     // 1 year
    BASIC(30),     // 30 days
    PREMIUM(365),  // 1 year
    TRIAL(7);      // 1 week

    private final int durationDays;

    MembershipType(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getDurationDays() {
        return durationDays;
    }
}

