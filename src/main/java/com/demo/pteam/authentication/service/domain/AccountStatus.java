package com.demo.pteam.authentication.service.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AccountStatus {
    DELETED(-1),    // 삭제
    SUSPENDED(0),   // 정지
    ACTIVE(1),      // 활성
    UNVERIFIED(2);  // 미인증


    private final byte code;
    private static final Map<Byte, AccountStatus> CODE_MAP =
            Arrays.stream(AccountStatus.values())
                    .collect(Collectors.toUnmodifiableMap(v -> v.getCode(), Function.identity()));

    AccountStatus(int code) {
        this.code = (byte) code;
    }

    public Byte getCode() {
        return code;
    }

    public static Byte getCode(AccountStatus type) {
        return Objects.requireNonNull(type, "AccountStatus is null").getCode();
    }

    public static AccountStatus getType(Byte code) {
        Objects.requireNonNull(code, "code is null");
        return Objects.requireNonNull(CODE_MAP.get(code), "Invalid code! Not Found AccountStatus");
    }
}
