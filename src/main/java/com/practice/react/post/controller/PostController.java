package com.practice.react.post.controller;

import com.practice.react.common.dto.PageResponse;
import com.practice.react.common.exception.ApiException;
import com.practice.react.common.session.LoginMember;
import com.practice.react.common.session.SessionConst;
import com.practice.react.post.domain.Post;
import com.practice.react.post.dto.PostRequest;
import com.practice.react.post.dto.SearchType;
import com.practice.react.post.dto.SortType;
import com.practice.react.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/post")
/**
 * 게시글 JSP 화면과 form 요청을 처리하는 MVC Controller.
 * 게시글 등록 POST는 팀 실습 대상으로 의도적으로 구현하지 않았다.
 */
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 카테고리·검색어·검색 조건·정렬·페이지 값을 받아 목록을 조회하고 JSP Model에 담는다.
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) Short categoryId,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "TITLE_CONTENT") SearchType searchType,
                       @RequestParam(defaultValue = "LATEST") SortType sort,
                       Model model) {
        PageResponse<Post> pageData;
        try {
            pageData = postService.getPosts(page, size, categoryId, keyword, searchType, sort);
        } catch (ApiException exception) {
            pageData = PageResponse.of(List.<Post>of(), 1, size, 0);
            model.addAttribute("error", exception.getMessage());
        }
        model.addAttribute("posts", pageData.content());
        model.addAttribute("currentPage", pageData.page());
        model.addAttribute("totalPages", pageData.totalPages());
        model.addAttribute("hasNext", pageData.hasNext());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType.name());
        model.addAttribute("sort", sort.name());
        return "post/list";
    }

    /** 게시글 작성 form JSP를 보여 준다. 실제 등록 POST 처리는 실습 대상이다. */
    @GetMapping("/write")
    public String writeForm() {
        return "post/write";
    }

    /**
     * TODO: 게시글 작성 실습을 위한 POST 처리 메서드를 완성하세요.
     * TODO: @Valid, @ModelAttribute, BindingResult, HttpSession, RedirectAttributes가 각각 필요한 이유를 찾아보세요.
     */
    @PostMapping("/write")
    public String write() {
        // TODO: form의 categoryId·title·content을 PostRequest로 받고 유효성 검사를 처리하세요.
        // TODO: Session영역에 "loginUser"키 값으로 로그인한 회원을 꺼내 작성자 ID를 얻으세요.
        // TODO: postService.write(작성자 ID, PostRequest)를 호출한 뒤 상세 페이지 또는 목록으로 redirect하세요.
        return "";
    }

    /** 게시글 상세를 조회한다. 조회수 증가와 상세 조회는 Service에서 함께 처리한다. */
    @GetMapping("/detail/{postId}")
    public String detail(@PathVariable Long postId, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("post", postService.getPost(postId));
            return "post/detail";
        } catch (ApiException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/post/list";
        }
    }

    /** 로그인 회원이 작성자인지 확인한 뒤 수정 form에 기존 게시글 값을 전달한다. */
    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable Long postId, HttpSession session, Model model,
                           RedirectAttributes redirectAttributes) {
        LoginMember loginMember = loginMemberOrNull(session);
        if (loginMember == null) return "redirect:/member/login";
        try {
            Post post = postService.getPostForEdit(postId);
            if (!post.getMemberId().equals(loginMember.memberId())) {
                redirectAttributes.addFlashAttribute("error", "작성자만 게시글을 수정할 수 있습니다.");
                return "redirect:/post/detail/" + postId;
            }
            model.addAttribute("post", post);
            return "post/edit";
        } catch (ApiException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/post/list";
        }
    }

    /** 수정 form을 검증하고, 로그인 회원의 ID를 사용해 작성자만 수정할 수 있게 한다. */
    @PostMapping("/{postId}/edit")
    public String update(@PathVariable Long postId, @Valid @ModelAttribute PostRequest request,
                         BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        LoginMember loginMember = loginMemberOrNull(session);
        if (loginMember == null) return "redirect:/member/login";
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", firstError(bindingResult));
            return "redirect:/post/" + postId + "/edit";
        }
        try {
            postService.update(postId, loginMember.memberId(), request);
            redirectAttributes.addFlashAttribute("message", "게시글을 수정했습니다.");
            return "redirect:/post/detail/" + postId;
        } catch (ApiException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/post/detail/" + postId;
        }
    }

    /** 로그인 회원이 작성자인지 확인한 뒤 삭제 확인 JSP를 보여 준다. */
    @GetMapping("/{postId}/delete")
    public String deleteForm(@PathVariable Long postId, HttpSession session, Model model,
                             RedirectAttributes redirectAttributes) {
        LoginMember loginMember = loginMemberOrNull(session);
        if (loginMember == null) return "redirect:/member/login";
        try {
            Post post = postService.getPostForEdit(postId);
            if (!post.getMemberId().equals(loginMember.memberId())) {
                redirectAttributes.addFlashAttribute("error", "작성자만 게시글을 삭제할 수 있습니다.");
                return "redirect:/post/detail/" + postId;
            }
            model.addAttribute("post", post);
            return "post/delete";
        } catch (ApiException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/post/list";
        }
    }

    /** 작성자만 게시글의 deleted_at을 갱신하는 논리 삭제를 수행한다. */
    @PostMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
        LoginMember loginMember = loginMemberOrNull(session);
        if (loginMember == null) return "redirect:/member/login";
        try {
            postService.delete(postId, loginMember.memberId());
            redirectAttributes.addFlashAttribute("message", "게시글을 삭제했습니다.");
            return "redirect:/post/list";
        } catch (ApiException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/post/detail/" + postId;
        }
    }

    /** 세션에 저장된 로그인 회원을 반환하고, 세션이 없거나 형식이 다르면 null을 반환한다. */
    private LoginMember loginMemberOrNull(HttpSession session) {
        Object member = session.getAttribute(SessionConst.LOGIN_MEMBER);
        return member instanceof LoginMember loginMember ? loginMember : null;
    }

    /** form 검증 실패 시 사용자에게 보여 줄 첫 번째 오류 메시지를 추출한다. */
    private String firstError(BindingResult bindingResult) {
        return bindingResult.getFieldError() == null
                ? "입력값을 확인해주세요." : bindingResult.getFieldError().getDefaultMessage();
    }
}
