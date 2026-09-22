<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><title>로그인</title><link rel="stylesheet" href="/css/api-practice.css"></head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <h2>로그인</h2>
    <% if (request.getAttribute("message") != null) { %><p class="message success">${message}</p><% } %>
    <% if (request.getAttribute("error") != null) { %><p class="message">${error}</p><% } %>
    <p class="message">실습 과제: `POST /member/login`을 추가하고 로그인 성공 시 HttpSession에 로그인 회원 정보를 저장하세요.</p>
    <form method="post" action="/member/login">
        <label for="email">이메일</label><input id="email" name="email" type="email" required>
        <label for="password">비밀번호</label><input id="password" name="password" type="password" required>
        <button type="submit">로그인</button><a href="/member/join">회원가입</a>
    </form>
</section></main>
</body></html>
