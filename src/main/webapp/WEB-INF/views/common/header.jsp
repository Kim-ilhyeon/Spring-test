<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<header>
    <h1><a href="/">API연습</a></h1>
    <nav>
        <a href="/post/list?page=1">게시글</a>
        <a href="/member/join">회원가입</a>
        <a href="/member/login">로그인</a>
        <form class="inline-form" method="post" action="/member/logout"><button type="submit">로그아웃 (연습 예정)</button></form>
    </nav>
</header>
