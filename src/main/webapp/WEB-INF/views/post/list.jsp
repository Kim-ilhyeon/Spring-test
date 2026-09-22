<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><title>게시글 목록</title><link rel="stylesheet" href="/css/api-practice.css"></head><body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main><section class="card">
    <div class="page-title"><h2>게시글 목록</h2><a href="/post/write">게시글 작성</a></div>
    <c:if test="${not empty message}"><p class="message success">${message}</p></c:if>
    <c:if test="${not empty error}"><p class="message">${error}</p></c:if>
    <form method="get" action="/post/list">
        <input type="hidden" name="page" value="1">
        <input type="hidden" name="size" value="10">
        <div class="row">
            <select id="category" name="categoryId"><option value="">전체 카테고리</option><option value="1" <c:if test="${categoryId == 1}">selected</c:if>>question</option><option value="2" <c:if test="${categoryId == 2}">selected</c:if>>inquiry</option></select>
            <select id="searchType" name="searchType"><option value="TITLE_CONTENT" <c:if test="${searchType == 'TITLE_CONTENT'}">selected</c:if>>제목+내용</option><option value="TITLE" <c:if test="${searchType == 'TITLE'}">selected</c:if>>제목</option><option value="CONTENT" <c:if test="${searchType == 'CONTENT'}">selected</c:if>>내용</option></select>
            <select id="sort" name="sort"><option value="LATEST" <c:if test="${sort == 'LATEST'}">selected</c:if>>최신순</option><option value="OLDEST" <c:if test="${sort == 'OLDEST'}">selected</c:if>>오래된순</option><option value="VIEWS" <c:if test="${sort == 'VIEWS'}">selected</c:if>>조회순</option></select>
            <input id="keyword" name="keyword" value="${keyword}" placeholder="검색어">
        </div>
        <button type="submit">검색</button>
    </form>
    <table><thead><tr><th>번호</th><th>카테고리</th><th>제목</th><th>작성자</th><th>조회수</th><th>작성일</th></tr></thead><tbody>
        <c:forEach items="${posts}" var="post"><tr><td>${post.postId}</td><td>${post.category}</td><td><a href="/post/detail/${post.postId}">${post.title}</a></td><td>${post.authorName}</td><td>${post.views}</td><td>${post.createdAt}</td></tr></c:forEach>
        <c:if test="${empty posts}"><tr><td colspan="6" class="empty">조회된 게시글이 없습니다.</td></tr></c:if>
    </tbody></table>
    <div class="pager">
        <c:if test="${currentPage > 1}"><a href="/post/list?page=${currentPage - 1}&size=10&categoryId=${categoryId}&searchType=${searchType}&sort=${sort}&keyword=${keyword}">이전</a></c:if>
        <span> ${currentPage} / ${totalPages} 페이지 </span>
        <c:if test="${hasNext}"><a href="/post/list?page=${currentPage + 1}&size=10&categoryId=${categoryId}&searchType=${searchType}&sort=${sort}&keyword=${keyword}">다음</a></c:if>
    </div>
</section></main>
</body></html>
