# React API final practice

`easycoding`의 **Controller → Service → Mapper(MyBatis XML) → MySQL** 계층을 JSP 없이 REST API로 옮긴 연습 프로젝트입니다. 로그인 상태는 React와 공유하는 `JSESSIONID` 세션 쿠키로 유지합니다.

## 기능과 API

| 기능 | API |
| --- | --- |
| 이메일 인증번호 발송 / 확인 | `POST /api/email-verifications`, `POST /api/email-verifications/verify` |
| 회원가입 / 로그인 / 로그아웃 | `POST /api/auth/signup`, `POST /api/auth/login`, `POST /api/auth/logout` |
| 내 정보 조회 / 수정 / 탈퇴 | `GET /api/members/me`, `POST /api/members/me/update`, `POST /api/members/me/withdraw` |
| 게시글 목록·검색·페이징 | `GET /api/posts?page=1&size=10&keyword=java&searchType=TITLE_CONTENT` |
| 게시글 상세 / 작성 / 수정 / 삭제 | `GET /api/posts/{postId}`, `POST /api/posts`, `POST /api/posts/{postId}/update`, `POST /api/posts/{postId}/delete` |

모든 응답은 `{ "success": true, "message": "...", "data": ... }` 형식입니다. 게시글 쓰기·수정·삭제와 회원 API는 로그인 세션이 필요합니다.

## 실행 전 설정

MySQL에 `react_practice` 데이터베이스를 만들고 환경변수 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`를 설정합니다. `schema.sql`이 `member`, `post_category`, `post` 테이블을 생성합니다. MyBatis SQL은 `src/main/resources/mappers`에 있습니다. 실제 이메일 인증을 위해 `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`도 설정하세요.

React 요청은 반드시 쿠키를 포함해야 합니다.

## JSP API 실습 화면

서버 실행 후 다음 주소에서 기능을 테스트할 수 있습니다. 화면은 모두 GET과 POST 요청만 사용합니다.

| 화면 | 주소 |
| --- | --- |
| 홈 | `/` |
| 회원가입 / 로그인 | `/member/join`, `/member/login` |
| 회원정보 수정 / 탈퇴 | `/member/edit`, `/member/withdraw` |
| 게시글 목록 / 작성 | `/post/list?page=1`, `/post/write` |
| 게시글 상세 / 수정 / 삭제 | `/post/detail/{postId}`, `/post/{postId}/edit`, `/post/{postId}/delete` |

```js
fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  credentials: 'include',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});
```

개발 서버 주소가 다르면 `CORS_ALLOWED_ORIGIN`을 바꿉니다.
# Spring-test
