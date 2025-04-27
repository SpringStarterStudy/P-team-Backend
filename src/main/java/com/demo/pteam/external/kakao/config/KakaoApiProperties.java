package com.demo.pteam.external.kakao.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*
    Kakao API 설정 - 자바 객체로 바인딩
 */

@Configuration
@ConfigurationProperties(prefix = "kakao.api")
@Getter
@Setter
public class KakaoApiProperties {

    private String apiKey;
    private String baseUrl;

}
