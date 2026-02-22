package com.vayurakshak.airquality.auth.service;

import com.vayurakshak.airquality.auth.dto.AuthResponse;
import com.vayurakshak.airquality.auth.dto.LoginRequest;
import com.vayurakshak.airquality.auth.dto.RegisterRequest;
import com.vayurakshak.airquality.infrastructure.exception.BadRequestException;
import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.infrastructure.security.JwtService;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import com.vayurakshak.airquality.user.entity.User;
import com.vayurakshak.airquality.user.enums.UserRole;
import com.vayurakshak.airquality.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final UserRole DEFAULT_ROLE = UserRole.ROLE_RESIDENT;

    /**
     * User Registration
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCaseAndDeletedFalse(email)) {
            throw new BadRequestException("Email already registered");
        }

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Organization not found"));

        UserRole role = request.getRole() != null
                ? request.getRole()
                : DEFAULT_ROLE;

        // 🔒 Prevent public admin creation
        if (role == UserRole.ROLE_ADMIN) {
            throw new BadRequestException("Admin registration is restricted");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role) // ✅ ENUM (not string)
                .organization(organization)
                .build();

        userRepository.save(user);

        log.info("User registered successfully: email={}, orgId={}",
                email, organization.getId());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    /**
     * User Login
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository
                .findByEmailIgnoreCaseAndDeletedFalse(email)
                .orElseThrow(() ->
                        new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        log.info("Login successful: email={}, role={}",
                email, user.getRole());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}