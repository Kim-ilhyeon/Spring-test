<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" href="/css/api-practice.css">
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>
    <section class="card">
        <h2>회원가입</h2>
        <% if (request.getAttribute("message") != null) { %><p class="message success">${message}</p><% } %>
        <% if (request.getAttribute("error") != null) { %><p class="message">${error}</p><% } %>

        <form id="joinForm" method="post" action="/member/join">
            <label for="email">이메일</label>
            <input id="email" name="email" type="email" placeholder="test@example.com" required>
            <div class="actions">
                <button id="sendCodeButton" type="button">인증번호 발송</button>
            </div>

            <label for="code">인증번호</label>
            <input id="code" name="code" placeholder="6자리 숫자" inputmode="numeric" maxlength="6">
            <div class="actions">
                <button id="verifyCodeButton" type="button">인증 확인</button>
            </div>
            <p id="verificationMessage" class="message" aria-live="polite">이메일 인증을 완료한 뒤 회원가입할 수 있습니다.</p>

            <label for="password">비밀번호</label><input id="password" name="password" type="password" required>
            <label for="name">이름</label><input id="name" name="name" required>
            <div class="row">
                <div><label for="age">나이</label><input id="age" name="age" type="number"></div>
                <div><label for="profileId">프로필 번호</label><input id="profileId" name="profileId" type="number"></div>
            </div>
            <button type="submit">회원가입</button>
        </form>
    </section>
</main>
</body>
<script src="${pageContext.request.contextPath}/js/join.js"></script>
</html>
