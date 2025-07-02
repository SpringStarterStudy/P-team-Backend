package com.demo.pteam.authentication.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
public class UniqueValueValidator implements ConstraintValidator<UniqueValue, String> {
    private final Map<String, UniqueCheckStrategy> strategyMap;
    private String target;

    public UniqueValueValidator(Map<String, UniqueCheckStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * 검증에 사용할 필드 초기화
     * <p> {@link UniqueValue} 어노테이션에 정의된 설정값을 기반으로 검증에 사용할 필드를 초기화합니다.
     * <p>구체적으로는 어노테이션에 명시된 {@code target} 옵션을 설정합니다.
     *
     * @param constraintAnnotation {@link UniqueValue} 어노테이션 인스턴스로, 검증 대상을 포함합니다.
     */
    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        this.target = constraintAnnotation.target();
    }

    /**
     * 유일(Unique)한 값인지 검증
     * <p> {@code value}가 유일한 값인 경우 {@code true}를 반환하고, 그렇지 않을 경우 {@code false}를 반환합니다.
     * <p> {@code value}의 형식이 유효하지 않은 경우, 검증을 건너뛰고 {@code true}를 반환합니다.
     * <p> 유효하지 않은 {@code target}일 경우 {@link IllegalArgumentException}를 throw 합니다.
     *
     * @param value 검증할 객체
     * @param constraintValidatorContext 검증 작업에 필요한 컨텍스트 정보가 포함된 객체
     * @return 유일한 값인 경우 {@code true}, 그렇지 않을 경우 {@code false}
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank()) {
            return true;    // @NotBlank에서 처리
        }

        UniqueCheckStrategy strategy = strategyMap.get(target);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported target: " + target);
        }
        if (!strategy.isValidFormat(value)) {
            return true;    // @Pattern에서 처리
        }
        return strategy.isUnique(value);
    }
}
