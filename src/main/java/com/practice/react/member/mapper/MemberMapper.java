package com.practice.react.member.mapper;

import com.practice.react.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    /**
     * member_id와 deleted_at 조건으로 활성 회원 한 명을 조회한다.
     */
    Member findActiveById(@Param("memberId") Long memberId);

    /**
     * TODO: 로그인 실습에서 이메일로 탈퇴하지 않은 회원 한 명을 조회하는 Mapper 메서드로 사용하세요.
     * TODO: Service의 login 메서드에서 입력받은 이메일을 전달해 호출하고, 반환된 Member의 BCrypt 암호를 비교하세요.
     *       비밀번호 평문 비교는 Mapper나 SQL이 아니라 Service에서 PasswordEncoder.matches로 처리합니다.
     */
    Member selectPasswordByEmaila();

    /**
     * 탈퇴하지 않은 동일 이메일 회원 수를 반환한다.
     */
    int countActiveByEmail(@Param("email") String email);

    /**
     * 이메일·BCrypt 해시 비밀번호·기본 프로필 정보를 member 테이블에 저장한다.
     */
    int insert(Member member);

    /**
     * 이름·나이·프로필·비밀번호와 updated_at을 수정한다.
     */
    int update(Member member);

    /**
     * 실제 행 삭제 대신 deleted_at을 채우고 이메일을 익명화하는 논리 삭제 SQL을 실행한다.
     */
    int withdraw(@Param("memberId") Long memberId);
}
