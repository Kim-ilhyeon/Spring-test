package com.practice.react;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.practice.react.member.domain.Member;
import com.practice.react.member.mapper.MemberMapper;
import com.practice.react.post.domain.Post;
import com.practice.react.post.mapper.PostMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql(statements = {
        "INSERT INTO post_category (category_id, category) VALUES (1, 'question')",
        "INSERT INTO post_category (category_id, category) VALUES (2, 'inquiry')"
})
class MyBatisMapperTests {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private PostMapper postMapper;

    @Test
    void memberAndPostMapperUseXmlQueries() {
        Member member = new Member(null, "mapper@test.com", "encoded-password", "테스터", (short) 25, (short) 1, null);
        assertEquals(1, memberMapper.insert(member));
        assertNotNull(member.getMemberId());

        Post post = new Post();
        post.setMemberId(member.getMemberId());
        post.setCategoryId((short) 1);
        post.setTitle("MyBatis 질문 테스트");
        post.setContent("Mapper XML을 통한 게시글 등록과 조인 조회를 확인합니다.");
        assertEquals(1, postMapper.insert(post));
        assertNotNull(post.getPostId());

        Post found = postMapper.findById(post.getPostId());
        assertEquals("question", found.getCategory());
        assertEquals("테스터", found.getAuthorName());
        assertEquals(1, postMapper.count(null, "MyBatis", "TITLE"));
        assertEquals(1, postMapper.findPage(0, 10, null, "조인", "CONTENT", "LATEST").size());
    }
}
