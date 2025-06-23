package com.demo.pteam.external.kakao.service;

import com.demo.pteam.external.kakao.client.KakaoMapClient;
import com.demo.pteam.external.kakao.dto.KakaoGeoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final KakaoMapClient kakaoMapClient;

    public KakaoGeoResponse requestCoordToAddress(BigDecimal latitude, BigDecimal longitude) {
        return kakaoMapClient.requestCoordToAddress(latitude, longitude);
    }

}
