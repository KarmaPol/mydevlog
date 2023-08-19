package com.mydevlog.controller;

import com.mydevlog.config.Appconfig;
import com.mydevlog.request.Signup;
import com.mydevlog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final Appconfig appconfig;

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
