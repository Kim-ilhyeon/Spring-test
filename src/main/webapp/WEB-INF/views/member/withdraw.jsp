<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><title>회원탈퇴</title><link rel="stylesheet" href="/css/api-practice.css"></head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <h2>회원탈퇴</h2>
    <% if (request.getAttribute("error") != null) { %><p class="message">${error}</p><% } %>
    <p class="message">현재 비밀번호를 확인한 뒤 회원을 탈퇴 처리합니다.</p>
    <form method="post" action="/member/withdraw">
        <label for="password">현재 비밀번호</label><input id="password" name="password" type="password" required>
        <button class="danger" type="submit">회원탈퇴 확인</button><a href="/member/edit">취소</a>
    </form>
</section></main>
</body></html>
