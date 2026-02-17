package com.vayurakshak.airquality.auth.service;

import com.vayurakshak.airquality.auth.dto.AuthResponse;
import com.vayurakshak.airquality.auth.dto.LoginRequest;
import com.vayurakshak.airquality.auth.dto.RegisterRequest;
import com.vayurakshak.airquality.auth.security.JwtService;
import com.vayurakshak.airquality.common.exception.BadRequestException;
import com.vayurakshak.airquality.common.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import com.vayurakshak.airquality.user.entity.User;
import com.vayurakshak.airquality.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthService.class);

    private static final String DEFAULT_ROLE = "ROLE_RESIDENT";

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        log.info("Registration attempt for email={}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registration blocked: email already exists -> {}", request.getEmail());
            throw new BadRequestException("Email already registered");
        }

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() -> {
                    log.error("Registration failed: organization not found -> {}",
                            request.getOrganizationId());
                    return new ResourceNotFoundException("Organization not found");
                });

        String role = resolveRole(request.getRole());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .organization(organization)
                .build();

        userRepository.save(user);

        log.info("User registered successfully: email={}, role={}, orgId={}",
                user.getEmail(),
                role,
                organization.getId());

        // ✅ generate token with claims
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest request) {

        log.info("Login attempt for email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: invalid credentials for email={}",
                            request.getEmail());
                    return new BadRequestException("Invalid credentials");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: invalid credentials for email={}",
                    request.getEmail());
            throw new BadRequestException("Invalid credentials");
        }

        log.info("Login successful for email={}, role={}",
                user.getEmail(),
                user.getRole());

        // ✅ generate token with claims
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    private String resolveRole(String requestedRole) {

        if (requestedRole == null || requestedRole.isBlank()) {
            return DEFAULT_ROLE;
        }

        if (requestedRole.equals("ROLE_ADMIN") ||
                requestedRole.equals("ROLE_RESIDENT")) {
            return requestedRole;
        }

        log.warn("Invalid role requested -> {}", requestedRole);
        throw new BadRequestException("Invalid role specified");
    }
}
