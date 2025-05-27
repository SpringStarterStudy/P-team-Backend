package com.demo.pteam.trainer.profile.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileRequest {

    private String profileImg;
    private String intro;

    @PositiveOrZero(message = "크레딧은 0 이상이어야 합니다.")
    private Integer credit;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime contactStartTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime contactEndTime;

    @NotNull(message = "이름 공개 선택 여부는 필수입니다.")
    private Boolean isNamePublic;

    @Valid private Address address;

    private List<Certification> certifications;
    private List<String> hashtags;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {

        @NotBlank(message = "도로명 주소는 필수 입니다.")
        private String roadAddress;

        private String detailAddress;

        @NotNull(message = "경도 값은 필수 입니다.")
        private BigDecimal latitude;

        @NotNull(message = "위도 값은 필수 입니다.")
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