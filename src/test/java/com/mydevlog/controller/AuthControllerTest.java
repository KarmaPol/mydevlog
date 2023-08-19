package com.mydevlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydevlog.repository.PostRepository;
import com.mydevlog.repository.UserRepository;
import com.mydevlog.request.Signup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


    @BeforeEach
    void clean(){
        postRepository.deleteAllInBatch();
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