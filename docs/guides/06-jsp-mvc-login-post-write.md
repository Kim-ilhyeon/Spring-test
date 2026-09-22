# JSP MVC 방식: 로그인·로그아웃·게시글 작성 실습

현재 프로젝트는 JSON API를 호출하는 방식이 아니라, **JSP form이 서버에 제출되고 Controller가 다음 JSP 또는 redirect 경로를 반환하는 방식**으로 정리되어 있습니다.

## 1. `@Controller`와 `@RestController`의 선택

|원하는 결과|사용할 애너테이션|Controller 반환값 예시|
|---|---|---|
|JSP 화면을 보여 주기|`@Controller`|`"member/login"`|
|다른 주소로 이동시키기|`@Controller`|`"redirect:/"`|
|JSON 데이터만 응답하기|`@RestController`|객체 또는 `ApiResponse`|

댓글 유무가 기준은 아닙니다. 서버에서 JSP를 렌더링할 것이므로 이 실습에서는 `@Controller`를 사용합니다.

## 2. 이미 준비된 주소

|기능|현재 제공된 GET 화면|팀원이 구현할 POST 처리|
|---|---|---|
|로그인|`GET /member/login`|`POST /member/login`|
|로그아웃|없음|`POST /member/logout`|
|게시글 작성|`GET /post/write`|`POST /post/write`|
|회원정보 수정|`GET /member/edit`, `POST /member/edit`|완성됨|

JSP의 `<form method="post" action="...">`와 input의 `name` 값은 이미 맞춰 두었습니다. JavaScript `fetch()`나 `@RequestBody`는 사용하지 않습니다.

## 3. 로그인 구현 순서

`MemberController`에 `POST /member/login` 메서드를 추가합니다.

1. `email`, `password`를 받을 폼 DTO를 만든다. JSON 요청이 아니므로 `@RequestBody` 대신 `@ModelAttribute`를 사용한다.
2. `MemberMapper.findActiveByEmail(email)`으로 회원을 찾는다.
3. `PasswordEncoder.matches(입력 비밀번호, DB 암호화 비밀번호)`를 확인한다.
4. 성공 시 `HttpSession`에 다음을 저장한다.

```java
new LoginMember(memberId, email, name)
```

키는 반드시 `SessionConst.LOGIN_MEMBER`를 사용한다.

5. 성공하면 `return "redirect:/";`으로 홈으로 보낸다.
6. 실패하면 `RedirectAttributes`로 오류 메시지를 담아 로그인 화면으로 redirect한다.

로그인 성공 여부를 JSP의 버튼 숨김만으로 처리하지 말고, 로그인 여부가 필요한 Controller/Interceptor에서도 세션을 확인해야 합니다.

## 4. 로그아웃 구현 순서

`MemberController`에 `POST /member/logout`을 추가합니다.

1. `session.invalidate()`로 기존 세션을 무효화한다.
2. `return "redirect:/";`으로 홈으로 이동한다.

헤더는 이미 form 형태로 `/member/logout`에 POST 요청을 보내도록 준비되어 있습니다.

## 5. 게시글 작성 구현 순서

`PostController`에는 작성 화면을 여는 `GET /post/write`만 있습니다. 같은 클래스에 POST 메서드를 추가합니다.

1. `PostRequest`를 `@Valid @ModelAttribute`로 받는다.
2. 세션에서 `LoginMember`를 얻는다. 없으면 로그인 페이지로 redirect한다.
3. Service에서 카테고리 존재 여부를 검증한다.
4. `Post`에 로그인 회원의 `memberId`, 카테고리, 제목, 내용을 담는다.
5. `PostMapper.insert(post)`로 저장한다.
6. 성공 후 `return "redirect:/post/detail/" + post.getPostId();`를 반환한다.

작성자 ID를 `<input type="hidden">`으로 받아서는 안 됩니다. 브라우저 값은 변경 가능하므로 세션의 회원 ID를 사용해야 합니다.

## 6. 로그인 인터셉터는 언제 연결할까?

로그인이 완성된 뒤 `WebConfig`에 `LoginInterceptor`를 등록합니다. JSP 방식에서는 로그인하지 않은 사용자를 JSON 401로 응답하기보다 로그인 화면으로 보내는 편이 자연스럽습니다.

```text
보호할 후보 경로
- /member/edit
- /post/write
- /post/*/edit
- /post/*/delete
```

`LoginInterceptor`에서 세션이 없을 때 `response.sendRedirect("/member/login")` 후 `false`를 반환하도록 바꾸는 것도 함께 실습해 보세요.

## 완료 점검

- 로그인 form 제출 후 홈으로 이동하고 세션이 생성된다.
- 회원정보 수정 페이지가 로그인 뒤 정상적으로 열린다.
- 로그아웃 뒤 회원정보 수정 페이지 접근 시 로그인 화면으로 이동한다.
- 로그인하지 않은 사용자는 게시글 저장이 되지 않는다.
- 게시글 저장 뒤 상세 화면으로 redirect되고 DB의 `member_id`가 로그인 회원과 일치한다.
