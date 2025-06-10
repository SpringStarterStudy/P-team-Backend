package com.demo.pteam.authentication.controller.mapper;

import com.demo.pteam.authentication.controller.dto.SignupRequest;
import com.demo.pteam.authentication.controller.dto.SignupResponse;
import com.demo.pteam.authentication.domain.AccountStatus;
import com.demo.pteam.authentication.domain.Role;
import com.demo.pteam.authentication.domain.Signup;
import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {AccountStatus.class, LocalDateTime.class})
public interface SignupMapper {
    SignupMapper INSTANCE = Mappers.getMapper(SignupMapper.class);

    Signup toDomain(SignupRequest request, String encodedPassword);

    @Mapping(source = "entity.role", target = "role", qualifiedByName = "roleToString")
    @Mapping(source = "entity.password", target = "encodedPassword")
    Signup toDomain(LocalAccountEntity entity);

    @Mapping(target = "status", expression = "java(AccountStatus.UNVERIFIED)")
    @Mapping(source = "domain.role", target = "role", qualifiedByName = "roleToEnum")
    @Mapping(source = "domain.encodedPassword", target = "password")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    LocalAccountEntity toEntity(Signup domain);
    SignupResponse toResponse(Signup domain);

    @Named("roleToEnum")
    static Role roleToEnum(String role) {
        return switch (role) {
            case "user" -> Role.ROLE_USER;
            case "trainer" -> Role.ROLE_TRAINER;
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }

    @Named("roleToString")
    static String roleToString(Role role) {
        return switch (role) {
            case Role.ROLE_USER -> "user";
            case Role.ROLE_TRAINER -> "trainer";
        };
    }
}
