package com.demo.pteam.authentication.service;

import com.demo.pteam.authentication.controller.dto.SignupRequest;
import com.demo.pteam.authentication.controller.dto.SignupResponse;
import com.demo.pteam.authentication.controller.mapper.SignupMapper;
import com.demo.pteam.authentication.domain.Signup;
import com.demo.pteam.authentication.repository.LocalAccountRepository;
import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final LocalAccountRepository localAccountRepository;
    private final SignupMapper signupMapper;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest signupRequest) {
        Signup signup = signupMapper.toDomain(signupRequest, encodePassword(signupRequest.password()));
        LocalAccountEntity savedEntity = localAccountRepository.save(signupMapper.toEntity(signup));
        return signupMapper.toResponse(signupMapper.toDomain(savedEntity));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
