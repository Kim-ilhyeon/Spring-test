package com.practice.react.common.interceptor;

import com.practice.react.common.exception.ApiException;
import com.practice.react.common.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * TODO: Controller 전에 로그인 세션 존재 여부를 확인하고, 비로그인 사용자를 로그인 페이지로 이동시키도록 구현하세요.
     * TODO: JSP 화면 요청은 401 예외 대신 response.sendRedirect로 /member/login에 이동시키는 방법을 찾아 적용하세요.
     * TODO: 요청 URL과 query string을 redirectURL 같은 이름으로 보관하고, 로그인 성공 후 해당 주소로 다시 이동하게 해보세요.
     *       외부 URL을 그대로 redirect하지 않도록 현재 프로젝트 내부 경로인지 검증하는 방법도 함께 확인하세요.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        if (request.getSession(false) == null
                || request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            // TODO: response.sendRedirect("/member/login?..."), return false로 화면 요청을 로그인 페이지로 전환하세요.
            throw new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return true;
    }
}
