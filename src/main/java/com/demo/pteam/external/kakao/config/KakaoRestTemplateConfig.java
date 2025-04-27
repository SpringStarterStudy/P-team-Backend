package com.demo.pteam.external.kakao.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/*
    Kakao 전용 RestTemplate 분리
*/

@Configuration
public class KakaoRestTemplateConfig {

    @Bean
    @Qualifier("kakaoRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
