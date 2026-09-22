package com.practice.react.common.session;

import java.io.Serializable;

public record LoginMember(Long memberId, String email, String name) implements Serializable { }
