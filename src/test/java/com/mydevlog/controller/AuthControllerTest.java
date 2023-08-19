package com.mydevlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydevlog.domain.Session;
import com.mydevlog.domain.Users;
import com.mydevlog.repository.PostRepository;
import com.mydevlog.repository.SessionRepository;
import com.mydevlog.repository.UserRepository;
import com.mydevlog.request.Login;
import com.mydevlog.request.Signup;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 생성")
    void test() throws Exception {

        //given
        Users user = userRepository.save(Users.builder()
                .email("test@email")
                .password("1234")
                .name("테스트")
                .build());

        Login login = Login.builder()
                        .email("test@email")
                                .password("1234").build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()));
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
    void test4() throws Exception {

        // given
        Users user = userRepository.save(Users.builder()
                .email("test@email")
                .password("1234")
                .name("테스트")
                .build());
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()));
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션으로 권한이 필요한 페이지 접속한다 /foo")
    void test5() throws Exception {

        // given
        Users user = userRepository.save(Users.builder()
                .email("test@email")
                .password("1234")
                .name("테스트")
                .build());
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()));
    }

    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {

        // given
        Signup signup = Signup.builder()
                .email("kim@gmail.com")
                .password("1234")
                .name("kimkyunghun")
                .build();

        // expected
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}