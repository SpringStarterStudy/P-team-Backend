package com.demo.pteam.security.authentication.mapper;

import com.demo.pteam.authentication.repository.dto.AccountDto;
import com.demo.pteam.security.dto.JwtAccountInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    JwtAccountInfo toJwtAccountInfo(AccountDto accountDto);
}
