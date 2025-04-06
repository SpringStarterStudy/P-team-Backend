package com.demo.pteam.workout.repository.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatusType {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절")
    ;

    private final String description;

}
