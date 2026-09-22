# Spring 프로젝트 시작 가이드

이 문서는 팀원에게 **프로젝트를 만든 직후 무엇을 보고, 왜 그렇게 설정하는지** 설명하기 위한 자료입니다. 현재 회원·게시판 JSP/MyBatis 실습 프로젝트를 기준으로 합니다.

## 1. Spring Initializr로 프로젝트 만들기

브라우저에서 [Spring Initializr](https://start.spring.io/)를 연 뒤, 아래처럼 설정합니다. 버전은 화면에 제시되는 안정 버전을 기본으로 두고, 팀에서 정한 버전이 있으면 모두 같은 버전을 사용합니다.

|항목|권장값|의미|
|---|---|---|
|Project|Maven|라이브러리와 빌드 과정을 `pom.xml`로 관리하는 도구|
|Language|Java|Spring 서버 코드를 작성할 언어|
|Spring Boot|안정 버전|Spring 설정을 자동화해 주는 Spring Boot 버전|
|Group|`com.practice`|자바 패키지의 큰 이름. 보통 회사·팀의 도메인 순서|
|Artifact|`react` 또는 `api-practice`|프로젝트 폴더와 빌드 결과물의 기본 이름|
|Name|Artifact와 동일|Spring Boot 프로젝트 표시 이름|
|Package name|`com.practice.react`|자바 소스의 최상위 패키지|
|Packaging|`War`|현재처럼 JSP를 포함한 웹 애플리케이션 형태로 빌드|
|Java|17|팀 공통 JDK 버전. 설치된 JDK와 반드시 맞춰야 함|

`Group`과 `Artifact`는 처음에는 헷갈리기 쉽습니다. `Group`은 소속을 나타내는 패키지 앞부분이고, `Artifact`는 이 프로젝트 자체의 이름입니다. 따라서 `com.practice` + `react`면 기본 패키지는 보통 `com.practice.react`가 됩니다.

### Initializr에서 선택할 의존성

|의존성|선택 이유|
|---|---|
|Spring Web|Controller, REST API, 내장 Tomcat 서버를 사용하기 위해 필요|
|Validation|`@NotBlank`, `@Email`, `@Pattern` 등 요청값 검증에 사용|
|MyBatis Framework|Mapper 인터페이스와 XML SQL을 연결|
|MySQL Driver|MySQL DB 연결|
|Spring Security|현재 프로젝트에서는 비밀번호 암호화(`PasswordEncoder`)에 사용. 처음부터 인증 체계까지 학습하지 않을 경우 나중에 추가해도 됨|

JSP는 Initializr에서 한 번에 완성되지 않을 수 있습니다. 생성 뒤 `pom.xml`에 JSP 컴파일용 `tomcat-embed-jasper`, JSTL 의존성을 추가해야 합니다. 현재 프로젝트에는 이미 설정되어 있습니다.

> `Spring Security`를 넣으면 기본 보안 설정 때문에 모든 주소가 막히거나 기본 로그인 화면이 나타날 수 있습니다. 처음에는 Security 설정 파일을 함께 설명하거나, 비밀번호 암호화가 필요해지는 시점에 추가하는 방법도 좋습니다.

### 생성 후 IDE에서 열기

1. `GENERATE`로 ZIP 파일을 받는다.
2. 원하는 작업 폴더에 압축을 푼다.
3. IntelliJ에서는 **`pom.xml`이 있는 폴더**를 연다.
4. Maven 동기화가 끝날 때까지 기다린다.
5. `...Application.java`의 실행 버튼 또는 아래 명령으로 실행한다.

```powershell
.\mvnw.cmd spring-boot:run
```

테스트와 깨끗한 재빌드는 다음 명령을 사용한다.

```powershell
.\mvnw.cmd clean test
```

`mvnw.cmd`는 Maven Wrapper입니다. 팀원이 별도로 Maven을 설치하지 않아도 프로젝트가 지정한 Maven 환경을 사용할 수 있게 해 줍니다.

## 2. 프로젝트 루트는 어디인가?

**`pom.xml`이 있는 폴더가 Spring 프로젝트의 루트**입니다. 현재 프로젝트에서는 다음 위치입니다.

`C:\Users\USER\Desktop\react-practice\react`

`src`만 열거나 `src/main/java`만 열면 Maven 설정, 실행 명령, 리소스 파일을 IDE가 제대로 인식하지 못할 수 있습니다.

```text
react/                         ← 프로젝트 루트 (pom.xml 위치)
├─ pom.xml
├─ mvnw, mvnw.cmd
├─ .mvn/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  ├─ resources/
│  │  └─ webapp/
│  └─ test/
└─ target/                     ← Maven이 자동 생성, 직접 수정하지 않음
```

## 3. 대표 폴더와 파일의 역할

### `src/main/java`

실행되는 자바 소스 코드 위치입니다. 이 프로젝트의 패키지 기준점은 `com.practice.react`입니다.

|위치|역할|
|---|---|
|`ReactApplication.java`|`main()`이 있는 서버 시작점. `@SpringBootApplication`이 같은 패키지와 하위 패키지의 Spring Bean을 찾는다.|
|`common/config`|CORS, Security, Web MVC 같은 공통 설정|
|`common/interceptor`|API 요청 전 로그인 여부처럼 공통 검사를 수행|
|`common/session`|세션에 저장할 로그인 회원 정보와 키 이름|
|`common/exception`|에러를 일관된 JSON 형식으로 바꾸는 예외 처리|
|`member`, `post`|도메인별 Controller, Service, Mapper, DTO, Domain을 둠|

### `src/main/resources`

자바 코드가 아닌 설정·정적 파일·SQL XML을 둡니다.

|파일/폴더|역할|
|---|---|
|`application.properties`|서버 포트, DB 접속, MyBatis, JSP 경로 등 실행 설정|
|`mappers/*.xml`|MyBatis Mapper 인터페이스의 메서드와 SQL을 연결|
|`static/css`, `static/js`|브라우저가 `/css/...`, `/js/...` 주소로 받는 정적 파일|
|`schema.sql`|테이블 생성용 SQL. 공용 DB에서는 자동 실행 여부를 특히 주의|

`application.properties`의 DB 주소·사용자·비밀번호는 환경마다 다릅니다. 실제 비밀번호는 Git에 올리지 말고 환경 변수 또는 개인 설정 파일로 분리하는 습관을 들이는 것이 좋습니다.

### `src/main/webapp`

JSP 같은 웹 화면 파일을 둡니다. 이 프로젝트에서는 다음 규칙을 사용합니다.

```text
src/main/webapp/WEB-INF/views/
├─ common/header.jsp
├─ home/home.jsp
├─ member/join.jsp
└─ post/list.jsp
```

`WEB-INF` 아래 JSP는 브라우저가 URL로 직접 열 수 없습니다. Controller가 뷰 이름을 반환해야만 열리므로, 화면 경로를 제어하기 좋습니다.

예를 들어 View Controller가 `"post/list"`를 반환하면 설정에 따라 `/WEB-INF/views/post/list.jsp`가 선택됩니다.

### `src/test`

테스트 코드와 테스트 전용 설정을 둡니다. 이 프로젝트의 테스트는 H2 메모리 DB를 사용하므로 공용 MySQL DB를 직접 변경하지 않고 Mapper를 확인할 수 있습니다.

### `pom.xml`

Maven의 중심 파일입니다.

- Spring Boot, MyBatis, MySQL 등 라이브러리 목록
- Java 버전
- 프로젝트 이름과 패키징 방식
- 빌드 플러그인

라이브러리를 추가·삭제한 뒤에는 Maven 새로고침을 해야 IDE와 빌드에 반영됩니다.

### `target`

컴파일된 `.class`, 테스트 결과, WAR 파일처럼 Maven이 자동 생성하는 결과물입니다. 직접 수정하지 않고, 이상한 이전 클래스가 남은 것 같으면 `mvnw.cmd clean test`로 안전하게 다시 만듭니다. 보통 Git에도 올리지 않습니다.

## 4. 가장 먼저 보여 줄 요청 흐름

한 기능이 작동하는 길을 먼저 보여 주면 폴더가 덜 낯설어집니다.

```text
JSP 버튼/JavaScript
        ↓ HTTP 요청
Controller
        ↓
Service  ← 검증, 권한, 업무 규칙, 트랜잭션
        ↓
Mapper 인터페이스
        ↓
Mapper XML의 SQL
        ↓
MySQL
```

처음에는 회원정보 수정처럼 이미 동작하는 기능 하나를 위에서 아래 방향으로 같이 따라가 보세요. 그 다음 회원가입이나 게시글 작성 기능을 똑같은 흐름으로 새로 구현하면 구조를 훨씬 빨리 익힐 수 있습니다.

## 5. 첫 설명 때 자주 나오는 질문

### Controller와 Service를 왜 나누나요?

Controller는 HTTP 요청·응답에 집중하고, Service는 "이 이메일이 중복인가?", "작성자인가?" 같은 업무 규칙에 집중하게 하려는 분리입니다. 나중에 JSP 대신 React가 와도 Service는 대부분 그대로 재사용할 수 있습니다.

### DTO와 Domain은 왜 다른가요?

DTO는 API로 받거나 보여 줄 데이터 모양이고, Domain은 DB의 회원·게시글 같은 핵심 데이터를 표현합니다. 비밀번호처럼 외부에 보여 주면 안 되는 값이 있기 때문에 같은 클래스로 모두 처리하지 않는 편이 안전합니다.

### Mapper XML을 쓰는 이유는 무엇인가요?

복잡한 SQL을 Java 문자열로 쓰지 않고 XML에서 분리해 읽기 쉽게 관리할 수 있습니다. `PostMapper.java`의 메서드 이름과 `PostMapper.xml`의 `id`가 같아야 연결됩니다.

### 서버가 실행되지 않을 때 제일 먼저 볼 것은?

1. JDK 17이 프로젝트에 설정됐는지
2. `application.properties`의 DB 접속 정보가 맞는지
3. MySQL 서버가 실행 중인지
4. 콘솔에서 가장 아래쪽 `Caused by:` 오류가 무엇인지
5. 소스 파일을 삭제한 뒤라면 `mvnw.cmd clean test`가 통과하는지
