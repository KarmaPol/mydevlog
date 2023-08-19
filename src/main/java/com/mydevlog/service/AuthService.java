package com.mydevlog.service;

import com.mydevlog.crypto.PasswordEncoder;
import com.mydevlog.domain.Users;
import com.mydevlog.exception.AlreadyExistsEmailException;
import com.mydevlog.repository.UserRepository;
import com.mydevlog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;


    public void signup(Signup signup) {
        Optional<Users> findUser = userRepository.findByEmail(signup.getEmail());
        if(findUser.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String encodedPassword = passwordEncoder.encrypt(signup.getPassword());

        Users user = Users.builder()
                .name(signup.getName())
                .password(encodedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
