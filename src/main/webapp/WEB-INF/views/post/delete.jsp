<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><title>게시글 삭제</title><link rel="stylesheet" href="/css/api-practice.css"></head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <h2>게시글 삭제</h2><p class="message">'${post.title}' 게시글을 삭제합니다. 삭제 후에는 목록과 상세에서 보이지 않습니다.</p>
    <form method="post" action="/post/${post.postId}/delete">
        <input type="hidden" name="postId" value="${post.postId}">
        <button class="danger" type="submit">삭제 확인</button><a href="/post/detail/${post.postId}">취소</a>
    </form>
</section></main>
</body></html>
