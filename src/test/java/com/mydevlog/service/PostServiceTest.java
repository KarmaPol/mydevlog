package com.mydevlog.service;

import com.mydevlog.domain.Post;
import com.mydevlog.exception.PostNotFound;
import com.mydevlog.repository.PostRepository;
import com.mydevlog.request.PostCreate;
import com.mydevlog.request.PostEdit;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        // given
        Post post = Post.builder().title("호돌맨 제목 ")
                .content("내용!!!")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("호돌맨 수정").content("내용!!!").build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post findPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals(postEdit.getTitle(), findPost.getTitle());
        assertEquals(postEdit.getContent(), findPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        Post post = Post.builder().title("호돌맨 제목 ")
                .content("내용!!!")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("호돌맨 제목 ")
                .content("내용 수정").build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post findPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals(postEdit.getTitle(), findPost.getTitle());
        assertEquals(postEdit.getContent(), findPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        // given
        Post post = Post.builder().title("호돌맨 제목 ")
                .content("내용!!!")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("잘못된 ID로 게시글 조회")
    void tes7() {
        // given
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();
        Post postResponse = postService.write(postCreate);

        // when

        // then
        assertThrows(PostNotFound.class, ()-> postService.get(postResponse.getId() + 1));
    }

    @Test
    @DisplayName("존재하지 않는 글 삭제")
    void test8() {
        // given
        Post post = Post.builder().title("호돌맨 제목 ")
                .content("내용!!!")
                .build();
        postRepository.save(post);

        // when

        // then
        assertThrows(PostNotFound.class, ()-> postService.delete(post.getId() + 1));
    }

    @Test
    @DisplayName("존재하지 않는 글 수정")
    void test9() {
        // given
        Post post = Post.builder().title("호돌맨 제목 ")
                .content("내용!!!")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("호돌맨 수정").content("내용!!!").build();

        // when

        // then
        assertThrows(PostNotFound.class, ()-> postService.edit(post.getId() + 1, postEdit));
    }

    @Test
    @DisplayName("존재하지 않는 글 내용 수정")
    void test10() {
        // given
        Post post = Post.builder().title("호돌맨 제목 ")
                .content("내용!!!")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("호돌맨 제목 ")
                .content("내용 수정").build();

        // when

        // then
        assertThrows(PostNotFound.class, ()-> postService.edit(post.getId() + 1, postEdit));
    }
}