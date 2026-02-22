package com.vayurakshak.airquality.infrastructure.security;

import com.vayurakshak.airquality.user.entity.User;
import com.vayurakshak.airquality.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByEmailIgnoreCaseAndDeletedFalse(email.trim())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials"));

        return new CustomUserPrincipal(
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getOrganization().getId(),
                user.getOrganization().getPlan().name(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}