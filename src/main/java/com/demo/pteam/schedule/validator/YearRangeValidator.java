package com.demo.pteam.schedule.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.Objects;

public class YearRangeValidator implements ConstraintValidator<YearRange, Integer> {
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = LocalDate.now().getYear() + 10;

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (Objects.isNull(year)) {
            return true;    // year이 null인 경우는 검증 대상 아님
        }
        if (year < MIN_YEAR || year > MAX_YEAR) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("%d 이상 %d 이하의 값으로 입력해주세요.", MIN_YEAR, MAX_YEAR)
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
