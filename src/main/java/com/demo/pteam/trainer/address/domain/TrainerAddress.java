package com.demo.pteam.trainer.address.domain;

import com.demo.pteam.external.kakao.dto.KakaoGeoResponse;
import com.demo.pteam.external.kakao.service.KakaoMapService;
import com.demo.pteam.global.exception.ApiException;
import com.demo.pteam.trainer.address.exception.TrainerAddressErrorCode;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class TrainerAddress {

    private final Long id;
    private String numberAddress;
    private final String roadAddress;
    private final String detailAddress;
    private String postalCode;
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
        validateCoordinates();
    }

    public static TrainerAddress of(String numberAddress, String roadAddress, String detailAddress,
                                    String postalCode, BigDecimal latitude, BigDecimal longitude) {
        return new TrainerAddress(null, numberAddress, roadAddress, detailAddress, postalCode, latitude, longitude);
    }

    private void validateCoordinates() {
        if(this.latitude == null || this.longitude == null) {
            throw new ApiException(TrainerAddressErrorCode.COORDINATES_NULL);
        }
        if (this.latitude.abs().compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new ApiException(TrainerAddressErrorCode.INVALID_LATITUDE);
        }
        if (this.longitude.abs().compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new ApiException(TrainerAddressErrorCode.INVALID_LONGITUDE);
        }
    }

    public static TrainerAddress from(String roadAddress, String detailAddress, BigDecimal latitude, BigDecimal longitude) {
        return new TrainerAddress(null, null, roadAddress, detailAddress, null, latitude, longitude);
    }

    public void verifyAndCompleteAddressWithKakao(KakaoMapService kakaoMapService) {
        KakaoGeoResponse response = kakaoMapService.requestCoordToAddress(latitude, longitude);

        if (response == null || response.getDocuments().isEmpty()) {
            throw new ApiException(TrainerAddressErrorCode.KAKAO_API_EMPTY_RESPONSE);
        }

        KakaoGeoResponse.Document document = response.getDocuments().get(0);

        if (document.getRoadAddress() == null) {
            throw new ApiException(TrainerAddressErrorCode.ROAD_ADDRESS_NOT_FOUND);
        }

        String kakaoRoadAddress = document.getRoadAddress().getAddressName();
        if (!kakaoRoadAddress.equals(this.roadAddress)) {
            throw new ApiException(TrainerAddressErrorCode.ADDRESS_COORDINATE_MISMATCH);
        }

        this.numberAddress = document.getAddress().getAddressName();
        this.postalCode = document.getRoadAddress().getZoneNo();
    }

}
