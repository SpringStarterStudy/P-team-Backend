package com.demo.pteam.trainerAddress.service;

import com.demo.pteam.trainer.address.controller.dto.TrainerAddressRequest;
import com.demo.pteam.trainer.address.controller.dto.TrainerAddressResponse;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.repository.TrainerAddressRepository;
import com.demo.pteam.trainer.address.service.TrainerAddressService;

import java.math.BigDecimal;

public class TrainerAddressCreateServiceTest {

    private TrainerAddressService trainerAddressService;
    private TrainerAddressRepository trainerAddressRepository;

    public void setUp() {
        trainerAddressRepository = new TrainerAddressRepository() {
            @Override
            public TrainerAddress save(TrainerAddress address) {
                return new TrainerAddress(
                        1L,
                        address.getNumberAddress(),
                        address.getRoadAddress(),
                        address.getDetailAddress(),
                        address.getPostalCode(),
                        address.getLatitude(),
                        address.getLongitude()
                );
            }
        };

        trainerAddressService = new TrainerAddressService(trainerAddressRepository);
    }

    // 성공 여부 테스트
    public void testCreateAddressSuccess() {
        System.out.print("성공 여부 테스트 -> ");
        // given
        TrainerAddressRequest request = new TrainerAddressRequest(
                1L,
                "서울 강동구 천호동 425-3",
                "서울 강동구 구천면로 192",
                "3층 301호",
                "05328",
                new BigDecimal("37.481085"),
                new BigDecimal("126.998931")
        );

        // when
        TrainerAddressResponse response = trainerAddressService.createAddress(request);

        // then
        if (response == null) {
            System.out.println("실패: 응답 객체가 null입니다.");
            return;
        }

        if (!"서울 강동구 구천면로 192".equals(response.getStreetAddress())) {
            System.out.println("실패: 예상 도로명 주소 = '서울 강동구 구천면로 192', 실제 = " + response.getStreetAddress());
            return;
        }

        if (new BigDecimal("37.481085").compareTo(response.getLatitude()) != 0) {
            System.out.println("실패: 예상 위도 = 37.481085, 실제 = " + response.getLatitude());
            return;
        }

        if (new BigDecimal("126.998931").compareTo(response.getLongitude()) != 0) {
            System.out.println("실패: 예상 경도 = 126.998931, 실제 = " + response.getLongitude());
            return;
        }

        System.out.println("성공: 모든 검증 통과");
    }

    // 실패 여부 테스트
    public void testCreateAddressFailuer() {
        System.out.print("실패 여부 테스트 -> ");
        // given
        TrainerAddressRequest request = new TrainerAddressRequest(
                1L,
                "서울 강동구 천호동 425-3",
                null,
                "3층 301호",
                "05328",
                new BigDecimal("37.481085"),
                new BigDecimal("126.998931")
        );

        try {
            TrainerAddressResponse response = trainerAddressService.createAddress(request);

            if(response != null) {
                System.out.println("실패: 도로명 주소가 없는데도 응답이 반환된다.");
            } else {
                System.out.println("성공: 도로명 주소가 누락된 경우 null 반환이 확인된다.");
            }

        } catch (Exception e) {
            System.out.println("성공: 도로명 주소가 누락된 경우 예외 발생 확인 - " + e.getMessage());
        }

    }


    public static void main(String[] args) {
        TrainerAddressCreateServiceTest test = new TrainerAddressCreateServiceTest();
        test.setUp();
        test.testCreateAddressSuccess();
        test.testCreateAddressFailuer();
    }
}