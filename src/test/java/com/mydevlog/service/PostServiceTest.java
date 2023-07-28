package com.mydevlog.service;

import com.mydevlog.domain.Post;
import com.mydevlog.repository.PostRepository;
import com.mydevlog.request.PostCreate;
import com.mydevlog.request.PostSearch;
import com.mydevlog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        PostResponse post = postService.get(postResponse.getId());

        // then
        Assertions.assertNotNull(post);
        assertEquals(1L, postRepository.count());
        assertEquals("제목입니다.", postResponse.getTitle());
        assertEquals("내용입니다.", postResponse.getContent());
    }

    @Test
    @DisplayName("글 1 페이지 조회")
    void test3() {
        // given
        List<Post> requestPost = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder().title("호돌맨 제목 " + i)
                            .content("내용 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPost);

        PostSearch postSearch = PostSearch.builder().page(1).size(10).build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        Assertions.assertNotNull(posts);
        assertEquals(10L, posts.size());
        assertEquals("호돌맨 제목 19", posts.get(0).getTitle());
    }
}