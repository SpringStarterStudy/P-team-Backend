package com.demo.pteam.external.kakao.service;

import com.demo.pteam.external.kakao.client.KakaoMapClient;
import com.demo.pteam.external.kakao.dto.KakaoGeoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final KakaoMapClient kakaoMapClient;

    public boolean isValidAddressByCoordinates(BigDecimal latitude, BigDecimal longitude, String streetAddress) {
        KakaoGeoResponse response = kakaoMapClient.requestCoordToAddress(latitude, longitude);

        if(response == null || response.getDocuments().isEmpty()) return false;

        String addressName = response.getDocuments().get(0).getRoadAddress().getAddressName();

        return streetAddress.equals(addressName);
    }

}
