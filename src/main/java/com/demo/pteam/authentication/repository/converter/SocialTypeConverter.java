package com.demo.pteam.authentication.repository.converter;

import com.demo.pteam.authentication.domain.SocialType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SocialTypeConverter implements AttributeConverter<SocialType, Byte> {
    @Override
    public Byte convertToDatabaseColumn(SocialType type) {
        return SocialType.getCode(type);
    }

    @Override
    public SocialType convertToEntityAttribute(Byte code) {
        return SocialType.getType(code);
    }
}
