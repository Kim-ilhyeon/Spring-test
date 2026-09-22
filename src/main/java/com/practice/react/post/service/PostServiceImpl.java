package com.practice.react.post.service;

import com.practice.react.common.dto.PageResponse;
import com.practice.react.common.exception.ApiException;
import com.practice.react.post.domain.Post;
import com.practice.react.post.dto.PostRequest;
import com.practice.react.post.dto.SearchType;
import com.practice.react.post.dto.SortType;
import com.practice.react.post.mapper.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;

    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    /**
     * TODO: 게시글 작성 Service 구현을 완성하세요.
     * TODO: 카테고리 존재 여부를 확인하고, Post에 memberId·categoryId·제목·내용을 채우세요.
     * TODO: postMapper.insert(post)의 결과를 확인한 뒤 생성된 postId로 게시글을 다시 조회해 반환하세요.
     */
    @Override
    @Transactional
    public Post write() {
        // TODO: validateCategory(request.categoryId())를 호출하세요.
        // TODO: new Post()로 작성할 게시글을 만들고, postMapper.insert(post)를 호출하세요.
        // TODO: insert 후 post.getPostId()를 사용해 find(postId)를 반환하세요.
        return null;
    }

    /**
     * page/size를 검증하고 offset을 계산해 목록과 전체 개수를 조회한다.
     * sortType은 enum 값만 XML에 전달해 임의 SQL 정렬문이 들어오는 것을 막는다.
     */
    @Override
    public PageResponse<Post> getPosts(int page, int size, Short categoryId, String keyword,
                                       SearchType searchType, SortType sortType) {
        if (page < 1 || size < 1 || size > 50) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "page는 1 이상, size는 1~50 사이여야 합니다.");
        }
        if (categoryId != null && postMapper.countCategoryById(categoryId) == 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다.");
        }
        String type = (searchType == null ? SearchType.TITLE_CONTENT : searchType).name();
        String sort = (sortType == null ? SortType.LATEST : sortType).name();
        long total = postMapper.count(categoryId, keyword, type);
        return PageResponse.of(postMapper.findPage((page - 1) * size, size, categoryId, keyword, type, sort),
                page, size, total);
    }

    /** 게시글이 존재할 때만 조회수를 증가시키고 상세 데이터를 조회한다. */
    @Override
    @Transactional
    public Post getPost(Long postId) {
        if (postMapper.incrementViews(postId) == 0) {
            throw new ApiException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
        }
        return find(postId);
    }

    /** 수정 화면 진입 시 조회수가 늘지 않도록 단순 상세 조회만 수행한다. */
    @Override
    public Post getPostForEdit(Long postId) {
        return find(postId);
    }

    /** 카테고리를 검증하고 SQL의 member_id 조건으로 작성자만 수정하게 한다. */
    @Override
    @Transactional
    public Post update(Long postId, Long memberId, PostRequest request) {
        validateCategory(request.categoryId());
        Post post = new Post();
        post.setPostId(postId);
        post.setMemberId(memberId);
        post.setCategoryId(request.categoryId());
        post.setTitle(request.title().trim());
        post.setContent(request.content().trim());
        if (postMapper.update(post) == 0) {
            throw new ApiException(HttpStatus.FORBIDDEN, "작성자만 게시글을 수정할 수 있습니다.");
        }
        return find(postId);
    }

    /** SQL의 post_id·member_id·deleted_at 조건으로 작성자만 논리 삭제하게 한다. */
    @Override
    @Transactional
    public void delete(Long postId, Long memberId) {
        if (postMapper.delete(postId, memberId) == 0) {
            throw new ApiException(HttpStatus.FORBIDDEN, "작성자만 게시글을 삭제할 수 있습니다.");
        }
    }

    /** 삭제되지 않은 게시글을 한 건 찾고, 없으면 404 예외를 발생시킨다. */
    private Post find(Long postId) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
        }
        return post;
    }

    /** post_category에 존재하는 카테고리인지 확인한다. */
    private void validateCategory(Short categoryId) {
        if (postMapper.countCategoryById(categoryId) == 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다.");
        }
    }
}
