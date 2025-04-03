package com.demo.pteam.authentication.repository.converter;

import com.demo.pteam.authentication.service.domain.AccountStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AccountStatusConverter implements AttributeConverter<AccountStatus, Byte> {
    @Override
    public Byte convertToDatabaseColumn(AccountStatus status) {
        return AccountStatus.getCode(status);
    }

    @Override
    public AccountStatus convertToEntityAttribute(Byte code) {
        return AccountStatus.getType(code);
    }
}
