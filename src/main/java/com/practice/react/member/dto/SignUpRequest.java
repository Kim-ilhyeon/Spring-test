package com.practice.react.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        @NotBlank(message = "이메일을 입력해주세요.") @Email(message = "올바른 이메일 형식이 아닙니다.") String email,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$",
                message = "비밀번호는 8~20자의 영문, 숫자, 특수문자(!@#$%^&*)를 모두 포함해야 합니다.") String password,
        @NotBlank(message = "이름을 입력해주세요.")
        @Pattern(regexp = "^[가-힣A-Za-z ]{1,15}$", message = "이름은 1~15자의 한글 또는 영문만 사용할 수 있습니다.") String name,
        @Min(value = 0, message = "나이는 0 이상이어야 합니다.") @Max(value = 150, message = "나이는 150 이하여야 합니다.") Short age,
        @Min(value = 1, message = "프로필 번호는 1 이상이어야 합니다.") Short profileId
) { }
