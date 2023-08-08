package com.mydevlog.service;

import com.mydevlog.domain.Session;
import com.mydevlog.domain.Users;
import com.mydevlog.exception.WrongSigningInformation;
import com.mydevlog.repository.UserRepository;
import com.mydevlog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    public String signin(Login login){
        Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new WrongSigningInformation());

        Session session = users.addSession();
        return session.getAccessToken();
    }
}
