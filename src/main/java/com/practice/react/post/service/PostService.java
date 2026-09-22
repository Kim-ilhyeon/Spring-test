package com.practice.react.post.service;

import com.practice.react.common.dto.PageResponse;
import com.practice.react.post.domain.Post;
import com.practice.react.post.dto.PostRequest;
import com.practice.react.post.dto.SearchType;
import com.practice.react.post.dto.SortType;

public interface PostService {
    /**
     * TODO: 게시글 작성 Service 메서드를 구현하세요.
     * TODO: 로그인 회원 번호와 PostRequest를 전달받아 새 Post를 만들고 Mapper insert를 호출하도록 구현하세요.
     */
    Post write();

    /**
     * 검색 조건과 정렬 조건을 적용한 게시글 한 페이지를 반환한다.
     */
    PageResponse<Post> getPosts(int page, int size, Short categoryId, String keyword, SearchType searchType, SortType sortType);

    /**
     * 조회수를 1 증가시킨 뒤 게시글 상세 정보를 반환한다.
     */
    Post getPost(Long postId);

    /**
     * 조회수를 증가시키지 않고 수정·삭제 화면에 사용할 게시글을 반환한다.
     */
    Post getPostForEdit(Long postId);

    /**
     * 로그인 회원이 작성자인 경우에만 게시글을 수정한다.
     */
    Post update(Long postId, Long memberId, PostRequest request);

    /**
     * 로그인 회원이 작성자인 경우에만 게시글을 논리 삭제한다.
     */
    void delete(Long postId, Long memberId);
}
