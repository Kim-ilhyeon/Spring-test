package com.practice.react.post.dto;

import com.practice.react.post.domain.Post;
import java.time.LocalDateTime;

public record PostResponse(Long postId, Short categoryId, String category, Long memberId, String authorName,
                           String title, String content, int views, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static PostResponse from(Post post) {
        return new PostResponse(post.getPostId(), post.getCategoryId(), post.getCategory(), post.getMemberId(),
                post.getAuthorName(), post.getTitle(), post.getContent(), post.getViews(), post.getCreatedAt(), post.getUpdatedAt());
    }
}
