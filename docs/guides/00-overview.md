# API 실습 진행 가이드

이 문서는 **정답 코드가 아니라 구현 순서와 점검 기준**입니다. 각 기능을 한 번에 완성하려 하지 말고, 아래 순서를 한 기능마다 반복하세요.

1. JSP에서 입력값과 버튼 동작을 먼저 정한다.
2. 요청/응답 DTO를 만든다.
3. Controller에 `GET` 또는 `POST` 경로를 만든다.
4. Service에서 검증·권한·업무 규칙을 처리한다.
5. Mapper 인터페이스와 Mapper XML의 SQL을 연결한다.
6. Postman 또는 JSP에서 성공·실패 상황을 각각 확인한다.

## 공통 약속

- HTTP 메서드는 실습 범위에 맞춰 `GET`, `POST`만 쓴다.
- Controller는 요청을 받고 응답하는 역할, Service는 규칙과 트랜잭션, Mapper는 DB 접근만 담당한다.
- 로그인 성공 후에는 `HttpSession`의 `SessionConst.LOGIN_MEMBER`에 `LoginMember`를 저장한다.
- 로그인 상태가 필요한 기능은 `LoginInterceptor`와 Controller 양쪽에서 세션 값을 신뢰하지 말고 확인한다.
- 실패는 `ApiException`과 `GlobalExceptionHandler`로 같은 형식의 JSON 응답을 돌려준다.

## 구현 순서 권장안

1. 회원가입(정규표현식) → 이메일 인증
2. 로그인 → 로그아웃
3. 게시글 전체 목록 → 상세 조회
4. 게시글 작성 → 수정 → 삭제
5. 회원탈퇴

로그인이 완성되면 이미 남아 있는 회원정보 수정 API도 즉시 동작한다. 로그인 구현 전에는 수정 API가 세션이 없어 `401`을 응답하는 것이 정상이다.

## 현재 남겨 둔 학습 재료

- `member`, `post`의 Domain, Mapper, Mapper XML
- JSP 주소와 입력 화면
- 회원정보 수정의 Controller/Service 흐름
- 세션 상수, 인터셉터, 예외 응답 구조

Mapper SQL은 참고 자료로 남아 있지만, Service와 Controller가 없으므로 현재는 기능이 실행되지 않는다.
