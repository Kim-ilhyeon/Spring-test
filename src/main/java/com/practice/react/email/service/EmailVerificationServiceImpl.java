package com.practice.react.email.service;

import com.practice.react.common.exception.ApiException;
import com.practice.react.common.session.SessionConst;
import com.practice.react.email.domain.EmailVerification;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    /** 인증번호의 유효 시간(분). */
    private static final int CODE_EXPIRES_MINUTES = 5;
    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final String senderAddress;
    private final SecureRandom random = new SecureRandom();

    public EmailVerificationServiceImpl(ObjectProvider<JavaMailSender> mailSenderProvider,
                                        @Value("${spring.mail.username:}") String senderAddress) {
        this.mailSenderProvider = mailSenderProvider;
        this.senderAddress = senderAddress;
    }

    /**
     * SMTP가 설정된 경우에만 6자리 난수를 발송하고, 발송 성공 후 세션에 코드·만료시간을 저장한다.
     * ObjectProvider를 사용해 SMTP 설정이 없는 개발 환경도 서버가 기동할 수 있게 한다.
     */
    @Override
    public void sendCode(String email, HttpSession session) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "SMTP 메일 설정이 필요합니다.");
        }
        String normalizedEmail = normalize(email);
        String code = "%06d".formatted(random.nextInt(1_000_000));
        SimpleMailMessage message = new SimpleMailMessage();
        if (!senderAddress.isBlank()) {
            message.setFrom(senderAddress);
        }
        message.setTo(normalizedEmail);
        message.setSubject("API 연습 이메일 인증번호");
        message.setText("인증번호는 " + code + "이며 " + CODE_EXPIRES_MINUTES + "분 동안 유효합니다.");
        mailSender.send(message);
        session.setAttribute(SessionConst.EMAIL_VERIFICATION,
                new EmailVerification(normalizedEmail, code, LocalDateTime.now().plusMinutes(CODE_EXPIRES_MINUTES), false));
    }

    /**
     * 세션의 인증 정보와 요청 이메일·코드를 모두 비교한다.
     * 일치하고 만료되지 않았을 때만 verified 값을 true로 바꾼다.
     */
    @Override
    public void verifyCode(String email, String code, HttpSession session) {
        Object value = session.getAttribute(SessionConst.EMAIL_VERIFICATION);
        if (!(value instanceof EmailVerification verification)
                || !verification.email().equals(normalize(email))
                || verification.expiresAt().isBefore(LocalDateTime.now())
                || !verification.code().equals(code)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않거나 만료되었습니다.");
        }
        session.setAttribute(SessionConst.EMAIL_VERIFICATION,
                new EmailVerification(verification.email(), verification.code(), verification.expiresAt(), true));
    }

    /** 이메일·인증 완료 여부·만료 시간을 모두 만족해야 true를 반환한다. */
    @Override
    public boolean isVerified(String email, HttpSession session) {
        Object value = session.getAttribute(SessionConst.EMAIL_VERIFICATION);
        return value instanceof EmailVerification verification
                && verification.verified()
                && verification.email().equals(normalize(email))
                && verification.expiresAt().isAfter(LocalDateTime.now());
    }

    /** 회원가입이 완료된 후 세션에 남은 인증번호를 제거한다. */
    @Override
    public void clear(HttpSession session) {
        session.removeAttribute(SessionConst.EMAIL_VERIFICATION);
    }

    /** 이메일 대소문자 차이로 인증 상태가 달라지지 않도록 소문자로 정규화한다. */
    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
