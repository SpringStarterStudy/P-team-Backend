package com.demo.pteam.authentication.controller.dto;

import com.demo.pteam.authentication.validator.FieldsMatch;
import com.demo.pteam.authentication.validator.UniqueValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@FieldsMatch(first = "password", second = "passwordConfirm", message = "비밀번호가 일치하지 않습니다.")
public record SignupRequest(
        @UniqueValue(target = "username", message = "이미 존재하는 아이디입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "아이디는 4~12자리 영문, 숫자만 가능합니다.")
        String username,

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                message = "비밀번호는 8~20자 이상으로 영문, 숫자, 특수문자 각각 1가지 이상 조합해주세요.")
        String password,

        @JsonProperty("password_confirm")
        String passwordConfirm,

        @Pattern(regexp = "^(user|trainer)$", message = "user 또는 trainer만 가능합니다.")
        String role,

        @UniqueValue(target = "email", message = "이미 존재하는 이메일입니다.")
        @Size(min = 8, max = 254, message = "이메일은 8자리 이상, 254자리 미만만 가능합니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]{3,}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "이메일 형식이 맞지 않습니다.")
        String email,

        @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$", message = "이름은 2~10자 이상의 한글 또는 영어로 입력해주세요.")
        String name,

        @UniqueValue(target = "nickname", message = "이미 존재하는 닉네임입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 2~10자 이상의 한글, 영어, 숫자만 가능합니다.")
        String nickname
) {
}
