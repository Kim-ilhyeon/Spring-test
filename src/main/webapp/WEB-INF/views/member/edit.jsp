<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko"><head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 정보 수정</title><link rel="stylesheet" href="/css/api-practice.css">
</head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <h2>내 정보 수정</h2>
    <% if (request.getAttribute("message") != null) { %><p class="message success">${message}</p><% } %>
    <% if (request.getAttribute("error") != null) { %><p class="message">${error}</p><% } %>
    <form method="post" action="/member/edit">
        <label for="email">이메일</label><input id="email" name="email" value="${member.email}" disabled>
        <label for="currentPassword">현재 비밀번호</label><input id="currentPassword" name="currentPassword" type="password" required>
        <label for="name">이름</label><input id="name" name="name" value="${member.name}" required>
        <div class="row"><div><label for="age">나이</label><input id="age" name="age" type="number" value="${member.age}" required></div><div><label for="profileId">프로필 번호</label><input id="profileId" name="profileId" type="number" value="${member.profileId}" required></div></div>
        <label for="newPassword">새 비밀번호 (변경하지 않으면 비워두기)</label><input id="newPassword" name="newPassword" type="password">
        <button type="submit">수정 저장</button><a class="danger" href="/member/withdraw">회원탈퇴</a>
    </form>
</section></main>
</body></html>
