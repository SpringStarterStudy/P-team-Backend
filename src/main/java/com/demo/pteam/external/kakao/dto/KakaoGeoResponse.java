package com.demo.pteam.external.kakao.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
    응답 JSON 매핑 DTO
*/

@Getter
@NoArgsConstructor
public class KakaoGeoResponse {

    private List<Document> documents;

    @Getter
    public static class Document {
        @JsonProperty("road_address")
        private RoadAddress roadAddress;
    }

    @Getter
    public static class RoadAddress {
        @JsonProperty("address_name")
        private String addressName;
    }
}
