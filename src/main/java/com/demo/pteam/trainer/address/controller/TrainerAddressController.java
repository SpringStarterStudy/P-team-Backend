package com.demo.pteam.trainer.address.controller;

import com.demo.pteam.global.response.ApiResponse;
import com.demo.pteam.trainer.address.controller.dto.TrainerAddressRequest;
import com.demo.pteam.trainer.address.controller.dto.TrainerAddressResponse;
import com.demo.pteam.trainer.address.service.TrainerAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainers/me/location")
@RequiredArgsConstructor
public class TrainerAddressController {

    private final TrainerAddressService trainerAddressService;

    @PostMapping
    public ResponseEntity<ApiResponse<TrainerAddressResponse>> createAddress(
            @RequestBody TrainerAddressRequest request
    ) {

        TrainerAddressResponse response = trainerAddressService.createAddress(request);

        return ResponseEntity.ok(ApiResponse.success("주소가 성공적으로 등록되었습니다.", response));
    }

}
