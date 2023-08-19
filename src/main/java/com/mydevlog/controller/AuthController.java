package com.mydevlog.controller;

import com.mydevlog.config.Appconfig;
import com.mydevlog.request.Login;
import com.mydevlog.request.Signup;
import com.mydevlog.response.SessionResponse;
import com.mydevlog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final Appconfig appconfig;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){
        Long userID = authService.signin(login);

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        byte[] encodedKey = key.getEncoded();

        SecretKey secretKey = Keys.hmacShaKeyFor(appconfig.getJwtKey()); // String -> Byte[] -> Key

        String strKey = Base64.getEncoder().encodeToString(encodedKey); // 키를 String으로 만들어준 후 서버에 계속 보관

        String jws = Jwts.builder().setSubject(String.valueOf(userID)).signWith(secretKey).setIssuedAt(new Date()).compact();

        return new SessionResponse(jws);
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup){
        authService.signup(signup);
    }

}