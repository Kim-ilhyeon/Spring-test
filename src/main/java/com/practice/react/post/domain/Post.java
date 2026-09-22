package com.practice.react.post.domain;

import java.time.LocalDateTime;

/**
 * post 테이블과 목록·상세 조회의 조인 결과를 표현하는 Domain 객체.
 * category와 authorName은 post 테이블 컬럼이 아니라 JOIN으로 가져오는 화면용 값이다.
 */
public class Post {
    /** 게시글 기본 키. */
    private Long postId;
    /** post_category를 가리키는 외래 키. */
    private Short categoryId;
    /** JOIN으로 조회한 카테고리 이름(question 또는 inquiry). */
    private String category;
    /** 작성 회원의 member_id. 수정·삭제 권한 비교에 사용한다. */
    private Long memberId;
    /** JOIN으로 조회한 작성자 이름. */
    private String authorName;
    /** 게시글 제목. */
    private String title;
    /** 게시글 본문. */
    private String content;
    /** 상세 조회 시 증가하는 조회수. */
    private int views;
    /** 게시글 생성 시각. */
    private LocalDateTime createdAt;
    /** 마지막 수정 시각. */
    private LocalDateTime updatedAt;

    /** MyBatis가 조회 결과를 객체에 채울 때 사용하는 기본 생성자. */
    public Post() { }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Short getCategoryId() { return categoryId; }
    public void setCategoryId(Short categoryId) { this.categoryId = categoryId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
