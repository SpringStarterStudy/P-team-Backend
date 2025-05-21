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
        @JsonProperty("address")
        private Address address;
    }

    @Getter
    public static class RoadAddress {
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("zone_no")
        private String zoneNo;
    }

    @Getter
    public static class Address {
        @JsonProperty("address_name")
        private String addressName;
    }

}
