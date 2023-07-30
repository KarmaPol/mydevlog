package com.mydevlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydevlog.domain.Post;
import com.mydevlog.repository.PostRepository;
import com.mydevlog.request.PostCreate;
import com.mydevlog.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc // @WebMvcTest를 사용할 수 없을때, 해당 어노테이션으로 MockMvc만 사용가능하다
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void test() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // 객체 json화 해주는 java 라이브러리
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 Title 값은 필수")
    void test2() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // 객체 json화 해주는 java 라이브러리
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다")
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // 객체 json화 해주는 java 라이브러리
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());


        //then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder().title("123456789012345").content("bar").build();
        postRepository.save(post);

        //when
        mockMvc.perform(get("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        List<Post> requestPost = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder().title("호돌맨 제목 " + i)
                            .content("내용 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPost);

        //when
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지 0 조회")
    void test6() throws Exception {
        //given
        List<Post> requestPost = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder().title("호돌맨 제목 " + i)
                            .content("내용 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPost);

        //when
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        //given
        Post post = Post.builder().title("호돌맨 제목 ")
                .content("내용!!!")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("호돌맨 제목 ")
                .content("내용 수정").build();

        ObjectMapper objectMapper = new ObjectMapper(); // 객체 json화 해주는 java 라이브러리
        String json = objectMapper.writeValueAsString(postEdit);

        //when
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        //given
        Post post = Post.builder().title("호돌맨 제목 ")
                .content("내용!!!")
                .build();
        postRepository.save(post);

        //when
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(0L, postRepository.count());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test10() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder().title("호돌맨 제목 ")
                .content("내용 수정").build();

        ObjectMapper objectMapper = new ObjectMapper(); // 객체 json화 해주는 java 라이브러리
        String json = objectMapper.writeValueAsString(postEdit);

        //when
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보' 포함 금지")
    void test11() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // 객체 json화 해주는 java 라이브러리
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}