package com.practice.react.member.controller;

import com.practice.react.common.exception.ApiException;
import com.practice.react.common.session.LoginMember;
import com.practice.react.common.session.SessionConst;
import com.practice.react.email.service.EmailVerificationService;
import com.practice.react.member.domain.Member;
import com.practice.react.member.dto.PasswordRequest;
import com.practice.react.member.dto.SignUpRequest;
import com.practice.react.member.dto.UpdateMemberRequest;
import com.practice.react.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
/**
 * 회원 관련 JSP 화면 요청을 처리하는 MVC Controller.
 * JSON을 반환하지 않고 View의 경로 또는 redirect(재요청 주소) 경로를 반환한다.
 */
public class MemberController {
    private final MemberService memberService;
    private final EmailVerificationService emailVerificationService;

    public MemberController(MemberService memberService, EmailVerificationService emailVerificationService) {
        this.memberService = memberService;
        this.emailVerificationService = emailVerificationService;
    }

    /** 회원가입 JSP를 보여 준다. */
    @GetMapping("/join")
    public String joinForm() {
        return "member/join";
    }

    /**
     * 이메일 인증까지 끝난 가입 정보를 검증하고 회원을 저장한다.
     * 이메일 인증 상태는 서버 세션에 저장되어 있으므로 브라우저 값만으로 가입할 수 없다.
     */
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute SignUpRequest request, BindingResult bindingResult, HttpSession session,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", firstError(bindingResult));
            return "redirect:/member/join";
        }
        if (!emailVerificationService.isVerified(request.email(), session)) {
            redirectAttributes.addFlashAttribute("error", "이메일 인증을 먼저 완료해주세요.");
            return "redirect:/member/join";
        }
        try {
            memberService.signUp(request);
            emailVerificationService.clear(session);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/member/login";
        } catch (ApiException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/member/join";
        }
    }

    /** 로그인 JSP를 보여 준다. */
    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    /**
     * 로그인 실습을 위한 빈 처리 메서드.
     * 여기에서 이메일 조회, BCrypt matches, LoginMember 세션 저장, 홈 redirect를 구현한다.
     * (추가적으로 비로그인 접근하여 로그인을 하는 경우 이전 요청에 대해서 다시 넘어갈 수 있도록 구현)
     *
     * => 위에 괄호 안에 내용은 예를 들어 게시글 작성하는 URL요청 시 비로그인 시에는 접근을 할 수 없기 때문에
     *    interceptor를 통해서 로그인 페이지로 넘어가게 하고,
     *    로그인 시 이전 비로그인 접근 시 요청으로 다시 돌아 갈 수 있게 구현하는 것을 의미함
     */
    @PostMapping("/login")  // TODO: 여기서 `@PostMapping("/login")`로 작성이 되어있는 이유를 아래 줄에 주석으로 작성하시오.
    public String login() {
        // TODO: 이메일과 비밀번호를 검증하고 반환받은 회원정보를 HttpSession에 "LoginUser"라는 키값으로 저장한 뒤 홈으로 redirect하세요.
        // TODO: 로그인 기능 구현 후 LoginInterceptor 구현과 이전 요청으로 재요청할 수 있도록 수정
        return "";
    }

    /**
     * 로그아웃 실습을 위한 빈 처리 메소드.
     * 여기에서 세션에 로그인 정보가 있는지 확인 후에 Session에 저장된 로그인 정보를 삭제하고, 홈 화면으로 redirect한다.
     */
    @GetMapping("/logout")
    public String logout() {
        // TODO: 기존 Session에 회원 정보가 있는지 확인 후, Session을 비우고, 종료하고 홈화면으로 redirect 재요청을 해라.
        return "";
    }

    /**
     * 현재 로그인한 회원의 비밀번호를 확인한 뒤 DB에서 계정을 삭제하고 세션을 종료한다.
     */
    @PostMapping("/withdraw")
    public String withdraw(@Valid @ModelAttribute PasswordRequest request, BindingResult bindingResult,
                           HttpSession session, RedirectAttributes redirectAttributes) {
        LoginMember loginMember = loginMemberOrNull(session);
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", firstError(bindingResult));
            return "redirect:/member/withdraw";
        }
        try {
            memberService.withdraw(loginMember.memberId(), request.password());
            session.invalidate();
            redirectAttributes.addFlashAttribute("message", "회원탈퇴가 완료되었습니다.");
            return "redirect:/";
        } catch (ApiException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/member/withdraw";
        }
    }

    /** 로그인 세션의 회원 정보를 조회해 회원정보 수정 JSP에 전달한다. */
    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        LoginMember loginMember = loginMemberOrNull(session);
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        model.addAttribute("member", memberService.getMember(loginMember.memberId()));
        return "member/edit";
    }

    /**
     * 회원정보 수정 form 값을 검증하고 Service에 수정을 위임한다.
     * 수정된 이름은 헤더 등에서 사용할 수 있도록 세션의 LoginMember에도 반영한다.
     */
    @PostMapping("/edit")
    public String update(@Valid @ModelAttribute UpdateMemberRequest request, BindingResult bindingResult, HttpSession session,
        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", firstError(bindingResult));
            return "redirect:/member/edit";
        }
        LoginMember loginMember = requiredLoginMember(session);
        try {
            Member member = memberService.update(loginMember.memberId(), request);
            session.setAttribute(SessionConst.LOGIN_MEMBER,
                    new LoginMember(member.getMemberId(), member.getEmail(), member.getName()));
            redirectAttributes.addFlashAttribute("message", "회원정보를 수정했습니다.");
        } catch (ApiException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/member/edit";
    }

    /** 회원탈퇴 확인 JSP를 보여 준다. */
    @GetMapping("/withdraw")
    public String withdrawForm() {
        return "member/withdraw";
    }

    /** 세션에 저장된 값이 LoginMember일 때만 반환하고, 그렇지 않으면 null을 반환한다. */
    private LoginMember loginMemberOrNull(HttpSession session) {
        Object member = session.getAttribute(SessionConst.LOGIN_MEMBER);
        return member instanceof LoginMember loginMember ? loginMember : null;
    }

    /** 로그인하지 않은 요청은 401 예외로 처리하고, 로그인 회원 정보를 반환한다. */
    private LoginMember requiredLoginMember(HttpSession session) {
        LoginMember loginMember = loginMemberOrNull(session);
        if (loginMember == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return loginMember;
    }

    /** BindingResult의 첫 번째 필드 오류 메시지를 화면에 전달할 문자열로 만든다. */
    private String firstError(BindingResult bindingResult) {
        return bindingResult.getFieldError() == null
                ? "입력값을 확인해주세요." : bindingResult.getFieldError().getDefaultMessage();
    }
}
