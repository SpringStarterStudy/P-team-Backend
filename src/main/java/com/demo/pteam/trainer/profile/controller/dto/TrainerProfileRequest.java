package com.demo.pteam.trainer.profile.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileRequest {

    private String profileImg;
    private String intro;
    private Integer credit;
    private String contactStartTime;
    private String contactEndTime;
    private Boolean isNamePublic;

    private Address address;
    private List<Certification> certifications;
    private List<String> hashtags;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String numberAddress;
        private String streetAddress;
        private String detailAddress;
        private String postalCode;
        private BigDecimal latitude;
        private BigDecimal longitude;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Certification {
        private String certiName;
        private String certiImg;
    }

}