package com.practice.react.member.service;

import com.practice.react.member.domain.Member;
import com.practice.react.member.dto.SignUpRequest;
import com.practice.react.member.dto.UpdateMemberRequest;

public interface MemberService {
    /**
     * 이메일 중복을 확인하고 BCrypt로 암호화한 비밀번호와 함께 새 회원을 저장한다.
     */
    Member signUp(SignUpRequest request);

    /**
     * 탈퇴하지 않은 회원 ID로 회원 한 명을 조회한다.
     */
    Member getMember(Long memberId);

    /**
     * 전달받은 이메일의 회원의 암호화된 비밀번호와 입력받은 비밀번호를 BCrypt matches를 이용해서 비교 후 맞다면 회원 정보를 가져와 반환한다.
     */
    Member login();

    /**
     * 현재 비밀번호를 검증한 뒤 이름·나이·프로필·선택적 새 비밀번호를 수정한다.
     */
    Member update(Long memberId, UpdateMemberRequest request);

    /**
     * 현재 비밀번호가 맞을 때 회원을 논리 삭제한다.
     */
    void withdraw(Long memberId, String password);
}
