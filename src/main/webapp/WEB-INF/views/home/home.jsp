<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko"><head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API연습 홈</title><link rel="stylesheet" href="/css/api-practice.css">
</head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <h2>Spring API 실습 시작 화면</h2>
    <p class="message">아래 화면은 UI 구조만 준비되어 있습니다. 회원가입·로그인·탈퇴와 게시판 기능은 직접 구현해 보세요.</p>
    <div class="actions"><a href="/member/join">회원 실습</a><a href="/post/list?page=1">게시판 실습</a><a class="secondary" href="/member/edit">회원정보 수정</a></div>
</section></main>
</body></html>
