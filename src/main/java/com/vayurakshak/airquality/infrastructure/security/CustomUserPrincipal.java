package com.vayurakshak.airquality.infrastructure.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserPrincipal implements UserDetails {

    private final String email;
    private final String password;
    private final String role;
    private final Long orgId;
    private final String subscriptionPlan;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(String email,
                               String password,
                               String role,
                               Long orgId,
                               String subscriptionPlan,
                               Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.orgId = orgId;
        this.subscriptionPlan = subscriptionPlan;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}