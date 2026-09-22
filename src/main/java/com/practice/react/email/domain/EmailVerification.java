package com.practice.react.email.domain;

import java.time.LocalDateTime;

/**
 * DB가 아닌 HttpSession에 잠시 보관하는 이메일 인증 상태.
 * 인증 대상 이메일, 6자리 코드, 만료 시각, 인증 완료 여부를 함께 저장한다.
 */
public record EmailVerification(String email, String code, LocalDateTime expiresAt, boolean verified) { }
