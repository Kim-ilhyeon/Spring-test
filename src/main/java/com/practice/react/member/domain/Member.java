package com.practice.react.member.domain;

import java.time.LocalDateTime;

/**
 * member 테이블의 한 행을 표현하는 Domain 객체.
 * DB 조회·저장에는 사용하지만, 비밀번호가 포함되므로 화면이나 JSON 응답에는 그대로 노출하지 않는다.
 */
public class Member {
    /** 회원 기본 키. INSERT 후 MyBatis가 자동 생성값을 채운다. */
    private Long memberId;
    /** 로그인과 중복 확인에 사용하는 이메일. */
    private String email;
    /** 평문이 아닌 BCrypt 단방향 해시 비밀번호. */
    private String password;
    /** 화면에 표시할 회원 이름. */
    private String name;
    /** 회원 나이. */
    private Short age;
    /** 선택한 프로필 번호. */
    private Short profileId;
    /** 회원가입 시각. */
    private LocalDateTime createdAt;

    /** MyBatis가 조회 결과를 객체에 채울 때 사용하는 기본 생성자. */
    public Member() { }

    /** Service에서 INSERT·UPDATE용 Member 객체를 만들 때 사용하는 생성자. */
    public Member(Long memberId, String email, String password, String name, Short age, Short profileId, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.profileId = profileId;
        this.createdAt = createdAt;
    }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Short getAge() { return age; }
    public void setAge(Short age) { this.age = age; }
    public Short getProfileId() { return profileId; }
    public void setProfileId(Short profileId) { this.profileId = profileId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
