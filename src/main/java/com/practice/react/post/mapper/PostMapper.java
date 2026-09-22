package com.practice.react.post.mapper;

import com.practice.react.post.domain.Post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {
    /**
     * TODO: 게시글 작성 실습에서 이 메서드로 새 Post를 저장하세요. MyBatis의 useGeneratedKeys는 실행 후 생성된 postId를 객체에 채운다.
     * TODO: PostMapper.xml의 insert SQL에서 parameterType, #{필드명}, useGeneratedKeys, keyProperty의 의미를 확인하세요.
     */
    int insert();

    /**
     * 삭제되지 않은 게시글 한 건을 카테고리·작성자 정보와 함께 조회한다.
     */
    Post findById(@Param("postId") Long postId);

    /**
     * 카테고리·검색·정렬·offset/size 조건을 적용한 한 페이지의 게시글을 조회한다.
     */
    List<Post> findPage(@Param("offset") int offset, @Param("size") int size, @Param("categoryId") Short categoryId,
                        @Param("keyword") String keyword, @Param("searchType") String searchType, @Param("sortType") String sortType);

    /**
     * 목록과 같은 카테고리·검색 조건으로 전체 게시글 수를 조회한다.
     */
    long count(@Param("categoryId") Short categoryId, @Param("keyword") String keyword, @Param("searchType") String searchType);

    /**
     * 삭제되지 않은 게시글의 views를 1 증가시키고, 변경 행 수를 반환한다.
     */
    int incrementViews(@Param("postId") Long postId);

    /**
     * post_id와 member_id가 모두 일치하는 경우에만 제목·내용·카테고리를 수정한다.
     */
    int update(Post post);

    /**
     * post_id와 member_id가 모두 일치하는 경우에만 deleted_at을 채운다.
     */
    int delete(@Param("postId") Long postId, @Param("memberId") Long memberId);

    /**
     * 전달된 카테고리 ID가 post_category에 존재하는지 확인한다.
     */
    int countCategoryById(@Param("categoryId") Short categoryId);
}
