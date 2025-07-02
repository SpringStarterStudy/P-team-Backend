package com.demo.pteam.schedule.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 입력된 연도가 유효한 범위 내에 있는지 검증하는 어노테이션입니다.
 *
 * <p> 이 어노테이션은 필드 및 메서드 파라미터에 적용되며, 연도가 다음 조건을 만족하는지 확인합니다:
 * <ul>
 *   <li>1900년 이상</li>
 *   <li>현재 연도 기준으로 최대 10년 이하</li>
 * </ul>
 *
 * <p> 유효하지 않은 값이 입력되면 검증 오류가 발생합니다.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearRangeValidator.class)
public @interface YearRange {
    String message() default "입력 형식이 올바르지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
