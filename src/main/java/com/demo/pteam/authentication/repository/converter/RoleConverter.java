package com.demo.pteam.authentication.repository.converter;

import com.demo.pteam.authentication.domain.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleConverter implements AttributeConverter<Role, Byte> {
    @Override
    public Byte convertToDatabaseColumn(Role role) {
        return Role.getCode(role);
    }

    @Override
    public Role convertToEntityAttribute(Byte code) {
        return Role.getType(code);
    }
}
