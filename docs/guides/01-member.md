# 회원 기능 실습 가이드

## 1. 회원가입: 이메일 인증과 정규표현식

### 목표

인증된 이메일만 가입시키고, 이메일·비밀번호·이름 형식을 서버에서 다시 검사한다. JSP의 검사는 사용자 편의를 위한 것이고, **최종 검사는 반드시 서버**가 한다.

### 권장 API

|기능|메서드·주소|입력|
|---|---|---|
|인증번호 발송|`POST /api/email-verifications`|`email`|
|인증번호 확인|`POST /api/email-verifications/verify`|`email`, `code`|
|회원가입|`POST /api/auth/signup`|`email`, `password`, `name`, `age`, `profileId`|

### 구현 순서

1. `EmailRequest`, `VerifyCodeRequest`, `SignUpRequest` DTO를 만든다.
2. DTO에 `@Email`, `@Pattern`, `@NotBlank`, `@Size`를 적용한다. 비밀번호 정책은 팀에서 한 가지로 정한다. 예: 영문·숫자·특수문자 포함, 8~20자.
3. 이메일 발송 요청 시 이메일 중복 여부를 확인하고, 6자리 난수·만료 시간·인증 여부를 세션 또는 별도 저장소에 보관한다.
4. 인증번호가 일치하고 만료되지 않았을 때만 `verified=true`로 바꾼다.
5. 회원가입 Service에서 다시 중복 이메일과 인증 완료 여부를 확인한다.
6. 비밀번호는 `PasswordEncoder.encode()` 후 `MemberMapper.insert()`로 저장한다. 평문 비밀번호는 DB에 저장하지 않는다.
7. 가입이 성공하면 인증 상태를 제거하고 JSP에서는 로그인 화면으로 이동시킨다.

### 함께 확인할 실패 사례

- 이메일 형식이 잘못됨
- 이미 사용 중인 이메일
- 인증번호가 틀림 또는 만료됨
- 인증을 하지 않고 가입 요청함
- 비밀번호 형식이 맞지 않음

## 2. 로그인과 로그아웃

### 권장 API

|기능|메서드·주소|입력|
|---|---|---|
|로그인|`POST /api/auth/login`|`email`, `password`|
|로그아웃|`POST /api/auth/logout`|없음|

### 로그인 Service 의사코드

1. `MemberMapper.findActiveByEmail(email)`로 탈퇴하지 않은 회원을 찾는다.
2. 회원이 없거나 `PasswordEncoder.matches(입력값, 암호화값)`이 거짓이면 같은 로그인 실패 메시지를 반환한다.
3. 성공하면 Controller에서 세션을 만들고 `LOGIN_MEMBER` 키에 `memberId`, `email`, `name`만 담은 `LoginMember`를 저장한다.
4. 응답에는 비밀번호를 절대 포함하지 않는다.

### 로그아웃 순서

1. 기존 세션이 있으면 `invalidate()` 한다.
2. 성공 응답을 반환한다.
3. JSP 헤더의 로그아웃 버튼은 이 API가 완성된 뒤에만 활성화한다.

## 3. 회원탈퇴

### 권장 API

`POST /api/members/me/withdraw`에 현재 비밀번호를 보낸다.

### 구현 순서

1. 세션에서 로그인 회원을 얻는다. 없으면 `401`이다.
2. 현재 비밀번호를 확인한다.
3. `MemberMapper.withdraw(memberId)`를 호출해 논리 삭제한다.
4. 성공했다면 세션을 무효화한다.

물리 삭제 대신 `deleted_at`을 채우는 이유는 게시글 작성자와의 연결, 감사 기록을 보존하기 위해서다. Mapper에 남아 있는 `withdraw` SQL도 이 방식이다.

## 완료 체크

- 같은 이메일로 두 번 가입할 수 없다.
- 인증하지 않은 이메일은 가입할 수 없다.
- 로그인 전 `/api/members/me` 요청은 `401`이다.
- 로그인 후 회원정보 수정 화면이 데이터를 읽고 저장한다.
- 로그아웃·탈퇴 뒤에는 수정 API가 다시 `401`이다.
