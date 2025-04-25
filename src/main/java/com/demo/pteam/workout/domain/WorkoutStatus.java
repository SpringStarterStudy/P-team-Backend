package com.demo.pteam.workout.domain;


public enum WorkoutStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절")
    ;

    private final String description;

    WorkoutStatus(String description) {
        this.description = description;
    }

    public boolean isFinished() {
        return this == APPROVED || this == REJECTED;
    }


}
