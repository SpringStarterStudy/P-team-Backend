package com.demo.pteam.authentication.service.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Role {
    ROLE_USER(1),
    ROLE_TRAINER(2);

    private final byte code;
    private static final Map<Byte, Role> CODE_MAP =
            Arrays.stream(Role.values())
                    .collect(Collectors.toUnmodifiableMap(v -> v.getCode(), Function.identity()));

    Role(int code) {
        this.code = (byte) code;
    }

    public Byte getCode() {
        return code;
    }

    public static Byte getCode(Role role) {
        return Objects.requireNonNull(role, "Role is null").getCode();
    }

    public static Role getType(Byte code) {
        Objects.requireNonNull(code, "code is null");
        return Objects.requireNonNull(CODE_MAP.get(code), "Invalid code! Not Found Role");
    }
}
