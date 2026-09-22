<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><title>게시글 상세</title><link rel="stylesheet" href="/css/api-practice.css"></head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <div class="page-title"><h2>게시글 상세</h2><a href="/post/list?page=1">목록</a></div>
    <% if (request.getAttribute("message") != null) { %><p class="message success">${message}</p><% } %>
    <% if (request.getAttribute("error") != null) { %><p class="message">${error}</p><% } %>
    <h3>${post.title}</h3>
    <p>카테고리: ${post.category} | 작성자: ${post.authorName} | 조회수: ${post.views}</p>
    <p>작성일: ${post.createdAt} | 수정일: ${post.updatedAt}</p>
    <div class="content-box">${post.content}</div>
    <div class="actions"><a href="/post/${post.postId}/edit">수정</a><a class="danger" href="/post/${post.postId}/delete">삭제</a></div>
</section></main>
</body></html>
