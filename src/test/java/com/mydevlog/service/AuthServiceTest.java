package com.mydevlog.service;

import com.mydevlog.domain.Users;
import com.mydevlog.exception.AlreadyExistsEmailException;
import com.mydevlog.exception.WrongSigningInformation;
import com.mydevlog.repository.UserRepository;
import com.mydevlog.request.Login;
import com.mydevlog.request.Signup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 중복 이메일 테스트")
    void test1(){
        // given
        Signup signup1 = Signup.builder()
                .email("kim@gmail.com")
                .password("1234")
                .name("kim11")
                .build();

        Signup signup2 = Signup.builder()
                .email("kim@gmail.com")
                .password("1234")
                .name("kimkyunghun")
                .build();

        authService.signup(signup1);

        // expected
        Assertions.assertThrows(AlreadyExistsEmailException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                authService.signup(signup2);
            }
        });
    }

    @Test
    @DisplayName("회원가입 테스트")
    void test2(){
        // given
        Signup signup = Signup.builder()
                .email("kim@gmail.com")
                .password("1234")
                .name("kimkyunghun")
                .build();

        // when
        authService.signup(signup);

        // then
        Assertions.assertEquals(userRepository.count(), 1L);

        Users user = userRepository.findAll().iterator().next();
        assertEquals(user.getEmail(), "kim@gmail.com");
        assertNotEquals(user.getPassword(), "1234");
        assertNotNull(user.getPassword());
    }

    @Test
    @DisplayName("로그인 테스트")
    void test3(){
        // given
        Signup signup = Signup.builder()
                .email("kim@gmail.com")
                .password("1234")
                .name("kimkyunghun")
                .build();
        authService.signup(signup);

        Login login = Login.builder().email("kim@gmail.com").password("1234").build();

        // when
        authService.signin(login);

        // then
        Assertions.assertEquals(userRepository.count(), 1L);

        Users user = userRepository.findAll().iterator().next();
        assertEquals(user.getEmail(), "kim@gmail.com");
        assertNotNull(user.getPassword());
    }

    @Test
    @DisplayName("로그인 비밀번호 틀림")
    void test4(){
        // given
        Signup signup = Signup.builder()
                .email("kim@gmail.com")
                .password("1234")
                .name("kimkyunghun")
                .build();
        authService.signup(signup);

        Login login = Login.builder().email("kim@gmail.com").password("12354").build();

        // expected
        Assertions.assertThrows(WrongSigningInformation.class, () -> authService.signin(login));
    }
}