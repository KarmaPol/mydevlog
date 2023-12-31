package com.mydevlog.config;

import com.mydevlog.domain.Users;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {

    private final Long userId;

    // role: 역할 -> 관리자, 사용자, 매니저
    // authority: 권한 -> 글쓰기, 글 읽기

    public UserPrincipal(Users user){
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))); // 역할을 만들기 위해서는 ROLE_ prefix를 붙이고, 권한은 그대로 둔다.
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
