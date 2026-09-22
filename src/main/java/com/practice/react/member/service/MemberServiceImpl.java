package com.practice.react.member.service;

import com.practice.react.common.exception.ApiException;
import com.practice.react.member.domain.Member;
import com.practice.react.member.dto.SignUpRequest;
import com.practice.react.member.dto.UpdateMemberRequest;
import com.practice.react.member.mapper.MemberMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입의 업무 규칙을 처리한다.
     * 이메일은 소문자로 정규화하고, 중복을 확인한 뒤 BCrypt 해시 비밀번호만 DB에 저장한다.
     */
    @Override
    @Transactional
    public Member signUp(SignUpRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (memberMapper.countActiveByEmail(email) > 0) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
        }
        // BCrypt는 같은 비밀번호도 매번 다른 salt를 사용한 단방향 해시값으로 만듭니다.
        Member member = new Member(null, email, passwordEncoder.encode(request.password()), request.name().trim(),
                request.age(), request.profileId(), null);
        memberMapper.insert(member);
        return getMember(member.getMemberId());
    }

    /** deleted_at이 비어 있는 회원만 조회하고, 없으면 404 예외를 발생시킨다. */
    @Override
    public Member getMember(Long memberId) {
        Member member = memberMapper.findActiveById(memberId);
        if (member == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.");
        }
        return member;
    }

    /**
     * 전달받은 이메일의 회원의 암호화된 비밀번호와 입력받은 비밀번호를 BCrypt matches를 이용해서 비교 후 맞다면 회원 정보를 가져와 반환한다.
     */
    @Override   // TODO: 왜 여기서 @Override어노테이션을 사용하는지 이유를 한줄 밑에 주석으로 작성하시오.
    public Member login() {
        // TODO: (1)-입력받은 이메일을 사용하는 회원의 암호화된 비밀번호를 DB에서 조회
        // TODO: (2)-BCrypt의 matches를 사용하여 입력받은 평문의 비밀번호와 DB에서 조회한 암호화된 비밀번호를 비교
        // TODO: (3)-일치한다면 해당 회원의 정보들을 DB에서 조회하여 Controller로 DTO에 담아서 반환
        // TODO: (4)-Controller에서 반환받은 회원정보를 Session영역에 "loginUser"라는 키값으로 저장
        return null;
    }

    /**
     * 현재 비밀번호를 BCrypt matches로 확인한 뒤 회원정보를 수정한다.
     * 새 비밀번호가 비어 있으면 기존 해시값을 유지한다.
     */
    @Override
    @Transactional
    public Member update(Long memberId, UpdateMemberRequest request) {
        Member member = getMember(memberId);
        if (!passwordEncoder.matches(request.currentPassword(), member.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }
        String password = request.newPassword() == null || request.newPassword().isBlank()
                ? member.getPassword() : passwordEncoder.encode(request.newPassword());
        memberMapper.update(new Member(memberId, member.getEmail(), password, request.name(), request.age(),
                request.profileId() == null ? member.getProfileId() : request.profileId(), member.getCreatedAt()));
        return getMember(memberId);
    }

    /**
     * 탈퇴 전 현재 비밀번호를 검증하고, 실제 DELETE 대신 deleted_at을 갱신한다.
     * 이 방식은 게시글 등 기존 데이터의 관계를 보존한다.
     */
    @Override
    @Transactional
    public void withdraw(Long memberId, String password) {
        Member member = getMember(memberId);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }
        if (memberMapper.withdraw(memberId) == 0) {
            throw new ApiException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.");
        }
    }

}
