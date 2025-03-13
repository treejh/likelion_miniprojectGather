package com.example.likelion_miniprojectgather.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomerUserDetails implements UserDetails {

    Long id;
    String email;
    String password;

    public CustomerUserDetails(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 현재는 권한을 사용하지 않으므로 빈 리스트 반환
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 X
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 X
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 X
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 상태
    }
}
