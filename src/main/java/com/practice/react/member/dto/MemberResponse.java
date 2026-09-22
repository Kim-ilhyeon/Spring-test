package com.practice.react.member.dto;

import com.practice.react.member.domain.Member;
import java.time.LocalDateTime;

public record MemberResponse(Long memberId, String email, String name, Short age, Short profileId, LocalDateTime createdAt) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getMemberId(), member.getEmail(), member.getName(), member.getAge(), member.getProfileId(), member.getCreatedAt());
    }
}
