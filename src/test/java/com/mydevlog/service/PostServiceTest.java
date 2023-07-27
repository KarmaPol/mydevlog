package com.mydevlog.service;

import com.mydevlog.domain.Post;
import com.mydevlog.repository.PostRepository;
import com.mydevlog.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 단건 조회")
    void test2() {
        // given
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();
        Post postResponse = postService.write(postCreate);

        // when
        Post post = postService.get(postResponse.getId());

        // then
        Assertions.assertNotNull(post);
        assertEquals(1L, postRepository.count());
        assertEquals("제목입니다.", postResponse.getTitle());
        assertEquals("내용입니다.", postResponse.getContent());
    }
}