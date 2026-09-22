package com.practice.react.email.controller;

import com.practice.react.common.dto.ApiResponse;
import com.practice.react.email.dto.EmailRequest;
import com.practice.react.email.dto.VerifyCodeRequest;
import com.practice.react.email.service.EmailVerificationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email-verifications")
/**
 * 회원가입 화면의 fetch 요청만 처리하는 REST Controller.
 * JSP를 다시 렌더링하지 않고 JSON 성공·실패 응답을 반환한다.
 */
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    /** 인증번호를 생성하고 SMTP 메일을 발송한 뒤, 인증 대기 상태를 세션에 저장한다. */
    @PostMapping
    public ApiResponse<Void> send(@Valid @RequestBody EmailRequest request, HttpSession session) {
        emailVerificationService.sendCode(request.email(), session);
        return ApiResponse.success("인증번호를 이메일로 발송했습니다.", null);
    }

    /** 세션에 저장된 인증번호와 요청 코드를 비교해 이메일 인증 완료 상태로 바꾼다. */
    @PostMapping("/verify")
    public ApiResponse<Void> verify(@Valid @RequestBody VerifyCodeRequest request, HttpSession session) {
        emailVerificationService.verifyCode(request.email(), request.code(), session);
        return ApiResponse.success("이메일 인증이 완료되었습니다.", null);
    }
}
