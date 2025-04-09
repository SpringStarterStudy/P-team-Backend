package com.demo.pteam.security.mapper;

import com.demo.pteam.authentication.repository.entity.LocalAccountEntity;
import com.demo.pteam.security.dto.LocalAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LocalAccountMapper {
    LocalAccountMapper INSTANCE = Mappers.getMapper(LocalAccountMapper.class);

    LocalAccountDto toLocalAccountDto(LocalAccountEntity localAccountEntity);
}
