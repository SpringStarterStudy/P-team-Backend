package com.demo.pteam.trainer.address.service;

import com.demo.pteam.trainer.address.controller.dto.TrainerAddressRequest;
import com.demo.pteam.trainer.address.controller.dto.TrainerAddressResponse;
import com.demo.pteam.trainer.address.domain.TrainerAddress;
import com.demo.pteam.trainer.address.repository.TrainerAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerAddressService {

    private final TrainerAddressRepository trainerAddressRepository;

    public TrainerAddressResponse createAddress(TrainerAddressRequest request) {

        TrainerAddress address = TrainerAddress.of(
                request.getNumberAddress(),
                request.getStreetAddress(),
                request.getDetailAddress(),
                request.getPostalCode(),
                request.getLatitude(),
                request.getLongitude()
        );

        TrainerAddress saved = trainerAddressRepository.save(address);

        return TrainerAddressResponse.builder()
                .addressId(saved.getId())
                .profileId(request.getProfileId()) // 현재는 매핑 안되지만, 추후 관계 설정 가능
                .streetAddress(saved.getRoadAddress())
                .detailAddress(saved.getDetailAddress())
                .postalCode(saved.getPostalCode())
                .latitude(saved.getLatitude())
                .longitude(saved.getLongitude())
                .build();
    }
}
