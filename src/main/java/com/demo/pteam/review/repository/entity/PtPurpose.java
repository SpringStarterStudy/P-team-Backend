package com.demo.pteam.review.repository.entity;

public enum PtPurpose {
    다이어트("체중 감량 목적"),
    근력강화("근육랑 증가 목적"),
    체형교정("자세 및 체형 개선 목적"),
    재활치료("부상 후 회복 목적");

    private final String description;

    PtPurpose(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
