package com.delivery.food.user.service.impl;

import com.delivery.food.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security UserDetails implementation based on the User entity.
 *
 * - Uses email as username (since no separate username field)
 * - Maps roles to authorities with "ROLE_" prefix
 * - Accounts are enabled/disabled based on active flag
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final boolean active;
    private final List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.active = user.isActive();

        // Map role to authorities (e.g. CUSTOMER → ROLE_CUSTOMER)
        this.authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    /**
     * Factory method to build UserDetails from User entity.
     * Used in UserDetailsService.
     */
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;  // login by email
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    // Optional: equals & hashCode for caching (if using caching)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
