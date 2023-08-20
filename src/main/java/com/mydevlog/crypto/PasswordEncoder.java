package com.mydevlog.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class PasswordEncoder {

    private static final SCryptPasswordEncoder sCryptPasswordEncoder = new SCryptPasswordEncoder(256, 10, 10, 10, 10);
    public String encrypt(String password){
        return sCryptPasswordEncoder.encode(password);
    }

    public boolean matches(String rawPassword, String encryptedPassword){
        return sCryptPasswordEncoder.matches(rawPassword, encryptedPassword);
    }
}
