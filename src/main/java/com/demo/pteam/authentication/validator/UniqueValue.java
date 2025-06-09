package com.demo.pteam.authentication.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 값이 유일(Unique)한 값인지 검증하는 어노테이션
 * <p> 이 어노테이션은 필드와 파라미터 레벨에 적용되며, 유니크 제약조건을 만족하는지 여부를 검증합니다.
 * 검증할 대상을 {@code target}에 지정합니다.
 * <p> 검증 로직은 getter 메서드를 통해 동작하므로, 비교할 필드에 대한 <b>getter 메서드가 반드시 선언</b>되어 있어야 합니다.
 * <p> {@code message} 속성은 검증 실패 시 반환될 메시지를 지정합니다.
 * 해당 메시지는 DB의 유니크 제약조건을 만족하지 않을 때 표시됩니다.
 * <p> {@code allowBlank} 속성은 빈 값 허용 여부를 결정합니다.
 * 기본값은 false입니다.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValueValidator.class)
public @interface UniqueValue {
    String message() default "이미 존재하는 값입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String target();

    boolean allowBlank() default false;
}
