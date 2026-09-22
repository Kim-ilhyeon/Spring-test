<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><title>게시글 수정</title><link rel="stylesheet" href="/css/api-practice.css"></head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <h2>게시글 수정</h2>
    <% if (request.getAttribute("error") != null) { %><p class="message">${error}</p><% } %>
    <form method="post" action="/post/${post.postId}/edit">
        <input type="hidden" name="postId" value="${post.postId}">
        <label for="categoryId">카테고리</label><select id="categoryId" name="categoryId"><option value="1" ${post.categoryId == 1 ? 'selected' : ''}>question</option><option value="2" ${post.categoryId == 2 ? 'selected' : ''}>inquiry</option></select>
        <label for="title">제목</label><input id="title" name="title" value="${post.title}" maxlength="63" required>
        <label for="content">내용</label><textarea id="content" name="content" rows="10" required>${post.content}</textarea>
        <button type="submit">수정 저장</button><a href="/post/detail/${post.postId}">취소</a>
    </form>
</section></main>
</body></html>
