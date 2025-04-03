package com.demo.pteam.authentication.service.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SocialType {
    KAKAO(1),
    NAVER(2);

    private final byte code;
    private static final Map<Byte, SocialType> CODE_MAP =
            Arrays.stream(SocialType.values())
                    .collect(Collectors.toUnmodifiableMap(v -> v.getCode(), Function.identity()));

    SocialType(int code) {
        this.code = (byte) code;
    }

    public Byte getCode() {
        return code;
    }

    public static Byte getCode(SocialType type) {
        return Objects.requireNonNull(type, "SocialType is null").getCode();
    }

    public static SocialType getType(Byte code) {
        Objects.requireNonNull(code, "code is null");
        return Objects.requireNonNull(CODE_MAP.get(code), "Invalid code! Not Found SocialType");
    }
}
