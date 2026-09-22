# 게시글 목록·필터·검색·정렬·페이징 실습 가이드

## 목표와 API

`GET /api/posts?page=1&size=10&categoryId=1&searchType=TITLE&keyword=spring&sort=LATEST`

모든 조건은 선택값이다. 값이 빠지면 전체 카테고리, 제목+내용 검색, 최신순을 기본값으로 정한다.

|쿼리 파라미터|의미|예시|
|---|---|---|
|`page`|1부터 시작하는 페이지|`1`|
|`size`|한 페이지 개수|`10`|
|`categoryId`|카테고리 필터|`1`, `2`|
|`searchType`|`TITLE`, `CONTENT`, `TITLE_CONTENT`|`TITLE`|
|`keyword`|검색어|`MyBatis`|
|`sort`|정렬 기준|`LATEST`, `OLDEST`, `VIEWS`|

## 단계별 구현

### 1. 요청 값 검증

Controller에서 `page >= 1`, `size`는 예를 들어 `1~50`인지 검사한다. 허용하지 않은 `searchType`, `sort`는 enum으로 막는다. `sort` 문자열을 SQL에 그대로 붙이면 SQL 인젝션 위험이 있으므로 절대 그대로 사용하지 않는다.

### 2. Service의 기본값과 offset

- 빈 검색어는 검색 조건 없이 처리한다.
- `offset = (page - 1) * size`를 계산한다.
- 목록과 전체 개수(`count`)를 각각 조회한다.
- `PageResponse`에 목록, 현재 페이지, 전체 건수, 전체 페이지, 다음 페이지 유무를 담는다.

### 3. Mapper / XML 확장

현재 `PostMapper`와 `PostMapper.xml`에는 페이지 조회·제목/내용 검색의 출발점이 있다. 다음을 직접 확장해 본다.

- `findPage`, `count` 파라미터에 `categoryId`를 추가한다.
- XML에서 `categoryId != null`일 때만 `AND p.category_id = #{categoryId}`를 추가한다.
- 정렬은 `<choose>`로 고정된 `ORDER BY` 세 가지만 선택한다.
  - 최신순: `p.post_id DESC`
  - 오래된순: `p.post_id ASC`
  - 조회순: `p.views DESC, p.post_id DESC`
- `count` SQL에는 `ORDER BY`, `LIMIT`, `OFFSET`을 넣지 않는다.

### 4. JSP 연결

`/post/list?page=n...` 화면에서 select와 input 값을 URL 쿼리로 만들고 API를 호출한다. 응답의 `content`를 표 행으로 출력하고, 이전/다음 링크에도 현재 필터·검색·정렬 값을 모두 유지한다.

## 확인 시나리오

1. 조건 없이 1페이지를 조회한다.
2. `question`만, `inquiry`만 각각 조회한다.
3. 제목 검색과 내용 검색 결과가 다르게 나오는지 확인한다.
4. 조회순 동점일 때 최신 글이 먼저인지 확인한다.
5. 마지막 페이지에서 다음 링크가 사라지는지 확인한다.
6. `page=0`, `size=100`, 알 수 없는 `sort` 요청이 `400`인지 확인한다.

## 자주 막히는 지점

- 목록 SQL의 `JOIN member`에는 탈퇴 회원도 게시글 작성자로 표시할지 팀 규칙을 정한다.
- `keyword`가 빈 문자열이면 `LIKE '%%'`가 되지 않도록 XML의 `<if>` 조건을 확인한다.
- 페이지 번호는 DB의 `OFFSET`과 달리 화면에서는 보통 1부터 시작한다.
