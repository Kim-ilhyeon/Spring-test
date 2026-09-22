<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><title>게시글 작성</title><link rel="stylesheet" href="/css/api-practice.css"></head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <h2>게시글 작성</h2><p class="message">실습 과제: `POST /post/write`를 추가하고 로그인 회원을 작성자로 사용해 게시글을 저장하세요.</p>
    <form method="post" action="/post/write">
        <label for="categoryId">카테고리</label><select id="categoryId" name="categoryId"><option value="1">question</option><option value="2">inquiry</option></select>
        <label for="title">제목</label><input id="title" name="title" maxlength="63" required>
        <label for="content">내용</label><textarea id="content" name="content" rows="10" required></textarea>
        <button type="submit">등록</button><a href="/post/list?page=1">취소</a>
    </form>
</section></main>
</body></html>
