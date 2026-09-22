package com.practice.react.email.service;

import jakarta.servlet.http.HttpSession;

public interface EmailVerificationService {
    /** 6자리 인증번호를 생성해 메일로 보내고 서버 세션에 인증 대기 상태를 저장한다. */
    void sendCode(String email, HttpSession session);
    /** 세션의 이메일·인증번호·만료 시간을 확인한 뒤 인증 완료 상태로 변경한다. */
    void verifyCode(String email, String code, HttpSession session);
    /** 회원가입하려는 이메일이 현재 세션에서 인증 완료된 이메일인지 확인한다. */
    boolean isVerified(String email, HttpSession session);
    /** 회원가입 성공 뒤 재사용을 막기 위해 세션의 인증 정보를 제거한다. */
    void clear(HttpSession session);
}
