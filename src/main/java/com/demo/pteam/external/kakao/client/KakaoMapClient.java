package com.demo.pteam.external.kakao.client;


import com.demo.pteam.external.kakao.config.KakaoApiProperties;
import com.demo.pteam.external.kakao.dto.KakaoGeoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/*
     Kakao geo coord2address 사용 -> Kakao RestTemplate 호출
*/

@Component
@RequiredArgsConstructor
public class KakaoMapClient {

    @Qualifier("kakaoRestTemplate")
    private final RestTemplate restTemplate;
    private final KakaoApiProperties kakaoApiProperties;

    public KakaoGeoResponse requestCoordToAddress(BigDecimal latitude, BigDecimal longitude) {
        String url = kakaoApiProperties.getBaseUrl() +
                "/v2/local/geo/coord2address.json?x=" + longitude + "&y=" + latitude;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiProperties.getApiKey());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoGeoResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, KakaoGeoResponse.class
        );

        return response.getBody();
    }

}
