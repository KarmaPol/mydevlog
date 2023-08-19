package com.mydevlog.service;

import com.mydevlog.crypto.PasswordEncoder;
import com.mydevlog.domain.Users;
import com.mydevlog.exception.AlreadyExistsEmailException;
import com.mydevlog.exception.WrongSigningInformation;
import com.mydevlog.repository.UserRepository;
import com.mydevlog.request.Login;
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

    public Long signin(Login login){
//        Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(() -> new WrongSigningInformation());

        Users user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new WrongSigningInformation());

        PasswordEncoder passwordEncoder = new PasswordEncoder();

        boolean matches = passwordEncoder.matches(login.getPassword(), user.getPassword());

        if(!matches){
            throw new WrongSigningInformation();
        }

        return user.getId();
    }

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
