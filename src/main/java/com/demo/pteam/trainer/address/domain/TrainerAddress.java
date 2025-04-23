package com.demo.pteam.trainer.address.domain;

import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.trainer.address.exception.TrainerAddressErrorCode;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class TrainerAddress {

    private final Long id;
    private final String numberAddress;
    private final String roadAddress;
    private final String detailAddress;
    private final String postalCode;
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public TrainerAddress(Long id, String numberAddress, String roadAddress, String detailAddress,
                          String postalCode, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.numberAddress = numberAddress;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
        validateCoordinates(latitude, longitude);
    }

    public static TrainerAddress of(String numberAddress, String roadAddress, String detailAddress,
                                    String postalCode, BigDecimal latitude, BigDecimal longitude) {
        return new TrainerAddress(null, numberAddress, roadAddress, detailAddress, postalCode, latitude, longitude);
    }

    private void validateCoordinates(BigDecimal latitude, BigDecimal longitude) {
        if(latitude == null || longitude == null) {
            throw new ApiException(TrainerAddressErrorCode.COORDINATES_NULL);
        }
        if (latitude.abs().compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new ApiException(TrainerAddressErrorCode.INVALID_LATITUDE);
        }
        if (longitude.abs().compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new ApiException(TrainerAddressErrorCode.INVALID_LONGITUDE);
        }
    }

}
