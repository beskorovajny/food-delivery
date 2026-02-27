package com.delivery.food.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SimpleUserDetails implements UserDetails {

    private final String email;
    private final List<GrantedAuthority> authorities;

    public SimpleUserDetails(String email) {
        this.email = email;
        this.authorities = List.of(); // or populate from roles if needed
    }

    public SimpleUserDetails(String email, List<String> roles) {
        this.email = email;
        this.authorities = roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // not used here (credentials validated elsewhere)
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
