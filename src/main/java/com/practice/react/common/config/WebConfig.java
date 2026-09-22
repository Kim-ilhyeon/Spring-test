package com.practice.react.common.config;

import com.practice.react.common.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    /**
     * TODO: 로그인 구현 뒤 이 메서드에서 보호 URL에 LoginInterceptor를 등록하세요.
     * TODO: registry.addInterceptor(loginInterceptor)의 사용법을 확인하세요.
     * TODO: addPathPatterns로
 *          /member/edit, /member/withdraw, /post/write, /post/{postId}/edit, /post/{postId}/delete등의
 *          비로그인 접근 시 제어해야하는 요청 URL을 보호하세요.
     * TODO: 로그인·회원가입 페이지와 정적 리소스는 excludePathPatterns로 제외해 로그인 페이지 순환을 막으세요.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO: 위 가이드에 맞춰 인터셉터 등록 코드를 작성하세요.
    }
}
