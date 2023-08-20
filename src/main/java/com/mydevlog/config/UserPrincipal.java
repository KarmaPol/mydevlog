package com.mydevlog.config;

import com.mydevlog.domain.Users;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(Users user){
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
